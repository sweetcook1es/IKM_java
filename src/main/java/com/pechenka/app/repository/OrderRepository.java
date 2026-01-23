package com.pechenka.app.repository;

import com.pechenka.app.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

    // найти заказы клиента
    List<Order> findByCustomerId(Integer customerId);

    // найти заказы по статусу
    List<Order> findByStatusId(Integer statusId);

    // получить заказ с полными деталями
    @Query("SELECT o FROM Order o " +
            "JOIN FETCH o.customer " +
            "JOIN FETCH o.status " +
            "LEFT JOIN FETCH o.orderItems oi " +
            "LEFT JOIN FETCH oi.product " +
            "WHERE o.id = :id")
    Optional<Order> findByIdWithFullDetails(@Param("id") Integer id);

    // общая выручка
    @Query("SELECT COALESCE(SUM(oi.priceAtOrder * oi.quantity), 0) " +
            "FROM Order o JOIN o.orderItems oi")
    BigDecimal getTotalRevenue();
}
