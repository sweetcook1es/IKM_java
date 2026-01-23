package com.pechenka.app.controller;

import com.pechenka.app.entity.WarehouseOperation;
import com.pechenka.app.service.WarehouseOperationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/warehouse-operations")
public class WarehouseOperationController {

    private final WarehouseOperationService warehouseOperationService;

    public WarehouseOperationController(WarehouseOperationService warehouseOperationService) {
        this.warehouseOperationService = warehouseOperationService;
    }

    // список всех операций
    @GetMapping
    public String listOperations(Model model) {
        List<WarehouseOperation> operations = warehouseOperationService.findAll();
        model.addAttribute("operations", operations);
        return "admin/warehouse-operations/list";
    }
}