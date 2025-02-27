package Interfacce;

import java.sql.SQLException;

public interface IDocente<T> {
    public String getCf();
    public String getNome();
    public String getCognome();
    public String getEmail();

    public void setCommand(ICommand<T> command);
    public T eseguiAzione() throws SQLException;
}
