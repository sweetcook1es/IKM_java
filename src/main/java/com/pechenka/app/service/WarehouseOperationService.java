package com.pechenka.app.service;

import com.pechenka.app.entity.WarehouseOperation;
import com.pechenka.app.repository.WarehouseOperationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class WarehouseOperationService {

    private final WarehouseOperationRepository warehouseOperationRepository;

    public WarehouseOperationService(WarehouseOperationRepository warehouseOperationRepository) {
        this.warehouseOperationRepository = warehouseOperationRepository;
    }

    public List<WarehouseOperation> findAll() {
        return warehouseOperationRepository.findAll();
    }

    public Optional<WarehouseOperation> findById(Integer id) {
        return warehouseOperationRepository.findById(id);
    }

    public List<WarehouseOperation> findByOrderId(Integer orderId) {
        return warehouseOperationRepository.findByOrderId(orderId);
    }

    public List<WarehouseOperation> findByEmployeeId(Integer employeeId) {
        return warehouseOperationRepository.findByEmployeeId(employeeId);
    }

    public List<WarehouseOperation> findByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return warehouseOperationRepository.findByDateRange(startDate, endDate);
    }

    public WarehouseOperation save(WarehouseOperation operation) {
        return warehouseOperationRepository.save(operation);
    }

    public void delete(Integer id) {
        warehouseOperationRepository.deleteById(id);
    }

    public List<WarehouseOperation> findTodayOperations() {
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDate.now().plusDays(1).atStartOfDay();
        return warehouseOperationRepository.findByDateRange(startOfDay, endOfDay);
    }
}