package dbms.service;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dbms.model.Workout;
import dbms.repository.WorkoutRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
public class WorkoutService {

    @Autowired
    private WorkoutRepository workoutRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private PlatformTransactionManager transactionManager;

    // unrepeatable read - notice the isolation level set to committed
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void transaction1_unrepeatable_reads(Integer workoutId) throws InterruptedException {
        Workout workoutBefore = workoutRepository.findById(workoutId).orElse(null);

        if (workoutBefore != null) {
            // initial read
            int initialDuration = workoutBefore.getDuration();

            // make HTTP request to Python endpoint to perform update
            String pythonUrl = "http://localhost:5000/unrepeatable-reads-python";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> requestEntity = new HttpEntity<>("{\"workout_id\": " + workoutId + "}", headers);
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(pythonUrl, requestEntity, String.class);

            // sleep 5 seconds to allow the python code to modify the data
            Thread.sleep(5000);

            // fetch the same data again but now from the python code to get the updated data and perform second read
            String responseBody = responseEntity.getBody();
            JsonObject jsonObject = JsonParser.parseString(responseBody).getAsJsonObject();
            int modifiedDuration = jsonObject.get("modified_duration").getAsInt();

            System.out.println("Initial duration: " + initialDuration);
            System.out.println("Modified duration: " + modifiedDuration);
        }
    }

    // unrepeatable read with locking mechanism
    public void transaction1_unrepeatable_reads_locking(Integer workoutId) throws InterruptedException {
        // manually begin a transaction
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());

        try {
            // perform a SELECT ... FOR UPDATE to lock the row until the transaction is committed
            Optional<Workout> lockedWorkout = workoutRepository.findByIdForUpdate(workoutId);

            // initial read
            int initialDuration = 0;
            if (lockedWorkout.isPresent()) {
                initialDuration = lockedWorkout.get().getDuration();
            }

            // make HTTP request to Python endpoint to perform update
            String pythonUrl = "http://localhost:5000/unrepeatable-reads-python";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> requestEntity = new HttpEntity<>("{\"workout_id\": " + workoutId + "}", headers);
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(pythonUrl, requestEntity, String.class);

            // sleep 5 seconds to allow the Python code to modify the data
            Thread.sleep(5000);

            // fetch the same data again but now from the Python code to get the updated data and perform second read
            String responseBody = responseEntity.getBody();
            JsonObject jsonObject = JsonParser.parseString(responseBody).getAsJsonObject();
            int modifiedDuration = jsonObject.get("modified_duration").getAsInt();

            System.out.println("Initial duration: " + initialDuration);
            System.out.println("Modified duration: " + modifiedDuration);

            // commit the transaction to release the lock
            transactionManager.commit(status);
        } catch (Exception e) {
            // rollback the transaction in case of any error
            transactionManager.rollback(status);
            throw e;
        }
    }

}
