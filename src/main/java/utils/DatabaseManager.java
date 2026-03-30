package utils;

import javax.swing.plaf.nimbus.State;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DatabaseManager {

    // The connection string pointing to our local SQLite file
    private static final String DB_URL = "jdbc:sqlite:automation_vault.db";

    /**
     * Executes a mock database setup and retrieves a value based on a SQL query.
     * In a real JPMC environment, this would connect to alive Oracle/PostgreSQL DB.
     */
    public static String getRoleFromDatabase(String userName) {
        String fetchedRole = null;

        // Try-with-resources automatically closes the connection to prevent memory leaks!
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {

            // 1. MOCK SETUP: Create a table and insert our data (Simulating the API's backend action)
            stmt.execute("CREATE TABLE IF NOT EXISTS users (id INTEGER PRIMARY KEY, name TEXT, role TEXT)");
            // Clear old data so we don't get duplicates
            stmt.execute("DELETE FROM users");
            // Insert the expected data
            stmt.execute("INSERT INTO users (name, role) VALUES ('" + userName + "', 'Senior SDET')");

            // 2. THE VALIDATION: Run the actual SQL SELECT query
            String sqlQuery = "SELECT role FROM users WHERE name = '" + userName + "'";
            ResultSet resultSet = stmt.executeQuery(sqlQuery);

            // 3. EXTRACT THE DATA
            if (resultSet.next()) {
                fetchedRole = resultSet.getString("role");
            }
        }catch (Exception e) {
            // 1. Log the critical failure directly into the TestNG HTML report
            org.testng.Reporter.log("CRITICAL ERROR: Database execution failed - " + e.getMessage(), true);

            // 2. Halt the execution instantly so we don't return a confusing 'null'
            throw new RuntimeException("Database infrastructure failure! Check SQL syntax and connection.");
        }

        return fetchedRole;
    }
}
