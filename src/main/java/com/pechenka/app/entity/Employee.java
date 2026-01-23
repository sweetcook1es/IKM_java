package com.pechenka.app.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "employees")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "employee_id")
    private Integer id;

    @NotBlank(message = "Табельный номер обязателен")
    @Size(max = 20, message = "Табельный номер не должен превышать 20 символов")
    @Column(name = "personnel_number", unique = true, nullable = false)
    private String personnelNumber;

    @NotBlank(message = "ФИО обязательно")
    @Size(max = 50, message = "ФИО не должно превышать 50 символов")
    @Column(name = "full_name", nullable = false)
    private String fullName;

    @NotBlank(message = "Роль обязательна")
    @Pattern(regexp = "Администратор|Комплектовщик|Оператор|Аналитик",
            message = "Роль должна быть: Администратор, Комплектовщик, Оператор или Аналитик")
    @Column(name = "role", nullable = false)
    private String role;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WarehouseOperation> warehouseOperations = new ArrayList<>();

    public Employee() {}

    public Employee(String personnelNumber, String fullName, String role) {
        this.personnelNumber = personnelNumber;
        this.fullName = fullName;
        this.role = role;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getPersonnelNumber() { return personnelNumber; }
    public void setPersonnelNumber(String personnelNumber) { this.personnelNumber = personnelNumber; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public List<WarehouseOperation> getWarehouseOperations() { return warehouseOperations; }
    public void setWarehouseOperations(List<WarehouseOperation> warehouseOperations) {
        this.warehouseOperations = warehouseOperations;
    }

    public String getSurname() {
        if (fullName != null && !fullName.trim().isEmpty()) {
            String[] parts = fullName.trim().split("\\s+");
            return parts.length > 0 ? parts[0] : fullName;
        }
        return "";
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", personnelNumber='" + personnelNumber + '\'' +
                ", fullName='" + fullName + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}