package org.example.computer_storebe.controller;

import org.example.computer_storebe.entity.computer.Computer;
import org.example.computer_storebe.service.ComputerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/computers")
public class ComputerController {

    private final ComputerService computerService;

    @Autowired
    public ComputerController(ComputerService computerService) {
        this.computerService = computerService;
    }

    @GetMapping
    public List<Computer> getAllComputers() {
        return computerService.getAllComputers();
    }

    @GetMapping("/{id}")
    public Computer getComputerById(@PathVariable Long id) {
        return computerService.getComputerById(id);
    }

    @PostMapping
    public Computer addComputer(@RequestBody Computer computer) {
        return computerService.saveComputer(computer);
    }

    @PutMapping("/{id}")
    public Computer updateComputer(
            @PathVariable Long id,
            @RequestBody Computer updatedComputer
    ) {
        return computerService.updateComputer(id, updatedComputer);
    }


    @DeleteMapping("/{id}")
    public void deleteComputer(@PathVariable Long id) {
        computerService.deleteComputer(id);
    }
}
