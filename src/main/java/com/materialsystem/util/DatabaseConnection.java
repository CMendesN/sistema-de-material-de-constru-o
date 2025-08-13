package com.materialsystem.util;

//import io.github.cdimascio.dotenv.Dotenv;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

//    private static final Dotenv dotenv = Dotenv.load();

    private static final String DB_IP = "192.168.100.55";
    private static final String DB_PORT = "5432";
    private static final String DB_DATABASE = "materialsystem";
    private static final String DB_USER = "system";
    private static final String DB_PASS = "System123#";

    private static final String URL = "jdbc:postgresql://" + DB_IP + ":" + DB_PORT + "/" + DB_DATABASE;

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, DB_USER, DB_PASS);
    }
}
    