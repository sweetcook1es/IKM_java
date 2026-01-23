package com.pechenka.app.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@NamedEntityGraph(
        name = "Order.withCustomerAndStatus",
        attributeNodes = {
                @NamedAttributeNode("customer"),
                @NamedAttributeNode("status"),
                @NamedAttributeNode(value = "orderItems", subgraph = "orderItems-subgraph")
        },
        subgraphs = {
                @NamedSubgraph(
                        name = "orderItems-subgraph",
                        attributeNodes = {
                                @NamedAttributeNode("product")
                        }
                )
        }
)
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Integer id;

    @Column(name = "creation_date", nullable = false)
    private LocalDateTime creationDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "status_id", nullable = false)
    private OrderStatus status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<WarehouseOperation> warehouseOperations = new ArrayList<>();

    public Order() {
        this.creationDate = LocalDateTime.now();
    }

    public Order(OrderStatus status, Customer customer) {
        this.creationDate = LocalDateTime.now();
        this.status = status;
        this.customer = customer;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public LocalDateTime getCreationDate() { return creationDate; }
    public void setCreationDate(LocalDateTime creationDate) { this.creationDate = creationDate; }

    public OrderStatus getStatus() { return status; }
    public void setStatus(OrderStatus status) { this.status = status; }

    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }

    public List<OrderItem> getOrderItems() { return orderItems; }
    public void setOrderItems(List<OrderItem> orderItems) { this.orderItems = orderItems; }

    public List<WarehouseOperation> getWarehouseOperations() { return warehouseOperations; }
    public void setWarehouseOperations(List<WarehouseOperation> warehouseOperations) {
        this.warehouseOperations = warehouseOperations;
    }

    public BigDecimal getTotalAmount() {
        BigDecimal total = BigDecimal.ZERO;
        if (orderItems != null) {
            for (OrderItem item : orderItems) {
                total = total.add(item.getPriceAtOrder().multiply(BigDecimal.valueOf(item.getQuantity())));
            }
        }
        return total;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", creationDate=" + creationDate +
                ", status=" + (status != null ? status.getName() : "null") +
                ", customer=" + (customer != null ? customer.getFullName() : "null") +
                '}';
    }
}