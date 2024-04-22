package concurrency_issues;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DirtyWrite {
    private final Lock updateLock1 = new ReentrantLock();
    private final Lock updateLock2 = new ReentrantLock();

    public void dirtyWrite(Connection connection) {
        Thread updateThread1 = new Thread(() -> {
            try {
                updateLock1.lock(); // Acquire the lock to ensure sequential execution with updateThread2
                connection.setAutoCommit(false); // Start a transaction

                try (Statement statement = connection.createStatement()) {
                    printOperatingSystemData(connection, "updateThread1");

                    // Update the operating system version
                    String updateQuery = "UPDATE operating_system SET os_version = 15 WHERE id = 100000001";
                    int rowsUpdated = statement.executeUpdate(updateQuery);
                    // Sleep to simulate a delay before committing
                    Thread.sleep(2000);

                    // Wait for updateThread2
                    while (updateLock2.tryLock()) {
                        updateLock2.unlock();
                        Thread.sleep(100); // Wait for a while before retrying
                    }

                    // Commit the transaction
                    connection.commit();
                } catch (SQLException | InterruptedException e) {
                    connection.rollback();
                    e.printStackTrace();
                } finally {
                    updateLock1.unlock(); // Release the lock after execution
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        Thread updateThread2 = new Thread(() -> {
            try {
                updateLock2.lock(); // Acquire the lock to ensure sequential execution with updateThread1
                // Set isolation level to read uncommitted data
                connection.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
                connection.setAutoCommit(false); // Start a transaction

                try (Statement statement = connection.createStatement()) {
                    printOperatingSystemData(connection, "updateThread2");
                    Thread.sleep(1000);
                    // Perform a dirty read by reading uncommitted data
                    printOperatingSystemData(connection, "updateThread2");

                    // Wait for updateThread1
                    while (updateLock1.tryLock()) {
                        updateLock1.unlock();
                        Thread.sleep(100); // Wait for a while before retrying
                    }

                    // Commit the transaction
                    connection.commit();
                } catch (InterruptedException | SQLException e) {
                    throw new RuntimeException(e);
                } finally {
                    updateLock2.unlock(); // Release the lock after execution
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        // Start the threads
        updateThread1.start();
        updateThread2.start();

        // Wait for threads to finish
        try {
            updateThread1.join();
            updateThread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void printOperatingSystemData(Connection connection, String threadName) throws SQLException {
        String selectQuery = "SELECT * FROM operating_system";
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(selectQuery);
            System.out.println(threadName);
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String osName = resultSet.getString("os_name");
                String osVersion = resultSet.getString("os_version"); // Retrieve as string
                System.out.println("ID: " + id + ", Name: " + osName + ", Version: " + osVersion);
            }
            System.out.println();
        }
    }
}
