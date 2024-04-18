package org.example.computer_store_backend_1.controller;

import org.example.computer_store_backend_1.entity.ComputerOrder;
import org.example.computer_store_backend_1.service.ComputerOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/orders")
public class ComputerOrderController {
    @Autowired
    private ComputerOrderService computerOrderService;

    @GetMapping
    public List<ComputerOrder> getAllOrders() {
        return computerOrderService.getAllOrders();
    }

    @GetMapping("/{id}")
    public Optional<ComputerOrder> getOrderById(@PathVariable Long id) {
        return computerOrderService.getOrderById(id);
    }

    @PostMapping
    public ComputerOrder createOrder(@RequestBody ComputerOrder computerOrder) {
        return computerOrderService.saveOrder(computerOrder);
    }

    @DeleteMapping("/{id}")
    public void deleteOrder(@PathVariable Long id) {
        computerOrderService.deleteOrder(id);
    }
}
