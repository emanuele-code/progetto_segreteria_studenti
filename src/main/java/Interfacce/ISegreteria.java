package Interfacce;

import java.sql.SQLException;

public interface ISegreteria<T> {
    public void setCommand(ICommand<T> command);
    public T eseguiAzione() throws SQLException;
}
