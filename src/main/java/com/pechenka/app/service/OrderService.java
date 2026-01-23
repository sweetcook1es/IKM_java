package com.pechenka.app.service;

import com.pechenka.app.entity.Order;
import com.pechenka.app.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderStatusService orderStatusService;

    public OrderService(OrderRepository orderRepository, OrderStatusService orderStatusService) {
        this.orderRepository = orderRepository;
        this.orderStatusService = orderStatusService;
    }

    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    public Optional<Order> findById(Integer id) {
        return orderRepository.findById(id);
    }

    public Optional<Order> findByIdWithDetails(Integer id) {
        return orderRepository.findByIdWithFullDetails(id);
    }

    public Order save(Order order) {
        return orderRepository.save(order);
    }

    public void delete(Integer id) {
        orderRepository.deleteById(id);
    }

    public List<Order> findByCustomerId(Integer customerId) {
        return orderRepository.findByCustomerId(customerId);
    }

    public List<Order> findByStatusId(Integer statusId) {
        return orderRepository.findByStatusId(statusId);
    }

    public void updateStatus(Integer orderId, Integer statusId) {
        Optional<Order> orderOpt = orderRepository.findById(orderId);
        if (orderOpt.isPresent()) {
            Order order = orderOpt.get();
            orderStatusService.findById(statusId).ifPresent(order::setStatus);
            orderRepository.save(order);
        }
    }

}