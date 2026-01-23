package com.pechenka.app.service;

import com.pechenka.app.entity.Stock;
import com.pechenka.app.repository.StockRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class StockService {

    private final StockRepository stockRepository;

    public StockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    public List<Stock> findAll() {
        return stockRepository.findAllWithDetails();
    }

    public Optional<Stock> findById(Integer id) {
        return stockRepository.findByIdWithDetails(id);
    }

    public Stock save(Stock stock) {
        return stockRepository.save(stock);
    }

    public void delete(Integer id) {
        stockRepository.deleteById(id);
    }

    public Optional<Stock> findByProductAndWarehouse(Integer productId, Integer warehouseId) {
        return stockRepository.findByProductIdAndWarehouseId(productId, warehouseId);
    }

    public List<Stock> findByProductId(Integer productId) {
        return stockRepository.findByProductId(productId);
    }

    public List<Stock> findByWarehouseId(Integer warehouseId) {
        return stockRepository.findByWarehouseId(warehouseId);
    }

    public List<Stock> findLowStockItems(Integer threshold) {
        return stockRepository.findLowStock(threshold);
    }

    public void updateQuantity(Integer id, Integer quantity) {
        stockRepository.findById(id).ifPresent(stock -> {
            stock.setQuantity(quantity);
            stockRepository.save(stock);
        });
    }
}