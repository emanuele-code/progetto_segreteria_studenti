package Commands;

import Dao.DAODocente;
import Interfacce.ICommand;
import Interfacce.IDocenteDAO;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Comando per chiudere la prenotazione a un esame specifico.
 * <p>
 * Implementa l'interfaccia {@link ICommand} e utilizza {@link IDocenteDAO}
 * per eseguire l'operazione nel database.
 * </p>
 * <p>
 * Il comando incapsula il codice dell'esame e la connessione, e
 * delega l'operazione al DAO del docente.
 * </p>
 *
 * @author Emanuele Pacilio
 * @version 1.0
 */
public class CommandChiudiPrenotazione implements ICommand<Void> {

    private IDocenteDAO docenteDAO;
    private Connection connection;
    private String codiceEsame;

    /**
     * Costruisce un nuovo comando per chiudere la prenotazione di un esame.
     *
     * @param codiceEsame Il codice dell'esame di cui chiudere le prenotazioni.
     * @param connection  La connessione al database da utilizzare.
     */
    public CommandChiudiPrenotazione(String codiceEsame, Connection connection) {
        docenteDAO = new DAODocente(connection);
        this.connection = connection;
        this.codiceEsame = codiceEsame;
    }

    /**
     * Esegue il comando per chiudere la prenotazione all'esame specificato.
     *
     * @return {@code null} (nessun valore di ritorno).
     * @throws SQLException se si verifica un errore durante l'accesso al database.
     */
    @Override
    public Void execute() throws SQLException {
        docenteDAO.chiudiPrenotazione(codiceEsame, connection);
        return null;
    }
}
