package Interfacce;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Interfaccia per la gestione delle operazioni relative agli studenti,
 * come prenotazione esami, visualizzazione appelli, esiti e libretto.
 */
public interface IStudenteDAO {

    /**
     * Prenota un esame per uno studente identificato dalla matricola.
     *
     * @param numeroAppello il numero dell'appello dell'esame
     * @param matricola     la matricola dello studente
     * @throws SQLException in caso di errori durante l'operazione di prenotazione
     */
    void prenotaEsame(String numeroAppello, String matricola) throws SQLException;

    /**
     * Recupera la lista degli appelli disponibili per un piano di studi e uno studente.
     *
     * @param nomePiano il nome del piano di studi
     * @param matricola la matricola dello studente
     * @return una lista di mappe contenenti i dettagli degli appelli
     * @throws SQLException in caso di errori nella query
     */
    List<Map<String, Object>> getAppelli(String nomePiano, String matricola) throws SQLException;

    /**
     * Recupera gli esiti degli esami sostenuti dallo studente.
     *
     * @param matricola la matricola dello studente
     * @return una lista di mappe contenenti gli esiti degli esami
     * @throws SQLException in caso di errori nella query
     */
    List<Map<String, Object>> getEsiti(String matricola) throws SQLException;

    /**
     * Conferma i voti per uno studente relativo a un determinato appello.
     *
     * @param scelta       la scelta dell'azione o voto da confermare
     * @param matricola    la matricola dello studente
     * @param numeroAppello il numero dell'appello
     * @throws SQLException in caso di errori durante la conferma
     */
    void confermaVoti(String scelta, String matricola, String numeroAppello) throws SQLException;

    /**
     * Recupera il libretto dello studente, ovvero la lista degli esami sostenuti e relativi dettagli.
     *
     * @param matricola la matricola dello studente
     * @return una lista di mappe contenenti le informazioni del libretto
     * @throws SQLException in caso di errori nella query
     */
    List<Map<String, Object>> getLibretto(String matricola) throws SQLException;

    /**
     * Elimina una prenotazione di esame per uno studente.
     *
     * @param numeroAppello il numero dell'appello da eliminare
     * @param matricola     la matricola dello studente
     * @throws SQLException in caso di errori durante l'eliminazione
     */
    void eliminaPrenotazione(String numeroAppello, String matricola) throws SQLException;
}
