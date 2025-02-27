package Commands;

import Dao.DAOStudente;
import Interfacce.ICommand;
import Interfacce.IStudenteDAO;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class CommandGetLibretto implements ICommand<List<Map<String, Object>>> {
    private IStudenteDAO studenteDAO;
    private String matricola;

    public CommandGetLibretto(Connection connection, String matricola){
        studenteDAO = new DAOStudente(connection);
        this.matricola = matricola;
    }

    @Override
    public List<Map<String, Object>> execute() throws SQLException {
        return studenteDAO.getLibretto(matricola);
    }
}
