package com.pechenka.app.controller;

import com.pechenka.app.entity.Stock;
import com.pechenka.app.service.StockService;
import com.pechenka.app.service.ProductService;
import com.pechenka.app.service.WarehouseService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/stock")
public class StockController {

    private final StockService stockService;
    private final ProductService productService;
    private final WarehouseService warehouseService;

    public StockController(StockService stockService,
                           ProductService productService,
                           WarehouseService warehouseService) {
        this.stockService = stockService;
        this.productService = productService;
        this.warehouseService = warehouseService;
    }

    // список всех остатков
    @GetMapping
    public String listStock(Model model) {
        List<Stock> stockItems = stockService.findAll();
        model.addAttribute("stockItems", stockItems);
        return "admin/stock/list";
    }

    // форма добавления остатка
    @GetMapping("/new")
    public String showStockForm(Model model) {
        model.addAttribute("stock", new Stock());
        model.addAttribute("products", productService.findAll());
        model.addAttribute("warehouses", warehouseService.findAll());
        return "admin/stock/form";
    }

    // сохранение остатка
    @PostMapping
    public String saveStock(@Valid @ModelAttribute("stock") Stock stock,
                            BindingResult result,
                            Model model,
                            RedirectAttributes redirectAttributes) {

        // проверяем валидацию
        if (result.hasErrors()) {
            model.addAttribute("products", productService.findAll());
            model.addAttribute("warehouses", warehouseService.findAll());
            return "admin/stock/form";
        }

        // проверяем, что товар и склад выбраны
        if (stock.getProduct() == null || stock.getProduct().getId() == null) {
            model.addAttribute("error", "Товар не выбран");
            model.addAttribute("products", productService.findAll());
            model.addAttribute("warehouses", warehouseService.findAll());
            return "admin/stock/form";
        }

        if (stock.getWarehouse() == null || stock.getWarehouse().getId() == null) {
            model.addAttribute("error", "Склад не выбран");
            model.addAttribute("products", productService.findAll());
            model.addAttribute("warehouses", warehouseService.findAll());
            return "admin/stock/form";
        }

        // проверяем уникальность сочетания товар-склад
        Optional<Stock> existingStock = stockService.findByProductAndWarehouse(
                stock.getProduct().getId(),
                stock.getWarehouse().getId()
        );

        if (stock.getId() == null) {
            // создание нового остатка
            if (existingStock.isPresent()) {
                Stock existing = existingStock.get();
                model.addAttribute("error",
                        "Остаток для товара '" + stock.getProduct().getName() +
                                "' на складе '" + stock.getWarehouse().getAddress() +
                                "' уже существует (ID: " + existing.getId() + "). " +
                                "Используйте редактирование существующего остатка.");
                model.addAttribute("products", productService.findAll());
                model.addAttribute("warehouses", warehouseService.findAll());
                return "admin/stock/form";
            }
        } else {
            // редактирование существующего остатка
            if (existingStock.isPresent() && !existingStock.get().getId().equals(stock.getId())) {
                Stock existing = existingStock.get();
                model.addAttribute("error",
                        "Остаток для товара '" + stock.getProduct().getName() +
                                "' на складе '" + stock.getWarehouse().getAddress() +
                                "' уже существует (ID: " + existing.getId() + "). " +
                                "Вы не можете изменить товар или склад на уже существующее сочетание.");
                model.addAttribute("products", productService.findAll());
                model.addAttribute("warehouses", warehouseService.findAll());
                return "admin/stock/form";
            }
        }

        // если проверка пройдена, сохраняем
        stockService.save(stock);

        if (stock.getId() == null) {
            redirectAttributes.addFlashAttribute("success", "Остаток успешно добавлен!");
        } else {
            redirectAttributes.addFlashAttribute("success", "Остаток успешно обновлен!");
        }

        return "redirect:/admin/stock";
    }

    // форма редактирования остатка
    @GetMapping("/{id}/edit")
    public String editStock(@PathVariable("id") Integer id, Model model) {
        Stock stock = stockService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Неверный ID остатка: " + id));

        model.addAttribute("stock", stock);
        model.addAttribute("products", productService.findAll());
        model.addAttribute("warehouses", warehouseService.findAll());
        return "admin/stock/form";
    }

    // обновление количества
    @PostMapping("/{id}/update")
    public String updateStockQuantity(@PathVariable("id") Integer id,
                                      @RequestParam("quantity") Integer quantity,
                                      RedirectAttributes redirectAttributes) {
        stockService.updateQuantity(id, quantity);
        redirectAttributes.addFlashAttribute("success", "Количество успешно обновлено!");
        return "redirect:/admin/stock";
    }

    // удаление остатка
    @PostMapping("/{id}/delete")
    public String deleteStock(@PathVariable("id") Integer id,
                              RedirectAttributes redirectAttributes) {
        stockService.delete(id);
        redirectAttributes.addFlashAttribute("success", "Остаток успешно удален!");
        return "redirect:/admin/stock";
    }

    // низкие остатки
    @GetMapping("/low")
    public String lowStock(Model model) {
        List<Stock> lowStockItems = stockService.findLowStockItems(10);
        model.addAttribute("stockItems", lowStockItems);
        model.addAttribute("title", "Товары с низким запасом (меньше 10)");
        return "admin/stock/list";
    }
}