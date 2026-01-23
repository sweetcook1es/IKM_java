package com.pechenka.app.controller;

import com.pechenka.app.entity.Warehouse;
import com.pechenka.app.service.WarehouseService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;

@Controller
@RequestMapping("/admin/warehouses")
public class WarehouseController {

    private final WarehouseService warehouseService;

    public WarehouseController(WarehouseService warehouseService) {
        this.warehouseService = warehouseService;
    }

    // список всех складов
    @GetMapping
    public String listWarehouses(Model model) {
        List<Warehouse> warehouses = warehouseService.findAll();
        model.addAttribute("warehouses", warehouses);
        return "admin/warehouses/list";
    }

    // форма добавления склада
    @GetMapping("/new")
    public String showWarehouseForm(Model model) {
        model.addAttribute("warehouse", new Warehouse());
        return "admin/warehouses/form";
    }

    // сохранение нового склада
    @PostMapping
    public String saveWarehouse(@Valid @ModelAttribute("warehouse") Warehouse warehouse,
                                BindingResult result,
                                RedirectAttributes redirectAttributes,
                                Model model) {

        // проверка на дубликат адреса (только при создании нового)
        if (warehouse.getId() == null) {
            List<Warehouse> existingWarehouses = warehouseService.findByAddressContaining(warehouse.getAddress());
            // поиск точного совпадения адреса
            boolean isDuplicate = existingWarehouses.stream()
                    .anyMatch(w -> w.getAddress().equalsIgnoreCase(warehouse.getAddress().trim()));

            if (isDuplicate) {
                result.rejectValue("address", "duplicate.address",
                        "Склад с таким адресом уже существует. Адрес должен быть уникальным.");
            }
        }

        if (result.hasErrors()) {
            return "admin/warehouses/form";
        }

        try {
            warehouseService.save(warehouse);
            redirectAttributes.addFlashAttribute("success", "Склад успешно сохранен!");
            return "redirect:/admin/warehouses";
        } catch (Exception e) {
            if (e.getCause() != null && e.getCause().getMessage().contains("warehouses_address_key")) {
                model.addAttribute("error", "Склад с таким адресом уже существует. Пожалуйста, укажите другой адрес.");
                return "admin/warehouses/form";
            }
            throw e;
        }
    }

    // форма редактирования склада
    @GetMapping("/{id}/edit")
    public String editWarehouse(@PathVariable("id") Integer id, Model model) {
        Warehouse warehouse = warehouseService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Неверный ID склада: " + id));

        model.addAttribute("warehouse", warehouse);
        return "admin/warehouses/form";
    }

    // обновление склада
    @PostMapping("/{id}")
    public String updateWarehouse(@PathVariable("id") Integer id,
                                  @Valid @ModelAttribute("warehouse") Warehouse warehouse,
                                  BindingResult result,
                                  RedirectAttributes redirectAttributes,
                                  Model model) {

        // проверяем, что ID в пути совпадает с ID в объекте
        if (!id.equals(warehouse.getId())) {
            warehouse.setId(id);
        }

        // проверка на дубликат адреса
        List<Warehouse> existingWarehouses = warehouseService.findByAddressContaining(warehouse.getAddress());
        boolean isDuplicate = existingWarehouses.stream()
                .anyMatch(w ->
                        w.getAddress().equalsIgnoreCase(warehouse.getAddress().trim()) &&
                                !w.getId().equals(warehouse.getId())
                );

        if (isDuplicate) {
            result.rejectValue("address", "duplicate.address",
                    "Склад с таким адресом уже существует. Адрес должен быть уникальным.");
        }

        if (result.hasErrors()) {
            return "admin/warehouses/form";
        }

        try {
            warehouseService.updateWarehouseAddress(id, warehouse.getAddress());
            redirectAttributes.addFlashAttribute("success", "Адрес склада успешно обновлен! Остатки товаров сохранены.");
            return "redirect:/admin/warehouses/" + id;
        } catch (Exception e) {
            if (e.getCause() != null && e.getCause().getMessage().contains("warehouses_address_key")) {
                model.addAttribute("error", "Склад с таким адресом уже существует. Пожалуйста, укажите другой адрес.");
                return "admin/warehouses/form";
            }
            model.addAttribute("error", "Ошибка при обновлении склада: " + e.getMessage());
            return "admin/warehouses/form";
        }
    }

    // удаление склада
    @PostMapping("/{id}/delete")
    public String deleteWarehouse(@PathVariable("id") Integer id,
                                  RedirectAttributes redirectAttributes) {
        try {
            warehouseService.delete(id);
            redirectAttributes.addFlashAttribute("success", "Склад успешно удален!");
        } catch (DataIntegrityViolationException | IllegalStateException e) {
            // Ловим ошибку целостности данных
            redirectAttributes.addFlashAttribute("error",
                    "Невозможно удалить склад. На нем есть товары. " +
                            "Сначала переместите или удалите все товары со склада.");
        }
        return "redirect:/admin/warehouses";
    }

    // просмотр склада
    @GetMapping("/{id}")
    public String viewWarehouse(@PathVariable("id") Integer id, Model model) {
        Warehouse warehouse = warehouseService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Неверный ID склада: " + id));

        model.addAttribute("warehouse", warehouse);
        return "admin/warehouses/view";
    }

    // поиск складов
    @GetMapping("/search")
    public String searchWarehouses(@RequestParam("keyword") String keyword, Model model) {
        List<Warehouse> warehouses = warehouseService.findByAddressContaining(keyword);
        model.addAttribute("warehouses", warehouses);
        model.addAttribute("keyword", keyword);
        return "admin/warehouses/list";
    }
}