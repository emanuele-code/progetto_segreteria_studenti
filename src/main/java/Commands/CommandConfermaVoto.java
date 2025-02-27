package Commands;

import Dao.DAOSegreteria;
import Interfacce.ICommand;
import Interfacce.ISegreteriaDAO;

import java.sql.Connection;
import java.sql.SQLException;

public class CommandConfermaVoto implements ICommand<Void> {

    private ISegreteriaDAO segreteriaDAO;
    private String matricola;
    private String numeroAppello;

    public CommandConfermaVoto(Connection connection, String matricola, String numeroAppello) {
        segreteriaDAO = new DAOSegreteria(connection);
        this.matricola = matricola;
        this.numeroAppello = numeroAppello;
    }

    @Override
    public Void execute() throws SQLException {
        segreteriaDAO.confermaVoto(matricola, numeroAppello);
        return null;
    }
}