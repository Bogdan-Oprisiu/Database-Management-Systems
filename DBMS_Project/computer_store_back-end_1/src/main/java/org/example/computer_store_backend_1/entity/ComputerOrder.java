package org.example.computer_store_backend_1.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

import java.time.LocalDate;

@Entity
public class ComputerOrder {
    @Id
    private Long id;

    private LocalDate orderDate;
    private String paymentMethod;

    public ComputerOrder() {
    }

    public ComputerOrder(Long id, LocalDate orderDate, String paymentMethod, Customer customer) {
        this.id = id;
        this.orderDate = orderDate;
        this.paymentMethod = paymentMethod;
        this.customer = customer;
    }

    @ManyToOne
    private Customer customer;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}
