package org.example.computer_storebe.service;

import org.example.computer_storebe.entity.User;
import org.example.computer_storebe.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private TransactionTemplate transactionTemplate;

    private List<User> fetchAllUsers() {
        return userRepository.findAll();
    }

    // dirty write - notice the isolation level set to read uncommitted
    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    public ResponseEntity<Map<String, Object>> transaction1_dirty_write(Long userId) throws InterruptedException {
        Map<String, Object> response = new HashMap<>();
        response.put("before", fetchAllUsers());  // Users before the transaction

        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            user.setFirstName("Jack");
            userRepository.save(user);
        }

        response.put("during", fetchAllUsers());  // Users during the transaction, before Python call

        Thread.sleep(5000);  // Simulate delay

        String pythonUrl = "http://localhost:5000/dirty-write-python";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<>("{\"user_id\": " + userId + "}", headers);
        restTemplate.postForEntity(pythonUrl, requestEntity, String.class);

        response.put("after", fetchAllUsers());  // Users after the Python call

        return ResponseEntity.ok(response);
    }

    // after about a minute a lock timeout will appear that demonstrates the fact that by using locking mechanism,
    // the database was able to prevent a dirty write from happening
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void transaction1_dirty_write_locking(Long userId) throws InterruptedException {
        User user = userRepository.findById(userId).orElse(null);

        if (user != null) {
            user.setFirstName("Jonas");
            userRepository.save(user);
        }

        Thread.sleep(5000); // delay for 5 seconds

        // call the Python endpoint to perform a separate transaction that updates the first name of the same user
        String pythonUrl = "http://localhost:5000/dirty-write-locking-python";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<>("{\"user_id\": " + userId + "}", headers);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(pythonUrl, requestEntity, String.class);
    }


    // lost update - notice the use of transaction template to not allow auto commit at the end od the method
    // doesn't need explicit locking mechanism since the data updated from the first transaction is committed before
    // the second transaction tries to update it
    public void transaction1_lost_update(Long userId) throws InterruptedException {
        User user = userRepository.findById(userId).orElse(null);

        if (user != null) {
            transactionTemplate.execute(status -> {
                user.setLastName("Roberts");
                userRepository.save(user);
                return null;
            });
        }

        // call the Python endpoint to perform the second transaction
        String pythonUrl = "http://localhost:5000/lost-update-python";

        Thread.sleep(5000);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<>("{\"user_id\": " + userId + "}", headers);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(pythonUrl, requestEntity, String.class);
    }
}
