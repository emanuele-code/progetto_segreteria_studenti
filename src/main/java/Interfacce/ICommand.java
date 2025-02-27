package Interfacce;

import java.sql.SQLException;

public interface ICommand<T> {
    public T execute() throws SQLException;
}
