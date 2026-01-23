package com.pechenka.app.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "warehouse_operations")
public class WarehouseOperation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "operation_id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Column(name = "operation_date", nullable = false)
    private LocalDateTime operationDate;

    public WarehouseOperation() {
        this.operationDate = LocalDateTime.now();
    }

    public WarehouseOperation(Order order, Employee employee) {
        this.operationDate = LocalDateTime.now();
        this.order = order;
        this.employee = employee;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Order getOrder() { return order; }
    public void setOrder(Order order) { this.order = order; }

    public Employee getEmployee() { return employee; }
    public void setEmployee(Employee employee) { this.employee = employee; }

    public LocalDateTime getOperationDate() { return operationDate; }
    public void setOperationDate(LocalDateTime operationDate) { this.operationDate = operationDate; }

    @Override
    public String toString() {
        return "WarehouseOperation{" +
                "id=" + id +
                ", order=" + (order != null ? order.getId() : "null") +
                ", employee=" + (employee != null ? employee.getFullName() : "null") +
                ", operationDate=" + operationDate +
                '}';
    }
}