//package org.example.computer_storebe.service;
//
//import org.example.computer_storebe.entity.computer.OperatingSystem;
//import org.example.computer_storebe.repository.computer.OperatingSystemRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import java.util.List;
//
//@Service
//public class OperatingSystemService {
//
//    private final OperatingSystemRepository operatingSystemRepository;
//
//    @Autowired
//    public OperatingSystemService(OperatingSystemRepository operatingSystemRepository) {
//        this.operatingSystemRepository = operatingSystemRepository;
//    }
//
//    public List<OperatingSystem> getAllOperatingSystems() {
//        return operatingSystemRepository.findAll();
//    }
//
//    public OperatingSystem getOperatingSystemById(Long id) {
//        return operatingSystemRepository.findById(id).orElse(null);
//    }
//
//    public OperatingSystem saveOperatingSystem(OperatingSystem operatingSystem) {
//        return operatingSystemRepository.save(operatingSystem);
//    }
//
//    public void deleteOperatingSystem(Long id) {
//        operatingSystemRepository.deleteById(id);
//    }
//
//    public OperatingSystem updateOperatingSystem(Long id, OperatingSystem updatedOperatingSystem) {
//        OperatingSystem existingOperatingSystem = operatingSystemRepository.findById(id).orElse(null);
//        if (existingOperatingSystem != null) {
//            // Update the existing operating system with the new data
//            existingOperatingSystem.setOsName(updatedOperatingSystem.getOsName());
//            existingOperatingSystem.setOsVersion(updatedOperatingSystem.getOsVersion());
//            // Save and return the updated operating system
//            return operatingSystemRepository.save(existingOperatingSystem);
//        }
//        return null; // Return null if the operating system with the given id doesn't exist
//    }
//}
