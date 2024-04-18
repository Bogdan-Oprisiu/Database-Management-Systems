package org.example.computer_store_backend_1.service;

import org.example.computer_store_backend_1.entity.ComputerOrder;
import org.example.computer_store_backend_1.repository.ComputerOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ComputerOrderService {
    @Autowired
    private ComputerOrderRepository computerOrderRepository;

    public List<ComputerOrder> getAllOrders() {
        return computerOrderRepository.findAll();
    }

    public Optional<ComputerOrder> getOrderById(Long id) {
        return computerOrderRepository.findById(id);
    }

    public ComputerOrder saveOrder(ComputerOrder computerOrder) {
        return computerOrderRepository.save(computerOrder);
    }

    public void deleteOrder(Long id) {
        computerOrderRepository.deleteById(id);
    }
}

