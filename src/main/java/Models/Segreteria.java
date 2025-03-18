package Models;

import Interfacce.ICommand;
import Interfacce.ISetCommand;

import java.sql.SQLException;


public class Segreteria<T> implements ISetCommand<T> {
    private ICommand<T> command;

    public void setCommand(ICommand command){
        this.command = command;
    }
    public T eseguiAzione() throws SQLException {
        return command.execute();
    }

}
