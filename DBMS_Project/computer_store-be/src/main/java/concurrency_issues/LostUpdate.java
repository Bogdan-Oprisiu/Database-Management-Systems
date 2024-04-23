package concurrency_issues;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static concurrency_issues.Utils.printOperatingSystemData;
import static concurrency_issues.Utils.setReadUncommittedIsolation;

public class LostUpdate {
    private final Lock lock = new ReentrantLock();
    private final Condition thread1Finished = lock.newCondition();
    private boolean thread1Completed = false;

    public void lostUpdate(Connection connection) throws SQLException {
        Thread updateThread1 = new Thread(() -> {
            try {
                Statement statement = connection.createStatement();
                setReadUncommittedIsolation(connection);
                // Update the operating system version
                String updateQuery = "UPDATE operating_system SET os_version = CONCAT(operating_system.os_version, '1') WHERE id = 100000001";
                lock.lock(); // Acquire the lock before updating
                int rowsUpdated = statement.executeUpdate(updateQuery);
                printOperatingSystemData(connection, "updateThread1");
                Thread.sleep(1000);
                thread1Completed = true;
                thread1Finished.signal(); // Signal that thread1 has completed
                // Wait for thread2 to finish
                while (!thread1Completed) {
                    thread1Finished.await();
                }
                printOperatingSystemData(connection, "updateThread1");
                Thread.sleep(1000);
            } catch (SQLException | InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                lock.unlock(); // Release the lock
            }
        });

        Thread updateThread2 = new Thread(() -> {
            try {
                Statement statement = connection.createStatement();
                setReadUncommittedIsolation(connection);
                // Start locked until the first thread updates and unlocks
                lock.lock();
                // Wait for thread1 to complete
                while (!thread1Completed) {
                    thread1Finished.await();
                }
                // Update the operating system version
                String updateQuery = "UPDATE operating_system SET os_version = 'Ubuntu' WHERE operating_system.os_version LIKE 'Fedora%'";
                int rowsUpdated = statement.executeUpdate(updateQuery);
                printOperatingSystemData(connection, "updateThread2");
                Thread.sleep(1000);
            } catch (SQLException | InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                lock.unlock(); // Unlock for the first thread to continue
            }
        });

        // Start the threads in the desired order
        updateThread1.start();
        updateThread2.start();

        // Wait for threads to finish
        try {
            updateThread1.join();
            updateThread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        printOperatingSystemData(connection, "End");
    }
}
