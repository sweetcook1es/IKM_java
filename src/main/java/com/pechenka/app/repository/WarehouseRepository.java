package com.pechenka.app.repository;

import com.pechenka.app.entity.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.Modifying;

import java.util.List;

@Repository
public interface WarehouseRepository extends JpaRepository<Warehouse, Integer> {

    // найти по части адреса
    @Query("SELECT w FROM Warehouse w WHERE LOWER(w.address) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Warehouse> findByAddressContaining(@Param("keyword") String keyword);

    // удаления с очисткой зависимостей
    @Modifying
    @Transactional
    @Query("DELETE FROM Warehouse w WHERE w.id = :id AND NOT EXISTS " +
            "(SELECT 1 FROM Stock s WHERE s.warehouse.id = :id AND s.quantity > 0)")
    int deleteIfEmpty(@Param("id") Integer id);

    // принудительное удаление
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM warehouses WHERE warehouse_id = :id", nativeQuery = true)
    void forceDelete(@Param("id") Integer id);
}
