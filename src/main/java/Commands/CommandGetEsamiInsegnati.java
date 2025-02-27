package Commands;

import Dao.DAODocente;
import Interfacce.ICommand;
import Interfacce.IDocenteDAO;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

public class CommandGetEsamiInsegnati implements ICommand<Map<String, String>> {
    private IDocenteDAO docenteDAO;
    private String cf;

    public CommandGetEsamiInsegnati(Connection connection, String cf) {
        docenteDAO = new DAODocente(connection);
        this.cf = cf;
    }

    @Override
    public Map<String, String> execute() throws SQLException {
        return docenteDAO.getEsami(cf);
    }
}
