package Commands;

import Dao.DAOSegreteria;
import Interfacce.ICommand;
import Interfacce.ISegreteriaDAO;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Comando per ottenere l'elenco dei voti che devono ancora essere confermati dalla segreteria.
 * <p>
 * Implementa l'interfaccia {@link ICommand} e utilizza {@link ISegreteriaDAO}
 * per accedere al database e recuperare le informazioni sui voti da confermare.
 * </p>
 *
 * @author Emanuele Pacilio
 * @version 1.0
 */
public class CommandGetVotiDaConfermare implements ICommand<List<Map<String, Object>>> {

    private ISegreteriaDAO segreteriaDAO;

    /**
     * Costruisce un nuovo comando per ottenere i voti che richiedono conferma da parte della segreteria.
     *
     * @param connection Connessione al database da utilizzare.
     */
    public CommandGetVotiDaConfermare(Connection connection) {
        segreteriaDAO = new DAOSegreteria(connection);
    }

    /**
     * Esegue il comando per recuperare la lista dei voti non ancora confermati.
     *
     * @return Una lista di mappe contenenti i dettagli dei voti da confermare.
     * @throws SQLException se si verifica un errore durante l'accesso al database.
     */
    @Override
    public List<Map<String, Object>> execute() throws SQLException {
        return segreteriaDAO.getVotiDaConfermare();
    }
}
