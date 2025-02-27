package Commands;
import Dao.DAOSegreteria;
import Interfacce.ICommand;
import Interfacce.ISegreteriaDAO;

import java.sql.Connection;
import java.sql.SQLException;

public class CommandCambiaPianoStudente implements ICommand<Void> {

    private ISegreteriaDAO segreteriaDAO;
    private String matricola;
    private String codiceCorso;

    public CommandCambiaPianoStudente(Connection connection, String matricola, String codiceCorso) {
        segreteriaDAO = new DAOSegreteria(connection);
        this.matricola = matricola;
        this.codiceCorso = codiceCorso;
    }

    @Override
    public Void execute() throws SQLException {
        segreteriaDAO.cambiaPianoStudente(matricola, codiceCorso);
        return null;
    }
}