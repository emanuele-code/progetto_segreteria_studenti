package Commands;

import Dao.DAODocente;
import Interfacce.ICommand;
import Interfacce.IDocenteDAO;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Comando per inserire un voto per uno studente in un appello d'esame.
 * <p>
 * Implementa l'interfaccia {@link ICommand} e utilizza {@link IDocenteDAO}
 * per effettuare l'inserimento tramite il layer DAO del docente.
 * </p>
 * <p>
 * Richiede il voto da inserire, la matricola dello studente e il numero dell'appello.
 * </p>
 *
 * @author Emanuele Pacilio
 * @version 1.0
 */
public class CommandInserisciVoto implements ICommand<Void> {

    private IDocenteDAO docenteDAO;
    private String voto;
    private String matricola;
    private String numeroAppello;

    /**
     * Costruisce un nuovo comando per inserire un voto.
     *
     * @param connection    Connessione al database da utilizzare.
     * @param voto          Voto da inserire (es. "28", "30L", ecc.).
     * @param matricola     Matricola dello studente a cui associare il voto.
     * @param numeroAppello Numero identificativo dell'appello d'esame.
     */
    public CommandInserisciVoto(Connection connection, String voto, String matricola, String numeroAppello) {
        docenteDAO         = new DAODocente(connection);
        this.voto          = voto;
        this.matricola     = matricola;
        this.numeroAppello = numeroAppello;
    }

    /**
     * Esegue il comando per inserire il voto nel sistema.
     *
     * @return null, poiché il comando non restituisce un valore.
     * @throws SQLException se si verifica un errore durante l'inserimento nel database.
     */
    @Override
    public Void execute() throws SQLException {
        docenteDAO.inserisciVoto(matricola, voto, numeroAppello);
        return null;
    }
}
