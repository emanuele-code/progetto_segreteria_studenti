package Commands;

import Dao.DAODocente;
import Interfacce.ICommand;
import Interfacce.IDocenteDAO;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

/**
 * Comando per ottenere la lista degli esami insegnati da un docente.
 * <p>
 * Implementa l'interfaccia {@link ICommand} e utilizza {@link IDocenteDAO}
 * per recuperare i dati relativi agli insegnamenti di un docente dal database.
 * </p>
 * <p>
 * Il comando richiede il codice fiscale del docente per eseguire la ricerca.
 * </p>
 *
 * @author Emanuele Pacilio
 * @version 1.0
 */
public class CommandGetEsamiInsegnati implements ICommand<Map<String, String>> {

    private IDocenteDAO docenteDAO;
    private String cf;

    /**
     * Costruisce un nuovo comando per ottenere gli esami insegnati da un docente.
     *
     * @param connection Connessione al database da utilizzare.
     * @param cf         Codice fiscale del docente.
     */
    public CommandGetEsamiInsegnati(Connection connection, String cf) {
        docenteDAO = new DAODocente(connection);
        this.cf = cf;
    }

    /**
     * Esegue il comando per recuperare gli esami insegnati dal docente specificato.
     *
     * @return Una mappa contenente i codici e i nomi degli esami.
     * @throws SQLException se si verifica un errore durante l'accesso al database.
     */
    @Override
    public Map<String, String> execute() throws SQLException {
        return docenteDAO.getEsami(cf);
    }
}
