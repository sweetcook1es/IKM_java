package com.pechenka.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import com.pechenka.app.repository.OrderRepository;
import com.pechenka.app.repository.ProductRepository;
import com.pechenka.app.repository.CustomerRepository;
import com.pechenka.app.repository.EmployeeRepository; // Изменено с UserRepository
import com.pechenka.app.entity.Employee; // Изменено с User
import com.pechenka.app.entity.Order;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class DashboardController {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        try {
            // получаем данные из БД
            long totalOrders = orderRepository.count();
            long totalProducts = productRepository.count();
            long totalCustomers = customerRepository.count();

            // получаем текущего аутентифицированного пользователя
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = auth.getName(); // табельный номер

            // ищем сотрудника в БД по табельному номеру
            Employee currentEmployee = null;
            try {
                currentEmployee = employeeRepository.findByPersonnelNumber(username).orElse(null);
            } catch (Exception e) {
                e.printStackTrace();
            }

            // добавляем ФИО сотрудника в модель
            if (currentEmployee != null && currentEmployee.getFullName() != null) {
                model.addAttribute("userFullName", currentEmployee.getFullName());
            } else {
                // если не нашли сотрудника, используем логин
                model.addAttribute("userFullName", username);
            }

            // выручка - используем BigDecimal
            BigDecimal totalRevenue = BigDecimal.ZERO;
            try {
                totalRevenue = orderRepository.getTotalRevenue();
                if (totalRevenue == null) {
                    totalRevenue = BigDecimal.ZERO;
                }
            } catch (Exception e) {
                // или посчитаем вручную
                List<Order> allOrders = orderRepository.findAll();
                double sum = allOrders.stream()
                        .mapToDouble(order ->
                                order.getTotalAmount() != null ?
                                        order.getTotalAmount().doubleValue() : 0.0)
                        .sum();
                totalRevenue = BigDecimal.valueOf(sum);
            }

            // передаем данные в модель
            model.addAttribute("totalOrders", totalOrders);
            model.addAttribute("totalProducts", totalProducts);
            model.addAttribute("totalCustomers", totalCustomers);
            model.addAttribute("totalRevenue", totalRevenue);

        } catch (Exception e) {
            // если что-то пошло не так, используем демо данные
            model.addAttribute("totalOrders", 5L);
            model.addAttribute("totalProducts", 7L);
            model.addAttribute("totalCustomers", 4L);
            model.addAttribute("totalRevenue", BigDecimal.valueOf(2560.0));
            model.addAttribute("userFullName", "Иванов Иван Иванович");

            e.printStackTrace();
        }

        return "admin/dashboard";
    }
}