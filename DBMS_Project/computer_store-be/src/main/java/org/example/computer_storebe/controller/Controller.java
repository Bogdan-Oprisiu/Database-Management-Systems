package org.example.computer_storebe.controller;

import lombok.val;
import org.example.computer_storebe.entity.User;
import org.example.computer_storebe.repository.UserRepository;
import org.example.computer_storebe.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/concurrency-issues-java")
public class Controller {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository users;

    @PostMapping("/transaction1-dirty-write")
    public ResponseEntity<String> transaction1_dirty_write(@RequestParam Long userId) throws InterruptedException {
        // Fetch data before the transaction
        Optional<User> userBefore = userService.fetchUser(userId);
        String before = "Before Transaction: " + (userBefore.isPresent() ? userBefore.get().toString() : "User not found");

        if(userBefore.isEmpty())
            return ResponseEntity.ok("User not found");

        // Perform the transaction and get data during the transaction
        User userDuring = userService.transaction1_dirty_write(userId);
        String during = "During Transaction: " + (userDuring != null ? userDuring.toString() : "User not found");

        // Wait to ensure Python side effects are visible and fetch data after the transaction
        Thread.sleep(5000); // Wait to ensure the Python script effects
        Optional<User> userAfter = userService.fetchUser(userId);
        String after = "After Transaction: " + (userAfter.isPresent() ? userAfter.get().toString() : "User not found");

        // Combine all into one response
        String response = "{ \"before\": \"" + before + "\", \"during\": \"" + during + "\", \"after\": \"" + after + "\" }";
        users.save(userBefore.get());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/transaction1-dirty-write-locking")
    public ResponseEntity<String> transaction1_dirty_write_locking(@RequestParam Long userId) throws InterruptedException {
        User result = userService.transaction1_dirty_write_locking(userId);
        return ResponseEntity.ok(result.toString());
    }

    @PostMapping("/transaction1-lost-update")
    public ResponseEntity<Map<String, String>> handleLostUpdate(@RequestParam Long userId) throws InterruptedException {
        Map<String, String> result = userService.transaction1_lost_update(userId);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/transaction1-lost-update-locking")
    public ResponseEntity<Map<String, String>> handleLostUpdate_locking(@RequestParam Long userId) throws InterruptedException {
        Map<String, String> result = userService.transaction1_lost_update_locking(userId);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/transaction1-unrepeatable-reads")
    public ResponseEntity<String> handleUnrepeatableReads(@RequestParam Long userId) throws InterruptedException {
        // Fetch data before the transaction
        Optional<User> userBefore = userService.fetchUser(userId);

        if(userBefore.isEmpty()) {
            return ResponseEntity.ok("User not found");
        }

        String before = "Before Transaction: " + (userBefore.isPresent() ? userBefore.get().toString() : "User not found");

        // Perform the transaction and get data during the transaction
        User userDuring = userService.transaction1_unrepeatable_reads(userId);
        String during = "During Transaction: " + (userDuring != null ? userDuring.toString() : "User not found");

        // Wait to ensure Python side effects are visible and fetch data after the transaction
        Thread.sleep(5000); // Wait to ensure the Python script effects
        Optional<User> userAfter = userService.fetchUser(userId);
        String after = "After Transaction: " + (userAfter.isPresent() ? userAfter.get().toString() : "User not found");
        var u = userBefore.get();
        u.setLastName("Doe");
        users.save(u);

        // Combine all into one response
        String response = "{ \"before\": \"" + before + "\", \"during\": \"" + during + "\", \"after\": \"" + after + "\" }";

        return ResponseEntity.ok(response);
    }

    @PostMapping("/transaction1-unrepeatable-reads-locking")
    public ResponseEntity<Map<String, String>> handleUnrepeatableReads_locking(@RequestParam Long userId) throws InterruptedException {
        Map<String, String> result = userService.transaction1_unrepeatable_reads_locking(userId);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/transaction1-dirty-read")
    public ResponseEntity<String> handleDirtyReads(@RequestParam Long userId) throws InterruptedException {
        // Fetch data before the transaction
        Optional<User> userBefore = userService.fetchUser(userId);

        if(userBefore.isEmpty()) {
            return ResponseEntity.ok("User not found");
        }
        var u = userBefore.get();
        u.setLastName("Doe");

        String before = "Before Transaction: " + (userBefore.isPresent() ? userBefore.get().toString() : "User not found");

        // Perform the transaction and get data during the transaction
        User userDuring = userService.transaction1_dirty_read(userId);
        String during = "During Transaction: " + (userDuring != null ? userDuring.toString() : "User not found");

        // Wait to ensure Python side effects are visible and fetch data after the transaction
        Thread.sleep(5000); // Wait to ensure the Python script effects
        Optional<User> userAfter = userService.fetchUser(userId);
        String after = "After Transaction: " + (userAfter.isPresent() ? userAfter.get().toString() : "User not found");
        users.save(u);

        // Combine all into one response
        String response = "{ \"before\": \"" + before + "\", \"during\": \"" + during + "\", \"after\": \"" + after + "\" }";

        return ResponseEntity.ok(response);
    }

    @PostMapping("/transaction1-dirty-read-locking")
    public ResponseEntity<Map<String, String>> handleDirtyRead_locking(@RequestParam Long userId) throws InterruptedException {
        Map<String, String> result = userService.transaction1_dirty_read_locking(userId);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/transaction1-phantom-read")
    public ResponseEntity<String> handlePhantomReads(@RequestParam Long userId) throws InterruptedException {
        // Fetch data before the transaction
        Optional<User> userBefore = userService.fetchUser(userId);

        if(userBefore.isEmpty()) {
            return ResponseEntity.ok("User not found");
        }
        var u = userBefore.get();
        u.setLastName("Doe");

        String before = "Before Transaction: " + (userBefore.isPresent() ? userBefore.get().toString() : "User not found");

        // Perform the transaction and get data during the transaction
        User userDuring = userService.transaction1_phantom_read(userId);
        String during = "During Transaction: " + (userDuring != null ? userDuring.toString() : "User not found");

        // Wait to ensure Python side effects are visible and fetch data after the transaction
        Thread.sleep(5000); // Wait to ensure the Python script effects
        Optional<User> userAfter = userService.fetchUser(userId);
        String after = "After Transaction: " + (userAfter.isPresent() ? userAfter.get().toString() : "User not found");
        users.save(u);

        // Combine all into one response
        String response = "{ \"before\": \"" + before + "\", \"during\": \"" + during + "\", \"after\": \"" + after + "\" }";

        return ResponseEntity.ok(response);
    }

    @PostMapping("/transaction1-phantom-read-locking")
    public ResponseEntity<Map<String, String>> handlePhantomRead_locking(@RequestParam Long userId) throws InterruptedException {
        Map<String, String> result = userService.transaction1_phantom_read_locking(userId);
        return ResponseEntity.ok(result);
    }
}
