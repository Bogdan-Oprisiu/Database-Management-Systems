package org.example.computer_storebe.repository.computer;

import org.example.computer_storebe.entity.Computer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComputerRepository extends JpaRepository<Computer, Long> {
}