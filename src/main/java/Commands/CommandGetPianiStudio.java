package Commands;

import Dao.DAOSegreteria;
import Interfacce.ICommand;
import Interfacce.ISegreteriaDAO;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

/**
 * Comando per ottenere l'elenco dei piani di studio disponibili.
 * <p>
 * Implementa l'interfaccia {@link ICommand} e utilizza {@link ISegreteriaDAO}
 * per recuperare i dati dal database tramite il layer di accesso della segreteria.
 * </p>
 *
 * L'elenco restituito contiene una mappa con l'ID del piano di studio come chiave
 * e il relativo nome come valore.
 * </p>
 *
 * @author Emanuele Pacilio
 * @version 1.0
 */
public class CommandGetPianiStudio implements ICommand<Map<Integer, String>> {

    private ISegreteriaDAO segreteriaDAO;

    /**
     * Costruisce un nuovo comando per ottenere i piani di studio disponibili.
     *
     * @param connection Connessione al database da utilizzare.
     */
    public CommandGetPianiStudio(Connection connection) {
        segreteriaDAO = new DAOSegreteria(connection);
    }

    /**
     * Esegue il comando per recuperare i piani di studio dal database.
     *
     * @return Una mappa contenente gli ID e i nomi dei piani di studio.
     * @throws SQLException se si verifica un errore durante l'accesso al database.
     */
    @Override
    public Map<Integer, String> execute() throws SQLException {
        return segreteriaDAO.getPianiDiStudio();
    }
}
