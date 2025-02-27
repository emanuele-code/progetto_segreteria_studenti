package Commands;

import Dao.DAODocente;
import Interfacce.ICommand;
import Interfacce.IDocenteDAO;

import java.sql.Connection;
import java.sql.SQLException;

public class CommandChiudiPrenotazione implements ICommand<Void> {

    private IDocenteDAO docenteDAO;
    private Connection connection;
    private String codiceEsame;

    public CommandChiudiPrenotazione(String codiceEsame, Connection connection) {
        docenteDAO = new DAODocente(connection);
        this.connection = connection;
        this.codiceEsame = codiceEsame;
    }

    @Override
    public Void execute() throws SQLException {
        docenteDAO.chiudiPrenotazione(codiceEsame, connection);
        return null;
    }
}