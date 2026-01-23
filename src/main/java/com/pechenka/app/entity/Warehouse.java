package com.pechenka.app.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "warehouses")
public class Warehouse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "warehouse_id")
    private Integer id;

    @NotBlank(message = "Адрес склада обязателен")
    @Size(max = 200, message = "Адрес не должен превышать 200 символов")
    @Column(name = "address", unique = true)
    private String address;

    @OneToMany(mappedBy = "warehouse", fetch = FetchType.LAZY)
    private List<Stock> stock = new ArrayList<>();

    public Warehouse() {}

    public Warehouse(String address) {
        this.address = address;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public List<Stock> getStock() { return stock; }
    public void setStock(List<Stock> stock) { this.stock = stock; }

    public void addStock(Stock stockItem) {
        stock.add(stockItem);
        stockItem.setWarehouse(this);
    }

    public void removeStock(Stock stockItem) {
        stock.remove(stockItem);
        stockItem.setWarehouse(null);
    }

    @Override
    public String toString() {
        return "Warehouse{" +
                "id=" + id +
                ", address='" + address + '\'' +
                '}';
    }
}