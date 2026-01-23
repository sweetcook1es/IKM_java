package com.pechenka.app.repository;

import com.pechenka.app.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderStatusRepository extends JpaRepository<OrderStatus, Integer> {

    // найти по имени
    Optional<OrderStatus> findByName(String name);
}