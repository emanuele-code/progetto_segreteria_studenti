package Commands;

import Dao.DAODocente;
import Interfacce.ICommand;
import Interfacce.IDocenteDAO;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Comando per ottenere la lista degli appelli associati a un docente specifico.
 * <p>
 * Implementa l'interfaccia {@link ICommand} e utilizza {@link IDocenteDAO}
 * per recuperare i dati dal database.
 * </p>
 * <p>
 * Il comando incapsula il codice fiscale del docente per effettuare la ricerca.
 * </p>
 *
 * @author Emanuele Pacilio
 * @version 1.0
 */
public class CommandGetAppelliPerDocente implements ICommand<List<Map<String, Object>>> {

    private IDocenteDAO docenteDAO;
    private String cf;

    /**
     * Costruisce un nuovo comando per ottenere gli appelli associati a un docente.
     *
     * @param connection Connessione al database da utilizzare.
     * @param cf         Codice fiscale del docente.
     */
    public CommandGetAppelliPerDocente(Connection connection, String cf) {
        docenteDAO = new DAODocente(connection);
        this.cf = cf;
    }

    /**
     * Esegue il comando per recuperare gli appelli associati al docente specificato.
     *
     * @return Una lista di mappe contenenti i dati degli appelli.
     * @throws SQLException se si verifica un errore durante l'accesso al database.
     */
    @Override
    public List<Map<String, Object>> execute() throws SQLException {
        return docenteDAO.getAppelli(cf);
    }
}
