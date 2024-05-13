package dbms.service;

import com.google.gson.JsonParser;
import dbms.model.Exercise;
import dbms.repository.ExerciseRepository;
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

import java.util.List;
import java.util.Optional;

@Service
public class ExerciseService {

    @Autowired
    private ExerciseRepository exerciseRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private PlatformTransactionManager transactionManager;

    // dirty read - notice the isolation level set to uncommitted
    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    public void transaction1_dirty_read(Integer exerciseId) throws InterruptedException {
        // initial read
        Exercise exerciseBefore = exerciseRepository.findById(exerciseId).orElse(null);
        int initialDifficulty = exerciseBefore != null ? exerciseBefore.getDifficultyLevel() : -1;

        // make HTTP request to Python endpoint to perform update
        String pythonUrl = "http://localhost:5000/dirty-read-python";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<>("{\"exercise_id\": " + exerciseId + "}", headers);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(pythonUrl, requestEntity, String.class);

        // sleep 5 seconds to allow the Python code to modify the data
        Thread.sleep(5000);

        // extract modified difficulty level from the response
        int modifiedDifficulty;
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            modifiedDifficulty = JsonParser.parseString(responseEntity.getBody()).getAsJsonObject().get("modified_difficulty").getAsInt();
        } else {
            System.out.println("Error: " + responseEntity.getStatusCodeValue());
            return;
        }

        System.out.println("Initial difficulty level: " + initialDifficulty);
        System.out.println("Modified difficulty level: " + modifiedDifficulty);
    }

    public void transaction1_dirty_read_locking(Integer exerciseId) throws InterruptedException {
        // manually begin a transaction
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());

        try {
            // perform a SELECT ... FOR UPDATE to lock the row until the transaction is committed
            Optional<Exercise> lockedExercise = exerciseRepository.findByIdForUpdate(exerciseId);

            // initial read
            int initialDifficulty = 0;
            if (lockedExercise.isPresent()) {
                initialDifficulty = lockedExercise.get().getDifficultyLevel();
            }

            // make HTTP request to Python endpoint to perform update
            String pythonUrl = "http://localhost:5000/dirty-read-python";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> requestEntity = new HttpEntity<>("{\"exercise_id\": " + exerciseId + "}", headers);
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(pythonUrl, requestEntity, String.class);

            // sleep 5 seconds to allow the Python code to modify the data
            Thread.sleep(5000);

            // extract modified difficulty level from the response
            int modifiedDifficulty;
            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                modifiedDifficulty = JsonParser.parseString(responseEntity.getBody()).getAsJsonObject().get("modified_difficulty").getAsInt();
            } else {
                System.out.println("Error: " + responseEntity.getStatusCodeValue());
                return;
            }

            System.out.println("Initial difficulty level: " + initialDifficulty);
            System.out.println("Modified difficulty level: " + modifiedDifficulty);

            transactionManager.commit(status);

        } catch (Exception e) {
            // rollback the transaction in case of any error
            transactionManager.rollback(status);
            throw e;
        }
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void transaction1_phantom_read() {
        // initial read
        List<Exercise> rowsBefore = exerciseRepository.findByDifficultyLevelEquals(2);

        String pythonUrl = "http://localhost:5000/phantom-read-python";
        restTemplate.postForObject(pythonUrl, null, String.class);

        // second read
        List<Exercise> rowsAfter = exerciseRepository.findByDifficultyLevelEquals(2);

        System.out.println("Rows read before transaction2: " + rowsBefore.size());
        System.out.println("Rows read after transaction2: " + rowsAfter.size());
    }


    public void transaction1_phantom_read_locking() {
        // manually begin a transaction
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());

        try {
            // initial read + perform a SELECT ... FOR UPDATE to lock the row until the transaction is committed
            List<Exercise> rowsBefore = exerciseRepository.findByDifficultyLevel();

            String pythonUrl = "http://localhost:5000/phantom-read-python";
            restTemplate.postForObject(pythonUrl, null, String.class);

            // second read without setting another lock, just reading the data from the database
            List<Exercise> rowsAfter = exerciseRepository.findByDifficultyLevelEquals(2);

            assert rowsBefore != null;
            System.out.println("Rows read before transaction2: " + rowsBefore.size());
            System.out.println("Rows read after transaction2: " + rowsAfter.size());

        } catch (Exception e) {
            // rollback the transaction in case of any error
            transactionManager.rollback(status);
            throw e;
        }
    }
}
