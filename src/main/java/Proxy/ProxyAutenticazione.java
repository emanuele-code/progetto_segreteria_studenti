package Proxy;

import Dao.DAOAutenticazione;
import Interfacce.IAutenticazioneDAO;
import Utils.UtilHash;

import java.sql.Connection;

/**
 * La classe {@code ProxyAutenticazione} funge da proxy per {@link DAOAutenticazione},
 * ritardando la sua inizializzazione fino al momento dell'effettiva necessità (lazy initialization).
 * <p>
 * Implementa l'interfaccia {@link IAutenticazioneDAO} e consente di aggiungere
 * logica accessoria (es. hashing della password) prima di delegare l'autenticazione
 * all'oggetto DAO effettivo.
 * </p>
 */
public class ProxyAutenticazione implements IAutenticazioneDAO {
    private DAOAutenticazione daoAutenticazione;
    private Connection connection;

    /**
     * Costruisce un nuovo {@code ProxyAutenticazione} utilizzando la connessione al database fornita.
     *
     * @param connection La connessione al database da utilizzare per il DAO.
     */
    public ProxyAutenticazione(Connection connection) {
        this.connection = connection;
    }

    /**
     * Restituisce l'istanza del {@link DAOAutenticazione}, creandola se non esiste ancora.
     *
     * @return L'istanza di {@code DAOAutenticazione}.
     */
    private DAOAutenticazione getDaoAutenticazione() {
        if (daoAutenticazione == null) {
            daoAutenticazione = new DAOAutenticazione(connection);
        }
        return daoAutenticazione;
    }

    /**
     * Verifica se l'utente è autenticato sulla base di una query generica e due parametri.
     *
     * @param query   La query SQL parametrica da eseguire.
     * @param param1  Il primo parametro da inserire nella query.
     * @param param2  Il secondo parametro da inserire nella query.
     * @return {@code true} se l'autenticazione ha successo; {@code false} altrimenti.
     */
    @Override
    public boolean isAutenticato(String query, String param1, String param2) {
        return getDaoAutenticazione().isAutenticato(query, param1, param2);
    }

    /**
     * Verifica se l'utente è autenticato confrontando username, credenziale e password (hashed).
     * La password in chiaro viene automaticamente convertita in hash prima della verifica.
     *
     * @param utente      L'identificativo dell'utente.
     * @param credenziale Il tipo di credenziale (es. email, username).
     * @param password    La password in chiaro fornita dall'utente.
     * @return {@code true} se le credenziali corrispondono; {@code false} altrimenti.
     */
    @Override
    public boolean isUtenteAutenticato(String utente, String credenziale, String password) {
        String hashedPassword = UtilHash.hashPassword(password);
        return getDaoAutenticazione().isUtenteAutenticato(utente, credenziale, hashedPassword);
    }
}
