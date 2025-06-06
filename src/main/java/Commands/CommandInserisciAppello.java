package Commands;

import Dao.DAODocente;
import Interfacce.ICommand;
import Interfacce.IDocenteDAO;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Comando per inserire un nuovo appello d'esame nel sistema.
 * <p>
 * Implementa l'interfaccia {@link ICommand} e utilizza {@link IDocenteDAO}
 * per effettuare l'inserimento tramite il layer DAO del docente.
 * </p>
 * <p>
 * Il comando richiede la data dell'appello, il codice dell'esame e il codice fiscale del docente.
 * </p>
 *
 * @author Emanuele Pacilio
 * @version 1.0
 */
public class CommandInserisciAppello implements ICommand<Void> {

    private IDocenteDAO docenteDAO;
    private String dataAppello;
    private String codiceEsame;
    private String cf;

    /**
     * Costruisce un nuovo comando per inserire un appello d'esame.
     *
     * @param connection   Connessione al database da utilizzare.
     * @param dataAppello  Data dell'appello in formato stringa (es. "2025-06-15").
     * @param codiceEsame  Codice identificativo dell'esame.
     * @param cf           Codice fiscale del docente responsabile.
     */
    public CommandInserisciAppello(Connection connection, String dataAppello, String codiceEsame, String cf) {
        docenteDAO       = new DAODocente(connection);
        this.dataAppello = dataAppello;
        this.codiceEsame = codiceEsame;
        this.cf          = cf;
    }

    /**
     * Esegue il comando per inserire l'appello nel sistema.
     *
     * @return null, poiché il comando non restituisce un valore.
     * @throws SQLException se si verifica un errore durante l'inserimento nel database.
     */
    @Override
    public Void execute() throws SQLException {
        docenteDAO.inserisciAppello(dataAppello, codiceEsame, cf);
        return null;
    }
}
