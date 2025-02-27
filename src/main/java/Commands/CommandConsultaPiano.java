package Commands;

import Dao.DAOStudente;
import Interfacce.ICommand;
import Interfacce.IStudenteDAO;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class CommandConsultaPiano implements ICommand<Void> {

    private IStudenteDAO studenteDAO;
    private String matricola;

    CommandConsultaPiano(Connection connection, String matricola) {
        studenteDAO = new DAOStudente(connection);
        this.matricola = matricola;
    }

    @Override
    public Void execute() throws SQLException {
        Map<String, Object> datiStudente = studenteDAO.consultaPiano(this.matricola);
        return null;
    }
}