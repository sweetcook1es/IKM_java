package com.pechenka.app.controller;

import com.pechenka.app.entity.OrderStatus;
import com.pechenka.app.service.OrderStatusService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@Controller
@RequestMapping("/admin/order-statuses")
public class OrderStatusController {

    private final OrderStatusService orderStatusService;

    public OrderStatusController(OrderStatusService orderStatusService) {
        this.orderStatusService = orderStatusService;
    }

    // список всех статусов
    @GetMapping
    public String listOrderStatuses(Model model) {
        List<OrderStatus> statuses = orderStatusService.findAll();
        model.addAttribute("statuses", statuses);
        return "admin/order-statuses/list";
    }
}