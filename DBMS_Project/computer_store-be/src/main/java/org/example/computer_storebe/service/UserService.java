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

    private final String url = "http://flask-app:5000";
//    private final String url = "http://localhost:5000";

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

    public void callPythonDirtyWrite(Long userId) {
        String pythonUrl = url + "/dirty-write-python";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<>("{\"user_id\": " + userId + "}", headers);
        restTemplate.postForEntity(pythonUrl, requestEntity, String.class);
    }

    public void callPythonLostUpdate(Long userId) {
        String pythonUrl = url + "/lost-update-python";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<>("{\"user_id\": " + userId + "}", headers);
        restTemplate.postForEntity(pythonUrl, requestEntity, String.class);
    }

    public void callPythonUnrepeatableReads(Long userId){
        String pythonUrl = url + "/simulate-update";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<>("{\"user_id\": " + userId + "}", headers);
        restTemplate.postForEntity(pythonUrl, requestEntity, String.class);
    }

    public void callDirtyRead(Long userId){
        String pythonUrl = url + "/simulate-update";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<>("{\"user_id\": " + userId + "}", headers);
        restTemplate.postForEntity(pythonUrl, requestEntity, String.class);
    }

    public void callPhantomRead(Long userId){
        String pythonUrl = url + "/simulate-update";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<>("{\"user_id\": " + userId + "}", headers);
        restTemplate.postForEntity(pythonUrl, requestEntity, String.class);
    }

    // dirty write - notice the isolation level set to read uncommitted
    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    public User transaction1_dirty_write(Long userId) throws InterruptedException {
        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            user.setLastName("Sparrrrrow");
            userRepository.save(user);
            callPythonDirtyWrite(userId);
            Thread.sleep(1000); // Simulate delay
        }
        return userRepository.findById(userId).orElse(null);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE, timeout = 10, propagation = Propagation.REQUIRES_NEW)
    public User transaction1_dirty_write_locking(Long userId) throws InterruptedException {
        User user = userRepository.findById(userId).orElse(null); // Using the locking method
        if (user != null) {
            user.setLastName("Sparrrrrow");
            userRepository.save(user);
            Thread.sleep(1000); // Simulate delay

            String pythonUrl = url + "/dirty-write-python";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> requestEntity = new HttpEntity<>("{\"user_id\": " + userId + "}", headers);

            // This call should wait or fail if the lock is effective
            restTemplate.postForEntity(pythonUrl, requestEntity, String.class);
        }

        return userRepository.findByIdWithLocking(userId).orElse(null); // Check the result after the transaction
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

            callPythonLostUpdate(userId);

            Optional<User> userAfterUpdate = userRepository.findById(userId);
            Map<String, String> result = new HashMap<>();
            result.put("beforeUpdate", userBeforeUpdate.get().getLastName());
            result.put("afterUpdate", userAfterUpdate.isPresent() ? userAfterUpdate.get().getLastName() : "Not found");
            return result;
        } else {
            return Collections.singletonMap("error", "User not found");
        }
    }

    @Transactional(isolation = Isolation.SERIALIZABLE, timeout = 10, propagation = Propagation.REQUIRES_NEW)
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
            Thread.sleep(1000);

            String pythonUrl = url + "/lost-update-python";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> requestEntity = new HttpEntity<>("{\"user_id\": " + userId + "}", headers);
            restTemplate.postForEntity(pythonUrl, requestEntity, String.class);

            Optional<User> userAfterUpdate = userRepository.findByIdWithLocking(userId);
            Map<String, String> result = new HashMap<>();
            result.put("beforeUpdate", userBeforeUpdate.get().getLastName());
            result.put("afterUpdate", userAfterUpdate.isPresent() ? userAfterUpdate.get().getLastName() : "Not found");
            return result;
        } else {
            return Collections.singletonMap("error", "User not found");
        }
    }

    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    public User transaction1_unrepeatable_reads(Long userId) throws InterruptedException {
        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            Thread.sleep(5000); // Simulate delay
            callPythonDirtyWrite(userId);
        }
        return userRepository.findById(userId).orElse(null);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE, timeout = 10, propagation = Propagation.REQUIRES_NEW)
    public Map<String, String> transaction1_unrepeatable_reads_locking(Long userId) throws InterruptedException {
        Map<String, String> results = new HashMap<>();
        Optional<User> firstRead = userRepository.findById(userId);
        results.put("firstRead", firstRead.map(User::toString).orElse("User not found"));

        // Call the Python endpoint to modify the data
        String pythonUrl = url + "/simulate-update";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<>("{\"user_id\": " + userId + "}", headers);
        restTemplate.postForEntity(pythonUrl, requestEntity, String.class);
        Thread.sleep(1000 * 60);

        Optional<User> finalRead = userRepository.findByIdWithLocking(userId);
        results.put("finalRead", finalRead.map(User::toString).orElse("User not found"));
        return results;
    }

    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    public User transaction1_dirty_read(Long userId) throws InterruptedException {
        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            Thread.sleep(5000); // Simulate delay
            callPythonDirtyWrite(userId);
        }
        return userRepository.findById(userId).orElse(null);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE, timeout = 10, propagation = Propagation.REQUIRES_NEW)
    public Map<String, String> transaction1_dirty_read_locking(Long userId) throws InterruptedException {
        Map<String, String> results = new HashMap<>();
        Optional<User> firstRead = userRepository.findById(userId);
        results.put("firstRead", firstRead.map(User::toString).orElse("User not found"));

        // Call the Python endpoint to modify the data
        String pythonUrl = url + "/simulate-update";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<>("{\"user_id\": " + userId + "}", headers);
        restTemplate.postForEntity(pythonUrl, requestEntity, String.class);
        Thread.sleep(1000 * 60);

        Optional<User> finalRead = userRepository.findByIdWithLocking(userId);
        results.put("finalRead", finalRead.map(User::toString).orElse("User not found"));
        return results;
    }

    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    public User transaction1_phantom_read(Long userId) throws InterruptedException {
        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            Thread.sleep(5000); // Simulate delay
            callPythonDirtyWrite(userId);
        }
        return userRepository.findById(userId).orElse(null);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE, timeout = 10, propagation = Propagation.REQUIRES_NEW)
    public Map<String, String> transaction1_phantom_read_locking(Long userId) throws InterruptedException {
        Map<String, String> results = new HashMap<>();
        Optional<User> firstRead = userRepository.findById(userId);
        results.put("firstRead", firstRead.map(User::toString).orElse("User not found"));

        // Call the Python endpoint to modify the data
        String pythonUrl = url + "/simulate-update";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<>("{\"user_id\": " + userId + "}", headers);
        restTemplate.postForEntity(pythonUrl, requestEntity, String.class);
        Thread.sleep(1000 * 60);

        Optional<User> finalRead = userRepository.findByIdWithLocking(userId);
        results.put("finalRead", finalRead.map(User::toString).orElse("User not found"));
        return results;
    }
}
