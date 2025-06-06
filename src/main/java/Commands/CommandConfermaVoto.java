package Commands;

import Dao.DAOSegreteria;
import Interfacce.ICommand;
import Interfacce.ISegreteriaDAO;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Comando per confermare ufficialmente un voto d'esame per uno studente.
 * <p>
 * Implementa l'interfaccia {@link ICommand} e utilizza {@link ISegreteriaDAO}
 * per effettuare l'aggiornamento nel database.
 * </p>
 * <p>
 * Il comando incapsula la matricola dello studente e il numero dell'appello
 * per il quale deve essere confermato il voto.
 * </p>
 *
 * @author Emanuele Pacilio
 * @version 1.0
 */
public class CommandConfermaVoto implements ICommand<Void> {

    private ISegreteriaDAO segreteriaDAO;
    private String matricola;
    private String numeroAppello;

    /**
     * Costruisce un nuovo comando per confermare un voto d'esame per uno studente.
     *
     * @param connection     Connessione al database da utilizzare.
     * @param matricola      Matricola dello studente.
     * @param numeroAppello  Numero identificativo dell'appello.
     */
    public CommandConfermaVoto(Connection connection, String matricola, String numeroAppello) {
        segreteriaDAO = new DAOSegreteria(connection);
        this.matricola = matricola;
        this.numeroAppello = numeroAppello;
    }

    /**
     * Esegue il comando per confermare il voto dell'appello specificato per lo studente.
     *
     * @return {@code null} (nessun valore di ritorno).
     * @throws SQLException se si verifica un errore durante l'accesso al database.
     */
    @Override
    public Void execute() throws SQLException {
        segreteriaDAO.confermaVoto(matricola, numeroAppello);
        return null;
    }
}
