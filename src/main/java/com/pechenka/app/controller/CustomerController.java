package com.pechenka.app.controller;

import com.pechenka.app.entity.Customer;
import com.pechenka.app.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    // список всех клиентов
    @GetMapping
    public String listCustomers(Model model) {
        List<Customer> customers = customerService.findAll();
        model.addAttribute("customers", customers);
        return "admin/customers/list";
    }

    // форма добавления клиента
    @GetMapping("/new")
    public String showCustomerForm(Model model) {
        model.addAttribute("customer", new Customer());
        return "admin/customers/form";
    }

    // сохранение клиента
    @PostMapping
    public String saveCustomer(@Valid @ModelAttribute("customer") Customer customer,
                               BindingResult result,
                               RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "admin/customers/form";
        }

        customerService.save(customer);
        redirectAttributes.addFlashAttribute("success", "Клиент успешно сохранен!");
        return "redirect:/admin/customers";
    }

    // форма редактирования клиента
    @GetMapping("/{id}/edit")
    public String editCustomer(@PathVariable("id") Integer id, Model model) {
        Customer customer = customerService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Неверный ID клиента: " + id));

        model.addAttribute("customer", customer);
        return "admin/customers/form";
    }

    // просмотр клиента
    @GetMapping("/{id}")
    public String viewCustomer(@PathVariable("id") Integer id, Model model) {
        Customer customer = customerService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Неверный ID клиента: " + id));

        model.addAttribute("customer", customer);
        return "admin/customers/view";
    }

    // удаление клиента
    @PostMapping("/{id}/delete")
    public String deleteCustomer(@PathVariable("id") Integer id,
                                 RedirectAttributes redirectAttributes) {
        customerService.delete(id);
        redirectAttributes.addFlashAttribute("success", "Клиент успешно удален!");
        return "redirect:/admin/customers";
    }

    // поиск клиентов
    @GetMapping("/search")
    public String searchCustomers(@RequestParam(value = "name", required = false) String name,
                                  Model model) {

        System.out.println("=== ПОИСК ===");
        System.out.println("Ключевое слово: " + name);

        List<Customer> customers;

        if (name != null && !name.trim().isEmpty()) {
            String searchTerm = name.trim();

            customers = customerService.searchByNameOrPhone(searchTerm);

            System.out.println("Найдено клиентов: " + customers.size());

            // отладочный вывод найденных клиентов
            for (Customer c : customers) {
                System.out.println("Найден: ID=" + c.getId() +
                        ", Имя=" + c.getFullName() +
                        ", Телефон=" + c.getPhone());
            }
        } else {
            customers = customerService.findAll();
            System.out.println("Показаны все клиенты: " + customers.size());
        }

        model.addAttribute("customers", customers);
        model.addAttribute("name", name);
        return "admin/customers/list";
    }
}