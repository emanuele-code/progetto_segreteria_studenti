package Commands;

import Dao.DAODocente;
import Interfacce.ICommand;
import Interfacce.IDocenteDAO;

import java.sql.Connection;
import java.sql.SQLException;

public class CommandInserisciAppello implements ICommand<Void> {

    private IDocenteDAO docenteDAO;
    private String dataAppello;
    private String codiceEsame;
    private String cf;

    public CommandInserisciAppello(Connection connection, String dataAppello, String codiceEsame, String cf) {
        docenteDAO       = new DAODocente(connection);
        this.dataAppello = dataAppello;
        this.codiceEsame = codiceEsame;
        this.cf          = cf;
    }

    @Override
    public Void execute() throws SQLException {
        docenteDAO.inserisciAppello(dataAppello, codiceEsame, cf);
        return null;
    }
}