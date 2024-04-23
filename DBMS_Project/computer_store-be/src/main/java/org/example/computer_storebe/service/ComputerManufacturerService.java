package org.example.computer_storebe.service;

import org.example.computer_storebe.entity.computer.ComputerManufacturer;
import org.example.computer_storebe.repository.computer.ComputerManufacturerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ComputerManufacturerService {

    private final ComputerManufacturerRepository manufacturerRepository;

    @Autowired
    public ComputerManufacturerService(ComputerManufacturerRepository manufacturerRepository) {
        this.manufacturerRepository = manufacturerRepository;
    }

    public List<ComputerManufacturer> getAllManufacturers() {
        return manufacturerRepository.findAll();
    }

    public ComputerManufacturer getManufacturerById(Long id) {
        return manufacturerRepository.findById(id).orElse(null);
    }

    public ComputerManufacturer saveManufacturer(ComputerManufacturer manufacturer) {
        return manufacturerRepository.save(manufacturer);
    }

    public void deleteManufacturer(Long id) {
        manufacturerRepository.deleteById(id);
    }

    public ComputerManufacturer updateManufacturer(Long id, ComputerManufacturer updatedManufacturer) {
        Optional<ComputerManufacturer> optionalManufacturer = manufacturerRepository.findById(id);
        if (optionalManufacturer.isPresent()) {
            ComputerManufacturer existingManufacturer = optionalManufacturer.get();
            existingManufacturer.setManufacturerName(updatedManufacturer.getManufacturerName());
            existingManufacturer.setManufacturerPhone(updatedManufacturer.getManufacturerPhone());
            existingManufacturer.setManufacturerEmail(updatedManufacturer.getManufacturerEmail());
            // Update any other properties as needed
            return manufacturerRepository.save(existingManufacturer);
        }
        return null; // Return null if the manufacturer with the given id doesn't exist
    }
}
