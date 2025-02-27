package Commands;

import Dao.DAOSegreteria;
import Interfacce.ICommand;
import Interfacce.ISegreteriaDAO;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class CommandGetVotiDaConfermare implements ICommand<List<Map<String, Object>>> {
    private ISegreteriaDAO segreteriaDAO;

    public CommandGetVotiDaConfermare(Connection connection) {
        segreteriaDAO = new DAOSegreteria(connection);
    }

    @Override
    public List<Map<String, Object>> execute() throws SQLException {
        return segreteriaDAO.getVotiDaConfermare();
    }
}
