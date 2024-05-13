package org.example.computer_storebe.controller;

import org.example.computer_storebe.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/concurrency-issues-java")
public class Controller {

    @Autowired
    private UserService userService;

//    @Autowired
//    private WorkoutService workoutService;
//
//    @Autowired
//    private ExerciseService exerciseService;
//
    @PostMapping("/transaction1-dirty-write")
    public ResponseEntity<String> transaction1_dirty_write(@RequestParam Long userId) throws InterruptedException {
        userService.transaction1_dirty_write(userId);
        return ResponseEntity.ok("Transaction 1 completed successfully");
    }

    @PostMapping("/transaction1-lost-update")
    public ResponseEntity<String> transaction1_lost_update(@RequestParam Long userId) throws InterruptedException {
        userService.transaction1_lost_update(userId);
        return ResponseEntity.ok("Transaction 1 completed successfully");
    }

//
//    @PostMapping("/transaction1-unrepeatable-reads")
//    public ResponseEntity<String> transaction1_unrepeatable_reads(@RequestParam Integer workoutId) throws InterruptedException {
//        workoutService.transaction1_unrepeatable_reads(workoutId);
//        return ResponseEntity.ok("Transaction 1 completed successfully");
//    }
//
//    @PostMapping("/transaction1-dirty-read")
//    public ResponseEntity<String> transaction1_dirty_read(@RequestParam Integer exerciseId) throws InterruptedException {
//        exerciseService.transaction1_dirty_read(exerciseId);
//        return ResponseEntity.ok("Transaction 1 completed successfully");
//    }
//
//
//    @PostMapping("/transaction1-phantom-read")
//    public ResponseEntity<String> transaction1_phantom_read() {
//        exerciseService.transaction1_phantom_read();
//        return ResponseEntity.ok("Transaction 1 completed successfully");
//    }
//
//    @PostMapping("/transaction1-dirty-write-locking")
//    public ResponseEntity<String> transaction1_dirty_write_locking(@RequestParam Integer userId) throws InterruptedException {
//        userService.transaction1_dirty_write_locking(userId);
//        return ResponseEntity.ok("Transaction 1 completed successfully");
//    }
//
//    @PostMapping("/transaction1-unrepeatable-reads-locking")
//    public ResponseEntity<String> transaction1_unrepeatable_reads_locking(@RequestParam Integer workoutId) throws InterruptedException {
//        workoutService.transaction1_unrepeatable_reads_locking(workoutId);
//        return ResponseEntity.ok("Transaction 1 completed successfully");
//    }
//
//    @PostMapping("/transaction1-dirty-read-locking")
//    public ResponseEntity<String> transaction1_dirty_read_locking(@RequestParam Integer exerciseId) throws InterruptedException {
//        exerciseService.transaction1_dirty_read_locking(exerciseId);
//        return ResponseEntity.ok("Transaction 1 completed successfully");
//    }
//
//    @PostMapping("/transaction1-phantom-read-locking")
//    public ResponseEntity<String> transaction1_phantom_read_locking() {
//        exerciseService.transaction1_phantom_read_locking();
//        return ResponseEntity.ok("Transaction 1 completed successfully");
//    }

}
