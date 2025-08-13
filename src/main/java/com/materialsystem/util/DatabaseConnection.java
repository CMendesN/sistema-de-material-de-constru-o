package com.materialsystem.util;

//import io.github.cdimascio.dotenv.Dotenv;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String DB_IP = "192.168.100.55";
    private static final String DB_PORT = "5432";
    private static final String DB_DATABASE = "materialsystem";
    private static final String DB_USER = "system";
    private static final String DB_PASS = "System123#";
    private static final String URL = "jdbc:postgresql://" + DB_IP + ":" + DB_PORT + "/" + DB_DATABASE;

    static {
        try {
            Class.forName("org.postgresql.Driver");
            // opcional: tempo máx. para tentar login
            java.sql.DriverManager.setLoginTimeout(5);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Driver PostgreSQL não encontrado no WEB-INF/lib", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, DB_USER, DB_PASS);
    }
}