package Interfacce;


import java.sql.SQLException;

public interface IControllerBase<T> {
    public void setController(T controller) throws SQLException;
}
