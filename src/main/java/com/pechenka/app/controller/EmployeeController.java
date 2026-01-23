package com.pechenka.app.controller;

import com.pechenka.app.entity.Employee;
import com.pechenka.app.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    // список всех сотрудников
    @GetMapping
    public String listEmployees(Model model) {
        List<Employee> employees = employeeService.findAll();
        model.addAttribute("employees", employees);
        return "admin/employees/list";
    }

    // форма добавления сотрудника
    @GetMapping("/new")
    public String showEmployeeForm(Model model) {
        model.addAttribute("employee", new Employee());
        return "admin/employees/form";
    }

    // сохранение сотрудника
    @PostMapping
    public String saveEmployee(@Valid @ModelAttribute("employee") Employee employee,
                               BindingResult result,
                               RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return "admin/employees/form";
        }

        // проверка уникальности табельного номера
        Optional<Employee> existingEmployee = employeeService.findByPersonnelNumber(employee.getPersonnelNumber());

        if (existingEmployee.isPresent()) {
            // если это редактирование и ID совпадают - разрешаем
            if (employee.getId() != null && employee.getId().equals(existingEmployee.get().getId())) {
                // это тот же сотрудник - OK
            } else {
                // это другой сотрудник с таким же табельным номером - ошибка
                result.rejectValue("personnelNumber", "error.employee",
                        "Сотрудник с таким табельным номером уже существует");
                return "admin/employees/form";
            }
        }

        employeeService.save(employee);
        redirectAttributes.addFlashAttribute("success",
                employee.getId() != null ? "Сотрудник успешно обновлен!" : "Сотрудник успешно добавлен!");
        return "redirect:/admin/employees";
    }

    // форма редактирования сотрудника
    @GetMapping("/{id}/edit")
    public String editEmployee(@PathVariable("id") Integer id, Model model) {
        Employee employee = employeeService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Неверный ID сотрудника: " + id));

        model.addAttribute("employee", employee);
        return "admin/employees/form";
    }

    // просмотр сотрудника
    @GetMapping("/{id}")
    public String viewEmployee(@PathVariable("id") Integer id, Model model) {
        Employee employee = employeeService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Неверный ID сотрудника: " + id));

        model.addAttribute("employee", employee);
        return "admin/employees/view";
    }

    // удаление сотрудника
    @PostMapping("/{id}/delete")
    public String deleteEmployee(@PathVariable("id") Integer id,
                                 RedirectAttributes redirectAttributes) {
        employeeService.delete(id);
        redirectAttributes.addFlashAttribute("success", "Сотрудник успешно удален!");
        return "redirect:/admin/employees";
    }

    // поиск сотрудников (только по ключевому слову - ФИО или табельный номер)
    @GetMapping("/search")
    public String searchEmployees(@RequestParam(value = "keyword", required = false) String keyword,
                                  Model model) {

        List<Employee> employees;

        if (keyword != null && !keyword.trim().isEmpty()) {
            // Универсальный поиск по ФИО или табельному номеру
            String searchTerm = keyword.trim();
            employees = employeeService.searchByNameOrPersonnelNumber(searchTerm);


            model.addAttribute("keyword", keyword);
        } else {
            employees = employeeService.findAll();
        }

        model.addAttribute("employees", employees);
        return "admin/employees/list";
    }

}