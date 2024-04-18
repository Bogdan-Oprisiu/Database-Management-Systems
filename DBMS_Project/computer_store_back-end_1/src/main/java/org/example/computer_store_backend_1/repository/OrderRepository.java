package org.example.computer_store_backend_1.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.example.computer_store_backend_1.entity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
}

