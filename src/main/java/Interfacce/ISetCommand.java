package Interfacce;

import java.sql.SQLException;

public interface ISetCommand<T> {
    void setCommand(ICommand<T> command);
    T eseguiAzione() throws SQLException;
}
