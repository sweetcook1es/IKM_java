package com.pechenka.app.repository;

import com.pechenka.app.entity.WarehouseOperation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface WarehouseOperationRepository extends JpaRepository<WarehouseOperation, Integer> {

    // найти операции по заказу
    List<WarehouseOperation> findByOrderId(Integer orderId);

    // найти операции по сотруднику
    List<WarehouseOperation> findByEmployeeId(Integer employeeId);

    // найти операции по дате
    @Query("SELECT wo FROM WarehouseOperation wo WHERE wo.operationDate BETWEEN :startDate AND :endDate")
    List<WarehouseOperation> findByDateRange(@Param("startDate") LocalDateTime startDate,
                                             @Param("endDate") LocalDateTime endDate);
}