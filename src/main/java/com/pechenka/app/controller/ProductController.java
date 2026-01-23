package com.pechenka.app.controller;

import com.pechenka.app.entity.Product;
import com.pechenka.app.service.ProductService;
import com.pechenka.app.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/products")
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;

    public ProductController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    // список всех товаров
    @GetMapping
    public String listProducts(Model model) {
        List<Product> products = productService.findAllWithDetails();
        model.addAttribute("products", products);
        return "admin/products/list";
    }

    // форма добавления товара
    @GetMapping("/new")
    public String showProductForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryService.findAll());
        return "admin/products/form";
    }

    // сохранение товара
    @PostMapping
    public String saveProduct(@Valid @ModelAttribute("product") Product product,
                              BindingResult result,
                              Model model,
                              RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("categories", categoryService.findAll());
            return "admin/products/form";
        }

        productService.save(product);
        redirectAttributes.addFlashAttribute("success", "Товар успешно сохранен!");
        return "redirect:/admin/products";
    }

    // форма редактирования товара
    @GetMapping("/{id}/edit")
    public String editProduct(@PathVariable("id") Integer id, Model model) {
        Product product = productService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Неверный ID товара: " + id));

        model.addAttribute("product", product);
        model.addAttribute("categories", categoryService.findAll());
        return "admin/products/form";
    }

    // обновление товара
    @PostMapping("/{id}")
    public String updateProduct(@PathVariable("id") Integer id,
                                @Valid @ModelAttribute("product") Product product,
                                BindingResult result,
                                Model model,
                                RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("categories", categoryService.findAll());
            return "admin/products/form";
        }

        product.setId(id);
        productService.save(product);
        redirectAttributes.addFlashAttribute("success", "Товар успешно обновлен!");
        return "redirect:/admin/products";
    }

    // просмотр товара
    @GetMapping("/{id}")
    public String viewProduct(@PathVariable("id") Integer id, Model model) {
        Product product = productService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Неверный ID товара: " + id));

        model.addAttribute("product", product);
        return "admin/products/view";
    }

    // удаление товара
    @PostMapping("/{id}/delete")
    public String deleteProduct(@PathVariable("id") Integer id,
                                RedirectAttributes redirectAttributes) {
        productService.delete(id);
        redirectAttributes.addFlashAttribute("success", "Товар успешно удален!");
        return "redirect:/admin/products";
    }

    // поиск товаров
    @GetMapping("/search")
    public String searchProducts(@RequestParam("keyword") String keyword, Model model) {
        List<Product> products = productService.search(keyword);
        model.addAttribute("products", products);
        model.addAttribute("keyword", keyword);
        return "admin/products/list";
    }

    // товары по категории
    @GetMapping("/category/{categoryId}")
    public String productsByCategory(@PathVariable("categoryId") Integer categoryId, Model model) {
        List<Product> products = productService.findByCategoryId(categoryId);
        model.addAttribute("products", products);
        return "admin/products/list";
    }
}