package Commands;

import Dao.DAOStudente;
import Interfacce.ICommand;
import Interfacce.IStudenteDAO;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Comando per eliminare la prenotazione a un appello d'esame da parte di uno studente.
 * <p>
 * Implementa l'interfaccia {@link ICommand} e utilizza {@link IStudenteDAO}
 * per eseguire l'operazione nel database.
 * </p>
 * <p>
 * Il comando incapsula la matricola dello studente e il numero dell'appello da annullare.
 * </p>
 *
 * @author Emanuele Pacilio
 * @version 1.0
 */
public class CommandEliminaPrenotazione implements ICommand<Void> {

    private IStudenteDAO studenteDAO;
    private String matricola;
    private String numeroAppello;

    /**
     * Costruisce un nuovo comando per eliminare una prenotazione a un appello.
     *
     * @param connection     Connessione al database da utilizzare.
     * @param matricola      Matricola dello studente.
     * @param numeroAppello  Numero identificativo dell'appello da annullare.
     */
    public CommandEliminaPrenotazione(Connection connection, String matricola, String numeroAppello) {
        studenteDAO        = new DAOStudente(connection);
        this.matricola     = matricola;
        this.numeroAppello = numeroAppello;
    }

    /**
     * Esegue il comando per annullare la prenotazione all'appello per lo studente specificato.
     *
     * @return {@code null} (nessun valore di ritorno).
     * @throws SQLException se si verifica un errore durante l'accesso al database.
     */
    @Override
    public Void execute() throws SQLException {
        studenteDAO.eliminaPrenotazione(numeroAppello, matricola);
        return null;
    }
}
