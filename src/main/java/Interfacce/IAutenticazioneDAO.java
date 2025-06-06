package Interfacce;

/**
 * Interfaccia per la gestione dell'autenticazione degli utenti.
 */
public interface IAutenticazioneDAO {

    /**
     * Verifica se un utente è autenticato in base a una query e due parametri.
     *
     * @param query la query SQL da eseguire per la verifica
     * @param param1 primo parametro della query
     * @param param2 secondo parametro della query
     * @return true se l'utente è autenticato, false altrimenti
     */
    boolean isAutenticato(String query, String param1, String param2);

    /**
     * Verifica se un utente è autenticato tramite username e credenziali.
     *
     * @param utente username o identificativo dell'utente
     * @param credenziale tipo di credenziale (es. "password", "token", ecc.)
     * @param password valore della password o credenziale da verificare
     * @return true se l'autenticazione ha successo, false altrimenti
     */
    boolean isUtenteAutenticato(String utente, String credenziale, String password);
}
