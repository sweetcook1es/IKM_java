package com.pechenka.app.controller;

import com.pechenka.app.entity.Order;
import com.pechenka.app.service.OrderService;
import com.pechenka.app.service.OrderStatusService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/orders")
public class OrderController {

    private final OrderService orderService;
    private final OrderStatusService orderStatusService;

    public OrderController(OrderService orderService,
                           OrderStatusService orderStatusService) {
        this.orderService = orderService;
        this.orderStatusService = orderStatusService;
    }

    // список всех заказов
    @GetMapping
    public String listOrders(Model model) {
        List<Order> orders = orderService.findAll();
        model.addAttribute("orders", orders);
        model.addAttribute("statuses", orderStatusService.findAll());
        return "admin/orders/list";
    }

    // просмотр заказа
    @GetMapping("/{id}")
    public String viewOrder(@PathVariable("id") Integer id, Model model) {
        Optional<Order> orderOpt = orderService.findByIdWithDetails(id);
        if (orderOpt.isEmpty()) {
            orderOpt = orderService.findById(id);
        }

        Order order = orderOpt.orElseThrow(() -> new IllegalArgumentException("Неверный ID заказа: " + id));

        model.addAttribute("order", order);
        model.addAttribute("statuses", orderStatusService.findAll());
        return "admin/orders/view";
    }

    // изменение статуса заказа
    @PostMapping("/{id}/update-status")
    public String updateOrderStatus(@PathVariable("id") Integer id,
                                    @RequestParam("statusId") Integer statusId,
                                    RedirectAttributes redirectAttributes) {
        orderService.updateStatus(id, statusId);
        redirectAttributes.addFlashAttribute("success", "Статус заказа обновлен!");
        return "redirect:/admin/orders/" + id;
    }

    // удаление заказа
    @PostMapping("/{id}/delete")
    public String deleteOrder(@PathVariable("id") Integer id,
                              RedirectAttributes redirectAttributes) {
        orderService.delete(id);
        redirectAttributes.addFlashAttribute("success", "Заказ успешно удален!");
        return "redirect:/admin/orders";
    }

    // заказы клиента
    @GetMapping("/customer/{customerId}")
    public String ordersByCustomer(@PathVariable("customerId") Integer customerId, Model model) {
        List<Order> orders = orderService.findByCustomerId(customerId);
        model.addAttribute("orders", orders);
        model.addAttribute("title", "Заказы клиента #" + customerId);
        model.addAttribute("statuses", orderStatusService.findAll());
        return "admin/orders/list";
    }

    // заказы по статусу
    @GetMapping("/status/{statusId}")
    public String ordersByStatus(@PathVariable("statusId") Integer statusId, Model model) {
        List<Order> orders = orderService.findByStatusId(statusId);
        model.addAttribute("orders", orders);
        model.addAttribute("title", "Заказы по статусу");
        model.addAttribute("statuses", orderStatusService.findAll());
        return "admin/orders/list";
    }
}