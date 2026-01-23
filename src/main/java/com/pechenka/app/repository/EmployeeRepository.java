package com.pechenka.app.repository;

import com.pechenka.app.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

    // найти по табельному номеру
    Optional<Employee> findByPersonnelNumber(String personnelNumber);

    // универсальный поиск по ФИО ИЛИ табельному номеру
    @Query("SELECT e FROM Employee e WHERE " +
            "LOWER(e.fullName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "e.personnelNumber LIKE CONCAT('%', :keyword, '%')")
    List<Employee> searchByNameOrPersonnelNumber(@Param("keyword") String keyword);
}
