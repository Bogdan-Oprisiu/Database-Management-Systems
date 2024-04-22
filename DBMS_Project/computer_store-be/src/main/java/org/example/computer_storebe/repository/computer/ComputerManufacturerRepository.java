package org.example.computer_storebe.repository.computer;

import org.example.computer_storebe.entity.computer.ComputerManufacturer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComputerManufacturerRepository extends JpaRepository<ComputerManufacturer, Long> {
}
