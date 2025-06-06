package Commands;

import Dao.DAOStudente;
import Interfacce.ICommand;
import Interfacce.IStudenteDAO;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Comando per ottenere gli esiti degli esami sostenuti da uno studente.
 * <p>
 * Implementa l'interfaccia {@link ICommand} e utilizza {@link IStudenteDAO}
 * per interrogare il database e recuperare i dati relativi agli esiti.
 * </p>
 * <p>
 * Il comando utilizza la matricola dello studente per filtrare i risultati.
 * </p>
 *
 * @author Emanuele Pacilio
 * @version 1.0
 */
public class CommandGetEsiti implements ICommand<List<Map<String, Object>>> {

    private IStudenteDAO studenteDAO;
    private String matricola;

    /**
     * Costruisce un nuovo comando per ottenere gli esiti degli esami di uno studente.
     *
     * @param connection Connessione al database da utilizzare.
     * @param matricola  Matricola dello studente.
     */
    public CommandGetEsiti(Connection connection, String matricola) {
        studenteDAO = new DAOStudente(connection);
        this.matricola = matricola;
    }

    /**
     * Esegue il comando per recuperare gli esiti degli esami dello studente specificato.
     *
     * @return Una lista di mappe contenenti i dati relativi agli esiti.
     * @throws SQLException se si verifica un errore durante l'accesso al database.
     */
    @Override
    public List<Map<String, Object>> execute() throws SQLException {
        return studenteDAO.getEsiti(matricola);
    }
}
