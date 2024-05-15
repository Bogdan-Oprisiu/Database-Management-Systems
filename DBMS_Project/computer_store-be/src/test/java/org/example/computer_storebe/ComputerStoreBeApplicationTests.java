package org.example.computer_storebe;

import org.example.computer_storebe.entity.Computer;
import org.example.computer_storebe.entity.OperatingSystem;
import org.example.computer_storebe.repository.computer.ComputerRepository;
import org.example.computer_storebe.repository.computer.OperatingSystemRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ComputerStoreBeApplicationTests {

    @Autowired
    private ComputerRepository computerRepository;
    @Autowired
    private OperatingSystemRepository operatingSystemRepository;

    @Test
    @Transactional
    @Rollback
    void testConcurrentTransactions() throws InterruptedException {
        // Fetch the computer entity
        Computer computer = computerRepository.findById(1L).orElse(null);
        if (computer == null) {
            // Handle if computer with id 1 is not found
            return;
        }

        // Modify the price of the computer in two concurrent transactions
        Thread thread1 = new Thread(() -> {
            // Retrieve the computer entity in a new transaction
            Computer computer1 = computerRepository.findById(1L).orElse(null);
            if (computer1 != null) {
                // Update the price
                computer1.setBasePrice(computer1.getBasePrice() + 100);
                // Save the changes
                computerRepository.save(computer1);
            }
        });

        Thread thread2 = new Thread(() -> {
            // Retrieve the computer entity in a new transaction
            Computer computer2 = computerRepository.findById(1L).orElse(null);
            if (computer2 != null) {
                // Update the price
                computer2.setBasePrice(computer2.getBasePrice() + 200);
                // Save the changes
                computerRepository.save(computer2);
            }
        });

        // Start both threads
        thread1.start();
        thread2.start();

        // Wait for both threads to finish
        thread1.join();
        thread2.join();

        // Retrieve the computer entity after concurrent updates
        Computer updatedComputer = computerRepository.findById(1L).orElse(null);

        // Assert that the final price reflects the updates made by both transactions
        assert updatedComputer != null;
        assertEquals(computer.getBasePrice() + 100 + 200, updatedComputer.getBasePrice());
    }

    @Test
    @Transactional
    @Rollback
    void dirtyWriteConcurrencyIssue() throws InterruptedException {
        // Create a new operating system record
        OperatingSystem newOperatingSystem = new OperatingSystem();
        newOperatingSystem.setOsName("Windows");
        newOperatingSystem.setOsVersion("10");
        operatingSystemRepository.save(newOperatingSystem);

        // Fetch an operating system record to be updated by two transactions concurrently
        Optional<OperatingSystem> osOptional = operatingSystemRepository.findById(newOperatingSystem.getId());
        if (osOptional.isPresent()) {
            OperatingSystem os = osOptional.get();

            // Start the first transaction, updating the operating system's version
            Runnable transaction1 = () -> {
                System.out.println("Transaction 1 started.");
                // Fetch the entity again to make sure we have the latest version
                Optional<OperatingSystem> osToUpdateOptional = operatingSystemRepository.findById(newOperatingSystem.getId());
                if (osToUpdateOptional.isPresent()) {
                    OperatingSystem osToUpdate = osToUpdateOptional.get();
                    osToUpdate.setOsVersion("11.0");
                    operatingSystemRepository.save(osToUpdate); // This will update the existing entity
                    System.out.println("Transaction1: ");
                    operatingSystemRepository.findById(newOperatingSystem.getId()).ifPresent(System.out::println);
                }
                try {
                    Thread.sleep(5000);
                    System.out.println("Transaction1: ");
                    operatingSystemRepository.findById(newOperatingSystem.getId()).ifPresent(System.out::println);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("Transaction 1 finished.");
            };

            // Start the second transaction, updating the operating system's version with a different value
            Runnable transaction2 = () -> {
                System.out.println("Transaction 2 started.");
                // Introduce a delay to let the first transaction start and update the record
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                // Fetch the entity again to make sure we have the latest version
                Optional<OperatingSystem> osToUpdateOptional = operatingSystemRepository.findById(newOperatingSystem.getId());
                if (osToUpdateOptional.isPresent()) {
                    OperatingSystem osToUpdate = osToUpdateOptional.get();
                    osToUpdate.setOsVersion("12.0");
                    operatingSystemRepository.save(osToUpdate); // This will update the existing entity
                    System.out.println("Transaction2: ");
                    operatingSystemRepository.findById(newOperatingSystem.getId()).ifPresent(System.out::println);
                }
                System.out.println("Transaction 2 finished.");
            };

            // Execute both transactions concurrently
            Thread thread1 = new Thread(transaction1);
            Thread thread2 = new Thread(transaction2);

            thread1.start();
            thread2.start();

            // Wait for both threads to finish
            thread1.join();
            thread2.join();
        } else {
            throw new IllegalStateException("Operating system record with ID " + newOperatingSystem.getId() + " not found.");
        }
    }
}
