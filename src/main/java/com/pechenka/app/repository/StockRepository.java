package com.pechenka.app.repository;

import com.pechenka.app.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.Modifying;

import java.util.List;
import java.util.Optional;

@Repository
public interface StockRepository extends JpaRepository<Stock, Integer> {

    // все остатки с полной информацией о товарах и складах
    @Query("SELECT s FROM Stock s LEFT JOIN FETCH s.product LEFT JOIN FETCH s.warehouse ORDER BY s.id")
    List<Stock> findAllWithDetails();

    // конкретный остаток по ID с детальной информацией
    @Query("SELECT s FROM Stock s LEFT JOIN FETCH s.product LEFT JOIN FETCH s.warehouse WHERE s.id = :id")
    Optional<Stock> findByIdWithDetails(@Param("id") Integer id);

    // остаток конкретного товара на конкретном складе
    Optional<Stock> findByProductIdAndWarehouseId(Integer productId, Integer warehouseId);

    // все остатки определенного товара на всех складах
    List<Stock> findByProductId(Integer productId);

    // все остатки на определенном складе
    List<Stock> findByWarehouseId(Integer warehouseId);

    // товары с низким запасом
    @Query("SELECT s FROM Stock s WHERE s.quantity < :threshold")
    List<Stock> findLowStock(@Param("threshold") Integer threshold);

    // удалить все остатки на складе
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM stock WHERE warehouse_id = :warehouseId", nativeQuery = true)
    void deleteAllByWarehouseId(@Param("warehouseId") Integer warehouseId);
}
