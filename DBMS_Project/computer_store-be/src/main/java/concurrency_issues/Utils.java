package concurrency_issues;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Utils {
    static void printOperatingSystemData(Connection connection, String threadName) throws SQLException {
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

    public static void setReadUncommittedIsolation(Connection connection) {
        try {
            connection.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
            System.out.println("Isolation level set to TRANSACTION_READ_UNCOMMITTED.");
        } catch (SQLException e) {
            System.err.println("Error setting isolation level: " + e.getMessage());
        }
    }
}
