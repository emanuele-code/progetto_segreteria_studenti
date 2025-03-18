package Interfacce;

import java.sql.SQLException;

public interface ICommand<T> {
    T execute() throws SQLException;
}
