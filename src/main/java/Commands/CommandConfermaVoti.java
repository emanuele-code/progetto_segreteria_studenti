package Commands;

import Dao.DAOStudente;
import Interfacce.ICommand;
import Interfacce.IStudenteDAO;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Comando per confermare o rifiutare i voti di un appello da parte di uno studente.
 * <p>
 * Implementa l'interfaccia {@link ICommand} e utilizza {@link IStudenteDAO}
 * per eseguire l'operazione nel database.
 * </p>
 * <p>
 * Il comando incapsula la scelta dello studente (accetta o rifiuta), la matricola
 * e il numero dell'appello da aggiornare.
 * </p>
 *
 * @author Emanuele Pacilio
 * @version 1.0
 */
public class CommandConfermaVoti implements ICommand<Void> {

    private IStudenteDAO studenteDAO;
    private String scelta;
    private String matricola;
    private String numeroAppello;

    /**
     * Costruisce un nuovo comando per confermare o rifiutare i voti di un appello.
     *
     * @param connection     Connessione al database da utilizzare.
     * @param scelta         Scelta dello studente (es. "accetta" o "rifiuta").
     * @param matricola      Matricola dello studente.
     * @param numeroAppello  Identificativo dell'appello.
     */
    public CommandConfermaVoti(Connection connection, String scelta, String matricola, String numeroAppello) {
        studenteDAO        = new DAOStudente(connection);
        this.scelta        = scelta;
        this.matricola     = matricola;
        this.numeroAppello = numeroAppello;
    }

    /**
     * Esegue il comando per registrare la scelta dello studente riguardo ai voti dell'appello.
     *
     * @return {@code null} (nessun valore di ritorno).
     * @throws SQLException se si verifica un errore durante l'accesso al database.
     */
    @Override
    public Void execute() throws SQLException {
        studenteDAO.confermaVoti(scelta, matricola, numeroAppello);
        return null;
    }
}
