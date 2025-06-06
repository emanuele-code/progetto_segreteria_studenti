package Commands;

import Dao.DAOStudente;
import Interfacce.ICommand;
import Interfacce.IStudenteDAO;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Comando per prenotare uno studente a un appello d'esame.
 * <p>
 * Implementa l'interfaccia {@link ICommand} e utilizza {@link IStudenteDAO}
 * per effettuare la prenotazione tramite il layer DAO dello studente.
 * </p>
 * <p>
 * Richiede il numero dell'appello e la matricola dello studente.
 * </p>
 *
 * @author Emanuele Pacilio
 * @version 1.0
 */
public class CommandPrenotaEsame implements ICommand<Void> {

    private IStudenteDAO studenteDAO;
    private String numeroAppello;
    private String matricola;

    /**
     * Costruisce un nuovo comando per prenotare uno studente ad un appello.
     *
     * @param connection    Connessione al database da utilizzare.
     * @param numeroAppello Numero identificativo dell'appello d'esame.
     * @param matricola     Matricola dello studente che effettua la prenotazione.
     */
    public CommandPrenotaEsame(Connection connection, String numeroAppello, String matricola) {
        studenteDAO        = new DAOStudente(connection);
        this.numeroAppello = numeroAppello;
        this.matricola     = matricola;
    }

    /**
     * Esegue il comando per prenotare lo studente all'appello.
     *
     * @return null, poiché il comando non restituisce un valore.
     * @throws SQLException se si verifica un errore durante la prenotazione nel database.
     */
    @Override
    public Void execute() throws SQLException {
        studenteDAO.prenotaEsame(this.numeroAppello, this.matricola);
        return null;
    }
}
