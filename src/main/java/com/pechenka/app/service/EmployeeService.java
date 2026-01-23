package com.pechenka.app.service;

import com.pechenka.app.entity.Employee;
import com.pechenka.app.repository.EmployeeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }

    public Optional<Employee> findById(Integer id) {
        return employeeRepository.findById(id);
    }

    public Employee save(Employee employee) {
        return employeeRepository.save(employee);
    }

    public void delete(Integer id) {
        employeeRepository.deleteById(id);
    }

    public List<Employee> searchByNameOrPersonnelNumber(String keyword) {
        return employeeRepository.searchByNameOrPersonnelNumber(keyword);
    }

    public Optional<Employee> findByPersonnelNumber(String personnelNumber) {
        return employeeRepository.findByPersonnelNumber(personnelNumber);
    }
}