package Interfacce;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Interfaccia per le operazioni della segreteria universitaria sul database.
 */
public interface ISegreteriaDAO {

    /**
     * Inserisce un nuovo studente nel database utilizzando un array di dati.
     *
     * @param dati array di stringhe contenente i dati dello studente da inserire
     * @throws SQLException in caso di errori durante l'inserimento nel database
     */
    void inserisciStudente(String[] dati) throws SQLException;

    /**
     * Cambia il piano di studi di uno studente.
     *
     * @param matricola matricola dello studente
     * @param codiceCorso codice del nuovo corso di studi
     * @throws SQLException in caso di errori durante l'aggiornamento nel database
     */
    void cambiaPianoStudente(String matricola, String codiceCorso) throws SQLException;

    /**
     * Conferma il voto di uno studente per un dato numero di appello.
     *
     * @param matricola matricola dello studente
     * @param numeroAppello numero dell'appello da confermare
     * @throws SQLException in caso di errori durante l'operazione
     */
    void confermaVoto(String matricola, String numeroAppello) throws SQLException;

    /**
     * Recupera tutti i piani di studio disponibili.
     *
     * @return una mappa con chiave intera (ID piano) e valore stringa (nome piano di studio)
     * @throws SQLException in caso di errori durante la query
     */
    Map<Integer, String> getPianiDiStudio() throws SQLException;

    /**
     * Recupera la lista dei voti che devono ancora essere confermati.
     *
     * @return una lista di mappe rappresentanti i voti da confermare con le relative informazioni
     * @throws SQLException in caso di errori durante la query
     */
    List<Map<String, Object>> getVotiDaConfermare() throws SQLException;
}
