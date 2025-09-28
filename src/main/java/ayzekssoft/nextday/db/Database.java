package ayzekssoft.nextday.db;

import ayzekssoft.nextday.config.ApplicationConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
    private static Connection connection;
    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException ignored) {}
    }

    public static synchronized Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                connection = DriverManager.getConnection(
                        ApplicationConfig.getDatabaseUrl(),
                        ApplicationConfig.getDatabaseUser(),
                        ApplicationConfig.getDatabasePassword()
                );
            } catch (SQLException e) {
                if (e.getMessage() != null && e.getMessage().contains("does not exist")) {
                    ensureDatabaseExists();
                    connection = DriverManager.getConnection(
                            ApplicationConfig.getDatabaseUrl(),
                            ApplicationConfig.getDatabaseUser(),
                            ApplicationConfig.getDatabasePassword()
                    );
                } else {
                    throw e;
                }
            }
        }
        return connection;
    }

    private static void ensureDatabaseExists() throws SQLException {
        String adminUrl = ApplicationConfig.getAdminDatabaseUrl();
        String dbName = ApplicationConfig.getDatabaseName();
        try (Connection conn = DriverManager.getConnection(adminUrl, ApplicationConfig.getDatabaseUser(), ApplicationConfig.getDatabasePassword());
             Statement st = conn.createStatement()) {
            st.executeUpdate("CREATE DATABASE \"" + dbName + "\"");
        }
    }
}


