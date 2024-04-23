package concurrency_issues;

import java.sql.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static concurrency_issues.Utils.printOperatingSystemData;

public class DirtyRead {
    private final Lock updateLock = new ReentrantLock();
    private final Lock readLock = new ReentrantLock();

    public void dirtyRead(Connection connection) {
        Thread updateThread = new Thread(() -> {
            try {
                updateLock.lock(); // Acquire the lock to ensure sequential execution with readThread
                connection.setAutoCommit(false); // Start a transaction

                try (Statement statement = connection.createStatement()) {
                    printOperatingSystemData(connection, "updateThread");

                    // Update the operating system version
                    String updateQuery = "UPDATE operating_system SET os_version = 15 WHERE id = 100000001";
                    int rowsUpdated = statement.executeUpdate(updateQuery);
                    // Sleep to simulate a delay before committing
                    Thread.sleep(2000);

                    // Commit the transaction
                    connection.commit();
                } catch (SQLException | InterruptedException e) {
                    connection.rollback();
                    e.printStackTrace();
                } finally {
                    updateLock.unlock(); // Release the lock after execution
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        Thread readThread = new Thread(() -> {
            try {
                readLock.lock(); // Acquire the lock to ensure sequential execution with updateThread
                // Set isolation level to read uncommitted data
                connection.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
                connection.setAutoCommit(false); // Start a transaction

                try (Statement statement = connection.createStatement()) {
                    printOperatingSystemData(connection, "readThread");
                    Thread.sleep(1000);
                    // Perform a dirty read by reading uncommitted data
                    printOperatingSystemData(connection, "readThread");
                } catch (InterruptedException | SQLException e) {
                    throw new RuntimeException(e);
                } finally {
                    readLock.unlock(); // Release the lock after execution
                }

                // Rollback the transaction without waiting for commit
                connection.commit();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        // Start the threads
        updateThread.start();
        readThread.start();

        // Wait for threads to finish
        try {
            updateThread.join();
            readThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
