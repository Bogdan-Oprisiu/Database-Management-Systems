package org.example.computer_storebe.service;

import org.example.computer_storebe.entity.User;
import org.example.computer_storebe.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private TransactionTemplate transactionTemplate;

    public List<User> fetchAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> fetchUser(Long id) {
        return userRepository.findById(id);
    }

    // dirty write - notice the isolation level set to read uncommitted
    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    public User transaction1_dirty_write(Long userId) throws InterruptedException {
        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            user.setLastName("Sparrrrrow");
            userRepository.save(user);
            Thread.sleep(5000); // Simulate delay

            String pythonUrl = "http://localhost:5000/dirty-write-python";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> requestEntity = new HttpEntity<>("{\"user_id\": " + userId + "}", headers);
            restTemplate.postForEntity(pythonUrl, requestEntity, String.class);
        }
        return userRepository.findById(userId).orElse(null);
    }

    // after about a minute a lock timeout will appear that demonstrates the fact that by using locking mechanism,
    // the database was able to prevent a dirty write from happening
    @Transactional(isolation = Isolation.SERIALIZABLE, timeout = 10, propagation = Propagation.REQUIRES_NEW)
    public User transaction1_dirty_write_locking(Long userId) throws InterruptedException {
        User user = userRepository.findById(userId).orElse(null); // Using the locking method
        if (user != null) {
            user.setLastName("Sparrrrrow");
            userRepository.save(user);
            Thread.sleep(5000); // Simulate delay

            String pythonUrl = "http://localhost:5000/dirty-write-python";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> requestEntity = new HttpEntity<>("{\"user_id\": " + userId + "}", headers);

            // This call should wait or fail if the lock is effective
            restTemplate.postForEntity(pythonUrl, requestEntity, String.class);
        }

        return userRepository.findByIdForUpdate(userId).orElse(null); // Check the result after the transaction
    }

    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    public Map<String, String> transaction1_lost_update(Long userId) throws InterruptedException {
        Optional<User> userBeforeUpdate = userRepository.findById(userId);

        if (userBeforeUpdate.isPresent()) {
            transactionTemplate.execute(status -> {
                User user = userBeforeUpdate.get();
                user.setLastName("Roberts");
                userRepository.save(user);
                return null;
            });

            // Simulate delay before calling Python
            Thread.sleep(5000);

            String pythonUrl = "http://localhost:5000/lost-update-python";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> requestEntity = new HttpEntity<>("{\"user_id\": " + userId + "}", headers);
            restTemplate.postForEntity(pythonUrl, requestEntity, String.class);

            Optional<User> userAfterUpdate = userRepository.findById(userId);
            Map<String, String> result = new HashMap<>();
            result.put("beforeUpdate", userBeforeUpdate.get().getLastName());
            result.put("afterUpdate", userAfterUpdate.isPresent() ? userAfterUpdate.get().getLastName() : "Not found");
            return result;
        } else {
            return Collections.singletonMap("error", "User not found");
        }
    }

    public Map<String, String> transaction1_lost_update_locking(Long userId) throws InterruptedException {
        Optional<User> userBeforeUpdate = userRepository.findById(userId);

        if (userBeforeUpdate.isPresent()) {
            transactionTemplate.execute(status -> {
                User user = userBeforeUpdate.get();
                user.setLastName("Roberts");
                userRepository.save(user);
                return null;
            });

            // Simulate delay before calling Python
            Thread.sleep(5000);

            String pythonUrl = "http://localhost:5000/lost-update-python";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> requestEntity = new HttpEntity<>("{\"user_id\": " + userId + "}", headers);
            restTemplate.postForEntity(pythonUrl, requestEntity, String.class);

            Optional<User> userAfterUpdate = userRepository.findByIdForUpdate(userId);
            Map<String, String> result = new HashMap<>();
            result.put("beforeUpdate", userBeforeUpdate.get().getLastName());
            result.put("afterUpdate", userAfterUpdate.isPresent() ? userAfterUpdate.get().getLastName() : "Not found");
            return result;
        } else {
            return Collections.singletonMap("error", "User not found");
        }
    }
}
