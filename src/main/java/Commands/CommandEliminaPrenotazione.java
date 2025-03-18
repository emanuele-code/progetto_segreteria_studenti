package Commands;

import Dao.DAOStudente;
import Interfacce.ICommand;
import Interfacce.IStudenteDAO;

import java.sql.Connection;
import java.sql.SQLException;

public class CommandEliminaPrenotazione implements ICommand<Void> {
    private IStudenteDAO studenteDAO;
    private String matricola;
    private String numeroAppello;

    public CommandEliminaPrenotazione(Connection connection, String matricola, String numeroAppello) {
        studenteDAO        = new DAOStudente(connection);
        this.matricola     = matricola;
        this.numeroAppello = numeroAppello;
    }

    @Override
    public Void execute() throws SQLException {
        studenteDAO.eliminaPrenotazione(numeroAppello, matricola);
        return null;
    }
}
