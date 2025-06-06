package Interfacce;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Interfaccia per le operazioni DAO relative ai docenti.
 */
public interface IDocenteDAO {

    /**
     * Inserisce un voto per uno studente in un appello specifico.
     *
     * @param cf            codice fiscale del docente che inserisce il voto
     * @param voto          voto da inserire
     * @param numeroAppello identificativo dell'appello
     * @throws SQLException in caso di errore durante l'accesso al database
     */
    void inserisciVoto(String cf, String voto, String numeroAppello) throws SQLException;

    /**
     * Inserisce un nuovo appello per un esame specifico.
     *
     * @param dataAppello   data dell'appello
     * @param codiceEsame   codice dell'esame relativo all'appello
     * @param cf            codice fiscale del docente che crea l'appello
     * @throws SQLException in caso di errore durante l'accesso al database
     */
    void inserisciAppello(String dataAppello, String codiceEsame, String cf) throws SQLException;

    /**
     * Ottiene la mappa degli esami associati a un docente.
     *
     * @param cf codice fiscale del docente
     * @return mappa contenente codice esame e relativo nome
     * @throws SQLException in caso di errore durante l'accesso al database
     */
    Map<String, String> getEsami(String cf) throws SQLException;

    /**
     * Chiude la prenotazione per un appello specifico.
     *
     * @param codiceAppello identificativo dell'appello
     * @param connection    connessione al database da utilizzare
     * @throws SQLException in caso di errore durante l'accesso al database
     */
    void chiudiPrenotazione(String codiceAppello, Connection connection) throws SQLException;

    /**
     * Recupera la lista degli appelli di un docente.
     *
     * @param cf codice fiscale del docente
     * @return lista di mappe contenenti i dati degli appelli
     * @throws SQLException in caso di errore durante l'accesso al database
     */
    List<Map<String, Object>> getAppelli(String cf) throws SQLException;

    /**
     * Recupera la lista delle prenotazioni per gli appelli di un docente.
     *
     * @param cf codice fiscale del docente
     * @return lista di mappe contenenti i dati delle prenotazioni
     * @throws SQLException in caso di errore durante l'accesso al database
     */
    List<Map<String, Object>> getPrenotazioni(String cf) throws SQLException;
}
