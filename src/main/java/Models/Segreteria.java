package Models;

import Interfacce.ICommand;
import Interfacce.ISegreteria;

import java.sql.Connection;
import java.sql.SQLException;


public class Segreteria<T> implements ISegreteria<T> {
    private final Connection connection;
    private ICommand<T> command;

    Segreteria(Connection connection){
        this.connection = connection;
    }

    public void setCommand(ICommand command){
        this.command = command;
    }

    public T eseguiAzione() throws SQLException {
        return command.execute();
    }

}
