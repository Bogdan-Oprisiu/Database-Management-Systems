package org.example.computer_storebe.controller;

import org.example.computer_storebe.entity.computer.ComputerManufacturer;
import org.example.computer_storebe.service.ComputerManufacturerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/manufacturer")
public class ComputerManufacturerController {

    private final ComputerManufacturerService manufacturerService;

    @Autowired
    public ComputerManufacturerController(ComputerManufacturerService manufacturerService) {
        this.manufacturerService = manufacturerService;
    }

    @GetMapping
    public List<ComputerManufacturer> getAllManufacturers() {
        return manufacturerService.getAllManufacturers();
    }

    @GetMapping("/{id}")
    public ComputerManufacturer getManufacturerById(@PathVariable Long id) {
        return manufacturerService.getManufacturerById(id);
    }

    @PostMapping
    public ComputerManufacturer addManufacturer(@RequestBody ComputerManufacturer manufacturer) {
        return manufacturerService.saveManufacturer(manufacturer);
    }

    @PutMapping("/{id}")
    public ComputerManufacturer updateManufacturer(@PathVariable Long id, @RequestBody ComputerManufacturer manufacturer) {
        manufacturer.setId(id);
        return manufacturerService.saveManufacturer(manufacturer);
    }

    @DeleteMapping("/{id}")
    public void deleteManufacturer(@PathVariable Long id) {
        manufacturerService.deleteManufacturer(id);
    }
}
