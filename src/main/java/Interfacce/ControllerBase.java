package Interfacce;


import java.sql.SQLException;

public interface ControllerBase<T> {
    public void setController(T controller) throws SQLException;
}
