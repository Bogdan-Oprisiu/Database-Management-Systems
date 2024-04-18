package org.example.computer_store_backend_1.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

import java.time.LocalDate;

@Entity
public class Order {
    @Id
    private Long id;

    private LocalDate orderDate;
    private String paymentMethod;

    @ManyToOne
    private Customer customer;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
