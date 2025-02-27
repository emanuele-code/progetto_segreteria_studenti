package Commands;

import Dao.DAOSegreteria;
import Interfacce.ICommand;
import Interfacce.ISegreteriaDAO;

import java.sql.Connection;
import java.sql.SQLException;

public class CommandInserisciStudente implements ICommand<Void> {

    private ISegreteriaDAO segreteriaDAO;
    private String[] credenziali;

    public CommandInserisciStudente(Connection connection, String[] credenziali) {
        segreteriaDAO = new DAOSegreteria(connection);
        this.credenziali = credenziali;
    }

    @Override
    public Void execute() throws SQLException {
        segreteriaDAO.inserisciStudente(credenziali);
        return null;
    }
}