package com.pechenka.app.service;

import com.pechenka.app.entity.Warehouse;
import com.pechenka.app.repository.WarehouseRepository;
import com.pechenka.app.repository.StockRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class WarehouseService {

    private final WarehouseRepository warehouseRepository;
    private final StockRepository stockRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public WarehouseService(WarehouseRepository warehouseRepository, StockRepository stockRepository) {
        this.warehouseRepository = warehouseRepository;
        this.stockRepository = stockRepository;
    }

    public List<Warehouse> findAll() {
        return warehouseRepository.findAll();
    }

    public Optional<Warehouse> findById(Integer id) {
        return warehouseRepository.findById(id);
    }

    public Warehouse save(Warehouse warehouse) {
        return warehouseRepository.save(warehouse);
    }

    public List<Warehouse> findByAddressContaining(String keyword) {
        return warehouseRepository.findByAddressContaining(keyword);
    }

    public void delete(Integer id) {
        stockRepository.deleteAllByWarehouseId(id);
        entityManager.flush();
        entityManager.clear();
        warehouseRepository.deleteById(id);
    }

    public void updateWarehouseAddress(Integer id, String newAddress) {
        Warehouse warehouse = warehouseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Склад не найден"));

        warehouse.setAddress(newAddress);
        warehouseRepository.save(warehouse);
    }
}