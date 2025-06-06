package Commands;

import Dao.DAOSegreteria;
import Interfacce.ICommand;
import Interfacce.ISegreteriaDAO;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Comando per inserire un nuovo studente nel sistema.
 * <p>
 * Implementa l'interfaccia {@link ICommand} e utilizza {@link ISegreteriaDAO}
 * per effettuare l'inserimento tramite il layer DAO della segreteria.
 * </p>
 * <p>
 * Le credenziali dello studente sono fornite come array di stringhe, contenente
 * le informazioni necessarie all'inserimento (es. nome, cognome, matricola, ecc.).
 * </p>
 *
 * @author Emanuele Pacilio
 * @version 1.0
 */
public class CommandInserisciStudente implements ICommand<Void> {

    private ISegreteriaDAO segreteriaDAO;
    private String[] credenziali;

    /**
     * Costruisce un nuovo comando per inserire uno studente.
     *
     * @param connection   Connessione al database da utilizzare.
     * @param credenziali  Array contenente le credenziali e le informazioni dello studente.
     */
    public CommandInserisciStudente(Connection connection, String[] credenziali) {
        segreteriaDAO = new DAOSegreteria(connection);
        this.credenziali = credenziali;
    }

    /**
     * Esegue il comando per inserire lo studente nel sistema.
     *
     * @return null, poiché il comando non restituisce un valore.
     * @throws SQLException se si verifica un errore durante l'inserimento nel database.
     */
    @Override
    public Void execute() throws SQLException {
        segreteriaDAO.inserisciStudente(credenziali);
        return null;
    }
}
