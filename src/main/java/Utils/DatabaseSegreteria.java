package Utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseSegreteria {
    private static final String URL = "jdbc:sqlite:DatabaseSegreteria.sqlite";

    private static Connection connection;

    private DatabaseSegreteria() {}

    public static Connection getConnection() throws SQLException, ClassNotFoundException {
        if (connection == null) {
            synchronized (DatabaseSegreteria.class) {
                if (connection == null) {
                    connection = DriverManager.getConnection(URL);
                }
            }
        }
        return connection;
    }
}
