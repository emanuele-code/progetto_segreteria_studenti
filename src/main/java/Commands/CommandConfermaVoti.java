package Commands;

import Dao.DAOStudente;
import Interfacce.ICommand;
import Interfacce.IStudenteDAO;

import java.sql.Connection;
import java.sql.SQLException;

public class CommandConfermaVoti implements ICommand<Void> {

    private IStudenteDAO studenteDAO;
    private String scelta;
    private String matricola;
    private String numeroAppello;

    public CommandConfermaVoti(Connection connection, String scelta, String matricola, String numeroAppello) {
        studenteDAO        = new DAOStudente(connection);
        this.scelta        = scelta;
        this.matricola     = matricola;
        this.numeroAppello = numeroAppello;
    }

    @Override
    public Void execute() throws SQLException {
        studenteDAO.ConfermaVoti(scelta, matricola, numeroAppello);
        return null;
    }
}