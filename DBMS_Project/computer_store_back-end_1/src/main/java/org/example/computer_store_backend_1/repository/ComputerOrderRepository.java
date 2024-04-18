package org.example.computer_store_backend_1.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.example.computer_store_backend_1.entity.ComputerOrder;

@Repository
public interface ComputerOrderRepository extends JpaRepository<ComputerOrder, Long> {
}

