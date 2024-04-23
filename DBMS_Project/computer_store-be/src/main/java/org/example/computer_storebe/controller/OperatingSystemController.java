package org.example.computer_storebe.controller;

import org.example.computer_storebe.entity.computer.OperatingSystem;
import org.example.computer_storebe.service.OperatingSystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/os")
public class OperatingSystemController {

    private final OperatingSystemService operatingSystemService;

    @Autowired
    public OperatingSystemController(OperatingSystemService operatingSystemService) {
        this.operatingSystemService = operatingSystemService;
    }

    @GetMapping
    public List<OperatingSystem> getAllOperatingSystems() {
        return operatingSystemService.getAllOperatingSystems();
    }

    @GetMapping("/{id}")
    public OperatingSystem getOperatingSystemById(@PathVariable Long id) {
        return operatingSystemService.getOperatingSystemById(id);
    }

    @PostMapping
    public OperatingSystem addOperatingSystem(@RequestBody OperatingSystem operatingSystem) {
        return operatingSystemService.saveOperatingSystem(operatingSystem);
    }

    @PutMapping("/{id}")
    public OperatingSystem updateOperatingSystem(
            @PathVariable Long id,
            @RequestBody OperatingSystem updatedOperatingSystem
    ) {
        return operatingSystemService.updateOperatingSystem(id, updatedOperatingSystem);
    }

    @DeleteMapping("/{id}")
    public void deleteOperatingSystem(@PathVariable Long id) {
        operatingSystemService.deleteOperatingSystem(id);
    }
}
