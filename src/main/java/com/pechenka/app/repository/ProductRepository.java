package com.pechenka.app.repository;

import com.pechenka.app.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    // найти по категории
    List<Product> findByCategoryId(Integer categoryId);

    // поиск по названию или описанию
    @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Product> searchByNameOrDescription(@Param("keyword") String keyword);

    // сортировка по названию
    @Query("SELECT p FROM Product p ORDER BY p.name ASC")
    List<Product> findAllSortedByName();

    // получить товары с деталями
    @Query("SELECT p FROM Product p JOIN FETCH p.category LEFT JOIN FETCH p.stock")
    List<Product> findAllWithDetails();
}
