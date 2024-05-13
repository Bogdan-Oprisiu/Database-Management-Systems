package org.example.computer_storebe.repository.computer;

import org.example.computer_storebe.entity.OperatingSystem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OperatingSystemRepository extends JpaRepository<OperatingSystem, Long> {
}