package com.pechenka.app.controller;

import com.pechenka.app.entity.Category;
import com.pechenka.app.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    // список всех категорий
    @GetMapping
    public String listCategories(Model model) {
        List<Category> categories = categoryService.findAll();
        model.addAttribute("categories", categories);
        return "admin/categories/list";
    }

    // форма добавления категории
    @GetMapping("/new")
    public String showCategoryForm(Model model) {
        model.addAttribute("category", new Category());
        return "admin/categories/form";
    }

    // сохранение новой категории
    @PostMapping
    public String saveCategory(@Valid @ModelAttribute("category") Category category,
                               BindingResult result,
                               RedirectAttributes redirectAttributes) {

        // проверка уникальности названия для новой категории
        if (category.getId() == null && categoryService.findByName(category.getName()).isPresent()) {
            result.rejectValue("name", "error.category",
                    "Категория с таким названием уже существует");
        }

        if (result.hasErrors()) {
            return "admin/categories/form";
        }

        categoryService.save(category);
        redirectAttributes.addFlashAttribute("success", "Категория успешно сохранена!");
        return "redirect:/admin/categories";
    }

    // форма редактирования категории
    @GetMapping("/{id}/edit")
    public String editCategory(@PathVariable("id") Integer id, Model model) {
        Category category = categoryService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Неверный ID категории: " + id));

        model.addAttribute("category", category);
        return "admin/categories/form";
    }

    // обновление существующей категории
    @PostMapping("/{id}")
    public String updateCategory(@PathVariable("id") Integer id,
                                 @Valid @ModelAttribute("category") Category categoryDetails,
                                 BindingResult result,
                                 RedirectAttributes redirectAttributes) {

        Category existingCategory = categoryService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Неверный ID категории: " + id));

        // проверка уникальности названия при редактировании
        if (!existingCategory.getName().equals(categoryDetails.getName())) {
            // если имя изменилось, проверяем уникальность нового имени
            if (categoryService.findByName(categoryDetails.getName()).isPresent()) {
                result.rejectValue("name", "error.category",
                        "Категория с таким названием уже существует");
            }
        }

        if (result.hasErrors()) {
            // восстанавливаем ID для правильного отображения формы
            categoryDetails.setId(id);
            return "admin/categories/form";
        }

        // обновляем только название и описание
        existingCategory.setName(categoryDetails.getName());
        existingCategory.setDescription(categoryDetails.getDescription());

        categoryService.save(existingCategory);
        redirectAttributes.addFlashAttribute("success", "Категория успешно обновлена!");
        return "redirect:/admin/categories";
    }

    // просмотр категории
    @GetMapping("/{id}")
    public String viewCategory(@PathVariable("id") Integer id, Model model) {
        Category category = categoryService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Неверный ID категории: " + id));

        model.addAttribute("category", category);
        return "admin/categories/view";
    }

    // удаление категории
    @PostMapping("/{id}/delete")
    public String deleteCategory(@PathVariable("id") Integer id,
                                 RedirectAttributes redirectAttributes) {
        try {
            Category category = categoryService.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Неверный ID категории: " + id));

            // можно добавить предупреждение пользователю
            int productCount = category.getProducts().size();

            categoryService.delete(id);

            if (productCount > 0) {
                redirectAttributes.addFlashAttribute("warning",
                        "Категория успешно удалена вместе с " + productCount + " товаром(ами)!");
            } else {
                redirectAttributes.addFlashAttribute("success", "Категория успешно удалена!");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error",
                    "Ошибка при удалении категории: " + e.getMessage());
            return "redirect:/admin/categories/" + id;
        }

        return "redirect:/admin/categories";
    }

    // поиск категорий
    @GetMapping("/search")
    public String searchCategories(@RequestParam("keyword") String keyword, Model model) {
        List<Category> categories = categoryService.findByNameContaining(keyword);
        model.addAttribute("categories", categories);
        model.addAttribute("keyword", keyword);
        return "admin/categories/list";
    }
}