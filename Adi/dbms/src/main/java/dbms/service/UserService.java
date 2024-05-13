package dbms.service;

import dbms.model.User;
import dbms.repository.UserRepository;
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

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private TransactionTemplate transactionTemplate;

    // dirty write - notice the isolation level set to read uncommitted
    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    public void transaction1_dirty_write(Integer userId) throws InterruptedException {
        User user = userRepository.findById(userId).orElse(null);

        if (user != null) {
            user.setFirstName("Jack");
            userRepository.save(user);
        }

        Thread.sleep(5000); // delay for 5 seconds

        // call the Python endpoint to perform a separate transaction that updates the first name of the same user
        String pythonUrl = "http://localhost:5000/dirty-write-python";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<>("{\"user_id\": " + userId + "}", headers);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(pythonUrl, requestEntity, String.class);
    }

    // after about a minute a lock timeout will appear that demonstrates the fact that by using locking mechanism,
    // the database was able to prevent a dirty write from happening
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void transaction1_dirty_write_locking(Integer userId) throws InterruptedException {
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
    public void transaction1_lost_update(Integer userId) throws InterruptedException {
        User user = userRepository.findById(userId).orElse(null);

        if (user != null) {
            transactionTemplate.execute(status -> {
                user.setLastName("Roberts");
                userRepository.save(user);
                return null;
            });
        }

        Thread.sleep(5000);

        // call the Python endpoint to perform the second transaction
        String pythonUrl = "http://localhost:5000/lost-update-python";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<>("{\"user_id\": " + userId + "}", headers);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(pythonUrl, requestEntity, String.class);
    }



}
