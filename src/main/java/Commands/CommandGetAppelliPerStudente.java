package Commands;

import Dao.DAOStudente;
import Interfacce.ICommand;
import Interfacce.IStudenteDAO;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Comando per ottenere la lista degli appelli disponibili per uno studente
 * in base al piano di studi selezionato.
 * <p>
 * Implementa l'interfaccia {@link ICommand} e utilizza {@link IStudenteDAO}
 * per interrogare il database e ottenere i risultati.
 * </p>
 * <p>
 * Il comando richiede il nome del piano di studi e la matricola dello studente.
 * </p>
 *
 * @author Emanuele Pacilio
 * @version 1.0
 */
public class CommandGetAppelliPerStudente implements ICommand<List<Map<String, Object>>> {

    private IStudenteDAO studenteDAO;
    private String nomePiano;
    private String matricola;

    /**
     * Costruisce un nuovo comando per ottenere gli appelli relativi a uno studente.
     *
     * @param connection Connessione al database da utilizzare.
     * @param nomePiano  Nome del piano di studi dello studente.
     * @param matricola  Matricola dello studente.
     */
    public CommandGetAppelliPerStudente(Connection connection, String nomePiano, String matricola) {
        studenteDAO = new DAOStudente(connection);
        this.nomePiano = nomePiano;
        this.matricola = matricola;
    }

    /**
     * Esegue il comando per recuperare gli appelli disponibili per lo studente.
     *
     * @return Una lista di mappe contenenti i dati degli appelli.
     * @throws SQLException se si verifica un errore durante l'accesso al database.
     */
    @Override
    public List<Map<String, Object>> execute() throws SQLException {
        return studenteDAO.getAppelli(nomePiano, matricola);
    }
}
