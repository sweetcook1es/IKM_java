package com.pechenka.app.service;

import com.pechenka.app.entity.OrderStatus;
import com.pechenka.app.repository.OrderStatusRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class OrderStatusService {

    private final OrderStatusRepository orderStatusRepository;

    public OrderStatusService(OrderStatusRepository orderStatusRepository) {
        this.orderStatusRepository = orderStatusRepository;
    }

    public List<OrderStatus> findAll() {
        return orderStatusRepository.findAll();
    }

    public Optional<OrderStatus> findById(Integer id) {
        return orderStatusRepository.findById(id);
    }

    public Optional<OrderStatus> findByName(String name) {
        return orderStatusRepository.findByName(name);
    }

    public OrderStatus save(OrderStatus orderStatus) {
        return orderStatusRepository.save(orderStatus);
    }

    public void delete(Integer id) {
        orderStatusRepository.deleteById(id);
    }
}