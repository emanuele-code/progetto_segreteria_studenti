package Commands;

import Dao.DAOSegreteria;
import Interfacce.ICommand;
import Interfacce.ISegreteriaDAO;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

public class CommandGetPianiStudio implements ICommand<Map<Integer, String>> {
    private ISegreteriaDAO segreteriaDAO;

    public CommandGetPianiStudio(Connection connection) {
        segreteriaDAO = new DAOSegreteria(connection);
    }

    @Override
    public Map<Integer, String> execute() throws SQLException {
        return segreteriaDAO.getPianiDiStudio();
    }
}
