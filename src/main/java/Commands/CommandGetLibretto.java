package Commands;

import Dao.DAOStudente;
import Interfacce.ICommand;
import Interfacce.IStudenteDAO;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Comando per ottenere il libretto di uno studente, contenente gli esami sostenuti e i voti.
 * <p>
 * Implementa l'interfaccia {@link ICommand} e utilizza {@link IStudenteDAO}
 * per recuperare i dati accademici dello studente dal database.
 * </p>
 * <p>
 * Il comando richiede la matricola dello studente come parametro di input.
 * </p>
 *
 * @author Emanuele Pacilio
 * @version 1.0
 */
public class CommandGetLibretto implements ICommand<List<Map<String, Object>>> {

    private IStudenteDAO studenteDAO;
    private String matricola;

    /**
     * Costruisce un nuovo comando per ottenere il libretto universitario di uno studente.
     *
     * @param connection Connessione al database da utilizzare.
     * @param matricola  Matricola dello studente.
     */
    public CommandGetLibretto(Connection connection, String matricola){
        studenteDAO = new DAOStudente(connection);
        this.matricola = matricola;
    }

    /**
     * Esegue il comando per recuperare il libretto dello studente specificato.
     *
     * @return Una lista di mappe contenenti le informazioni sugli esami e i voti.
     * @throws SQLException se si verifica un errore durante l'accesso al database.
     */
    @Override
    public List<Map<String, Object>> execute() throws SQLException {
        return studenteDAO.getLibretto(matricola);
    }
}
