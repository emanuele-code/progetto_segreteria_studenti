package Commands;

import Dao.DAODocente;
import Interfacce.ICommand;
import Interfacce.IDocenteDAO;

import java.sql.Connection;
import java.sql.SQLException;

public class CommandInserisciVoto implements ICommand<Void> {

    private IDocenteDAO docenteDAO;
    private String voto;
    private String matricola;
    private String numeroAppello;

    public CommandInserisciVoto(Connection connection, String voto, String matricola, String numeroAppello) {
        docenteDAO         = new DAODocente(connection);
        this.voto          = voto;
        this.matricola     = matricola;
        this.numeroAppello = numeroAppello;
    }

    @Override
    public Void execute() throws SQLException {
        docenteDAO.inserisciVoto(matricola, voto, numeroAppello);
        return null;
    }
}