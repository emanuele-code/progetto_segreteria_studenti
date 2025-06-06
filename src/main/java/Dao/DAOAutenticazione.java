package Dao;

import Interfacce.IAutenticazioneDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Implementazione dell'interfaccia {@link IAutenticazioneDAO} per la gestione dell'autenticazione
 * di studenti, docenti e della segreteria.
 */
public class DAOAutenticazione implements IAutenticazioneDAO {

    /**
     * Hash della password della segreteria.
     */
    private final String SEGRETERIA_HASHED_PASSWORD = "bf629c9d8a2c2369aad28d9bb563c66f3370cc70cf7a4a72f6ddd39f17fbf227";

    /**
     * Email associata alla segreteria.
     */
    private final String SEGRETERIA_EMAIL = "segreteria@esempio.com";

    /**
     * Connessione al database utilizzata per eseguire le query.
     */
    private Connection connection;

    /**
     * Costruttore che inizializza la connessione al database.
     *
     * @param connection connessione al database
     */
    public DAOAutenticazione(Connection connection) {
        this.connection = connection;
    }

    /**
     * Verifica se un utente è autenticato in base a una query SQL personalizzata e due parametri.
     *
     * @param query   la query SQL da eseguire
     * @param param1  primo parametro (es. username o identificativo)
     * @param param2  secondo parametro (es. password)
     * @return true se l'utente esiste nel database, false altrimenti
     */
    @Override
    public boolean isAutenticato(String query, String param1, String param2) {
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, param1);
            stmt.setString(2, param2);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Verifica l'autenticazione dell'utente in base al tipo (studente, docente, segreteria).
     *
     * @param utente      tipo di utente ("studente", "docente", "segreteria")
     * @param credenziale identificativo dell'utente (matricola, CF o email)
     * @param password    password o hash della password
     * @return true se le credenziali sono valide, false altrimenti
     */
    @Override
    public boolean isUtenteAutenticato(String utente, String credenziale, String password) {
        String query = "";
        switch (utente) {
            case "studente":
                query = "SELECT * FROM STUDENTE WHERE matricola = ? AND password = ?";
                break;
            case "docente":
                query = "SELECT * FROM DOCENTE WHERE cf = ? AND password = ?";
                break;
            case "segreteria":
                return SEGRETERIA_EMAIL.equals(credenziale) && SEGRETERIA_HASHED_PASSWORD.equals(password);
        }
        return isAutenticato(query, credenziale, password);
    }
}
