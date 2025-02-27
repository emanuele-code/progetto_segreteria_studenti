package Commands;

import Dao.DAODocente;
import Interfacce.ICommand;
import Interfacce.IDocenteDAO;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class CommandGetAppelliPerDocente implements ICommand<List<Map<String, Object>>> {
    private IDocenteDAO docenteDAO;
    private String cf;

    public CommandGetAppelliPerDocente(Connection connection, String cf) {
        docenteDAO = new DAODocente(connection);
        this.cf = cf;
    }

    @Override
    public List<Map<String, Object>> execute() throws SQLException {
        return docenteDAO.getAppelli(cf);
    }
}
