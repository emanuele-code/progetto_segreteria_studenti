package Commands;

import Dao.DAOSegreteria;
import Interfacce.ICommand;
import Interfacce.ISegreteriaDAO;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Comando per modificare il piano di studi di uno studente specifico.
 * <p>
 * Implementa l'interfaccia {@link ICommand} e utilizza {@link ISegreteriaDAO}
 * per eseguire l'operazione a livello di database.
 * </p>
 * <p>
 * Il comando incapsula le informazioni necessarie (matricola e codice del corso)
 * e delega l'operazione all'oggetto DAO.
 * </p>
 *
 * @author Emanuele Pacilio
 * @version 1.0
 */
public class CommandCambiaPianoStudente implements ICommand<Void> {

    private ISegreteriaDAO segreteriaDAO;
    private String matricola;
    private String codiceCorso;

    /**
     * Costruisce un nuovo comando per cambiare il piano di studi di uno studente.
     *
     * @param connection   Connessione al database.
     * @param matricola    Matricola dello studente.
     * @param codiceCorso  Codice del nuovo corso da inserire nel piano.
     */
    public CommandCambiaPianoStudente(Connection connection, String matricola, String codiceCorso) {
        segreteriaDAO = new DAOSegreteria(connection);
        this.matricola = matricola;
        this.codiceCorso = codiceCorso;
    }

    /**
     * Esegue il comando che modifica il piano di studi dello studente nel database.
     *
     * @return {@code null} (nessun valore di ritorno).
     * @throws SQLException se si verifica un errore durante l'accesso al database.
     */
    @Override
    public Void execute() throws SQLException {
        segreteriaDAO.cambiaPianoStudente(matricola, codiceCorso);
        return null;
    }
}
