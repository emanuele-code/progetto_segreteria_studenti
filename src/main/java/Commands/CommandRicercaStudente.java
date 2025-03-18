package Commands;
import Interfacce.ICommand;
import Interfacce.IRicercaStudenteStrategy;
import Interfacce.ISetCommand;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class CommandRicercaStudente implements ICommand<List<ISetCommand>> {

    private IRicercaStudenteStrategy strategy;
    private Connection connection;

    public CommandRicercaStudente(IRicercaStudenteStrategy strategy, Connection connection){
        this.strategy = strategy;
        this.connection = connection;
    }

    @Override
    public List<ISetCommand> execute() throws SQLException {
        return strategy.ricercaStudente(connection);
    }
}