package concurrency_issues;

import java.sql.*;

public class ConcurrencyIssues {
    public static void main(String[] args) {
        try {
            deleteInsertedOperatingSystemData(getConnection());

            // Connect to the database
            Connection connection = getConnection();
            insertOperatingSystemData(connection, "Linux", "Fedora");
//            printOperatingSystemData(connection);
            System.out.println();

//            System.out.println("Dirty read:");
//            DirtyRead dirtyRead = new DirtyRead();
//            dirtyRead.dirtyRead(connection);

//            System.out.println("Dirty write: ");
//            DirtyWrite dirtyWrite = new DirtyWrite();
//            dirtyWrite.dirtyWrite(connection);

            System.out.println("Lost update: ");
            LostUpdate lostUpdate = new LostUpdate();
            lostUpdate.lostUpdate(connection);

            System.out.println();
//            printOperatingSystemData(connection);

            deleteInsertedOperatingSystemData(connection);
            // Close the connection
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static Connection getConnection() throws SQLException {
        String jdbcUrl = "jdbc:postgresql://localhost:5432/computer_store";
        String username = "postgres";
        String password = "bogdan123";
        return DriverManager.getConnection(jdbcUrl, username, password);
    }

    private static void insertOperatingSystemData(Connection connection, String osName, String osVersion) throws SQLException {
        String insertQuery = "INSERT INTO operating_system (id, os_name, os_version) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(insertQuery)) {
            int id = 100000001; // Generate or retrieve the id from somewhere
            statement.setInt(1, id);
            statement.setString(2, osName);
            statement.setString(3, osVersion);
            statement.executeUpdate();
            System.out.println("Inserted: " + osName + " " + osVersion);
        }
    }

    private static void printOperatingSystemData(Connection connection) throws SQLException {
        String selectQuery = "SELECT * FROM operating_system";
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(selectQuery);
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String osName = resultSet.getString("os_name");
                String osVersion = resultSet.getString("os_version"); // Retrieve as string
                System.out.println("ID: " + id + ", Name: " + osName + ", Version: " + osVersion);
            }
        }
    }

    private static void deleteInsertedOperatingSystemData(Connection connection) throws SQLException {
        String deleteQuery = "DELETE FROM operating_system WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(deleteQuery)) {
            statement.setInt(1, 100000001);
            int rowsAffected = statement.executeUpdate();
            System.out.println("Deleted " + rowsAffected + " row(s) from operating_system table.");
        }
    }
}
