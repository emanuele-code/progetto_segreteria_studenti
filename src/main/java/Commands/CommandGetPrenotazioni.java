package Commands;

import Dao.DAODocente;
import Interfacce.ICommand;
import Interfacce.IDocenteDAO;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Comando per ottenere l'elenco delle prenotazioni effettuate per gli appelli di un docente.
 * <p>
 * Implementa l'interfaccia {@link ICommand} e utilizza {@link IDocenteDAO}
 * per accedere al database e recuperare i dati delle prenotazioni relative agli esami gestiti dal docente.
 * </p>
 * <p>
 * Il comando utilizza il codice fiscale del docente come parametro per filtrare le prenotazioni.
 * </p>
 *
 * @author Emanuele Pacilio
 * @version 1.0
 */
public class CommandGetPrenotazioni implements ICommand<List<Map<String, Object>>> {

    private IDocenteDAO docenteDAO;
    private String cf;

    /**
     * Costruisce un nuovo comando per ottenere le prenotazioni associate agli appelli di un docente.
     *
     * @param connection Connessione al database da utilizzare.
     * @param cf         Codice fiscale del docente.
     */
    public CommandGetPrenotazioni(Connection connection, String cf){
        docenteDAO = new DAODocente(connection);
        this.cf    = cf;
    }

    /**
     * Esegue il comando per recuperare le prenotazioni per gli appelli del docente specificato.
     *
     * @return Una lista di mappe contenenti le informazioni sulle prenotazioni.
     * @throws SQLException se si verifica un errore durante l'accesso al database.
     */
    @Override
    public List<Map<String, Object>> execute() throws SQLException {
        return docenteDAO.getPrenotazioni(cf);
    }
}
