package org.example.computer_storebe.service;

import org.example.computer_storebe.entity.computer.Computer;
import org.example.computer_storebe.repository.computer.ComputerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ComputerService {

    private final ComputerRepository computerRepository;

    @Autowired
    public ComputerService(ComputerRepository computerRepository) {
        this.computerRepository = computerRepository;
    }

    public List<Computer> getAllComputers() {
        return computerRepository.findAll();
    }

    public Computer getComputerById(Long id) {
        return computerRepository.findById(id).orElse(null);
    }

    public Computer saveComputer(Computer computer) {
        return computerRepository.save(computer);
    }

    public void deleteComputer(Long id) {
        computerRepository.deleteById(id);
    }

    public Computer updateComputer(Long id, Computer updatedComputer) {
        Optional<Computer> optionalComputer = computerRepository.findById(id);
        if (optionalComputer.isPresent()) {
            Computer existingComputer = optionalComputer.get();
            existingComputer.setModelName(updatedComputer.getModelName());
            existingComputer.setBasePrice(updatedComputer.getBasePrice());
            existingComputer.setManufacturer(updatedComputer.getManufacturer());
            existingComputer.setCategory(updatedComputer.getCategory());
            existingComputer.setOperatingSystem(updatedComputer.getOperatingSystem());
            // Update any other properties as needed
            return computerRepository.save(existingComputer);
        }
        return null; // Return null if the computer with the given id doesn't exist
    }
}
