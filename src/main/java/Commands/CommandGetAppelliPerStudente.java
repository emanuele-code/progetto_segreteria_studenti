package Commands;

import Dao.DAOStudente;
import Interfacce.ICommand;
import Interfacce.IStudenteDAO;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class CommandGetAppelliPerStudente implements ICommand<List<Map<String, Object>>> {
    private IStudenteDAO studenteDAO;
    private String nomePiano;

    public CommandGetAppelliPerStudente(Connection connection, String nomePiano) {
        studenteDAO = new DAOStudente(connection);
        this.nomePiano = nomePiano;
    }

    @Override
    public List<Map<String, Object>> execute() throws SQLException {
        return studenteDAO.getAppelli(nomePiano);
    }
}
