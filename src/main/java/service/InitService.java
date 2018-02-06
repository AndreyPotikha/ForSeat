package service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class InitService {

    public static void createTables() {
        String URL = "jdbc:sqlite:miracle.sqlite";
        try (Connection connection = DriverManager.getConnection(URL);
             Statement statement = connection.createStatement()) {
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS \"time\" (\"i\" INTEGER DEFAULT 0 ,\"distance\" DOUBLE DEFAULT 0 ,\"calibration\" DOUBLE DEFAULT 0 ,\"time_to_i\" TEXT DEFAULT (null) , \"time_in_i\" TEXT DEFAULT (null))");
        } catch (SQLException e) {
            e.printStackTrace();

        }
    }
}
