package Commands;

import Dao.DAOStudente;
import Interfacce.ICommand;
import Interfacce.IStudenteDAO;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class CommandPrenotaEsame implements ICommand<Void> {

    private IStudenteDAO studenteDAO;
    private String numeroAppello;
    private String matricola;

    public CommandPrenotaEsame(Connection connection, String numeroAppello, String matricola) {
        studenteDAO        = new DAOStudente(connection);
        this.numeroAppello = numeroAppello;
        this.matricola     = matricola;
    }

    @Override
    public Void execute() throws SQLException {
        studenteDAO.prenotaEsame(this.numeroAppello, this.matricola);
        return null;
    }
}
