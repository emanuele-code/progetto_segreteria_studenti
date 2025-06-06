package Dao;

import Interfacce.IDocenteDAO;
import Utils.UtilAlert;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Classe DAO per la gestione delle operazioni del docente nel database.
 * Implementa {@link IDocenteDAO}.
 */
public class DAODocente implements IDocenteDAO {
    private final Connection connection;

    /**
     * Costruttore che inizializza la connessione al database.
     * @param connection connessione al database
     */
    public DAODocente(Connection connection){
        this.connection = connection;
    }

    /**
     * Inserisce o aggiorna il voto di uno studente per un appello specifico.
     * @param matricola      la matricola dello studente
     * @param voto           il voto assegnato
     * @param numeroAppello  l'identificativo dell'appello
     * @throws SQLException in caso di errore nell'accesso al database
     */
    public void inserisciVoto(String matricola, String voto, String numeroAppello) throws SQLException {
        String query = "UPDATE PRENOTAZIONE SET voto = ?, stato = 'voto_inserito' WHERE numero_appello = ? AND matricola = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, voto);
            statement.setString(2, numeroAppello);
            statement.setString(3, matricola);
            statement.executeUpdate();
        }
    }

    /**
     * Recupera i dati anagrafici di un docente tramite il codice fiscale.
     * @param cf          codice fiscale del docente
     * @param connection  connessione al database
     * @return mappa contenente nome, cognome ed email del docente
     * @throws SQLException in caso di errore nell'accesso al database
     */
    public static Map<String, String> getDatiDocente(String cf, Connection connection) throws SQLException {
        String query = "SELECT cf, nome_docente, cognome_docente, email FROM DOCENTE WHERE cf = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, cf);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    Map<String, String> datiDocente = new HashMap<>();
                    datiDocente.put("nome", rs.getString("nome_docente"));
                    datiDocente.put("cognome", rs.getString("cognome_docente"));
                    datiDocente.put("email", rs.getString("email"));
                    return datiDocente;
                }
            }
        }
        return null;
    }

    /**
     * Inserisce un nuovo appello per un esame associato a un docente.
     * @param dataAppello  data dell'appello in formato yyyy-MM-dd
     * @param codiceEsame  codice dell'esame
     * @param cf           codice fiscale del docente
     * @throws SQLException in caso di errore nell'accesso al database
     */
    public void inserisciAppello(String dataAppello, String codiceEsame, String cf) throws SQLException {

        String query = "INSERT INTO APPELLO (data_appello, codice_esame, cf) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, dataAppello);
            statement.setString(2, codiceEsame);
            statement.setString(3, cf);
            statement.executeUpdate();
        }
    }

    /**
     * Disattiva la prenotazione per un appello, rendendolo non disponibile.
     * @param numeroAppello identificativo dell'appello
     * @param connection     connessione al database
     * @throws SQLException in caso di errore
     */
    public void chiudiPrenotazione(String numeroAppello, Connection connection) throws SQLException {
        String query = "UPDATE APPELLO SET disponibile = 0 WHERE numero_appello = ?";
        try (PreparedStatement updateStatement = connection.prepareStatement(query)) {
            updateStatement.setString(1, numeroAppello);
            updateStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Restituisce gli esami insegnati da un docente.
     * @param cf codice fiscale del docente
     * @return mappa con codice esame come chiave e nome esame come valore
     * @throws SQLException in caso di errore
     */
    public Map<String, String> getEsami(String cf) throws SQLException {
        Map<String, String> esami = new HashMap<>();
        String query = "SELECT E.codice_esame, E.nome_esame FROM ESAME E JOIN INSEGNA I ON I.codice_esame = E.codice_esame WHERE I.cf = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, cf);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    esami.put(rs.getString("codice_esame"), rs.getString("nome_esame"));
                }
            }
        }
        return esami;
    }

    /**
     * Restituisce gli appelli passati creati dal docente.
     * @param cf codice fiscale del docente
     * @return lista di mappe contenenti i dettagli degli appelli
     * @throws SQLException in caso di errore
     */
    public List<Map<String, Object>> getAppelli(String cf) throws SQLException {
        String query = "SELECT A.numero_appello, A.data_appello, E.nome_esame, A.disponibile, " +
                "COALESCE(P.numero_iscritti, 0) AS numero_iscritti, D.nome_docente, D.cognome_docente " +
                "FROM APPELLO A " +
                "JOIN ESAME E ON E.codice_esame = A.codice_esame " +
                "JOIN DOCENTE D ON D.cf = A.cf " +
                "LEFT JOIN (SELECT numero_appello, COUNT(*) AS numero_iscritti FROM PRENOTAZIONE GROUP BY numero_appello) P " +
                "ON A.numero_appello = P.numero_appello " +
                "WHERE A.cf = ?"    ;

        List<Map<String, Object>> listaAppelli = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, cf);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> dati = new HashMap<>();
                    dati.put("data_appello", rs.getString("data_appello"));
                    dati.put("nome_esame", rs.getString("nome_esame"));
                    dati.put("disponibile", rs.getString("disponibile"));
                    dati.put("numero_iscritti", rs.getString("numero_iscritti"));
                    dati.put("credenziali_docente", rs.getString("nome_docente") + " " + rs.getString("cognome_docente"));
                    dati.put("numero_appello", rs.getString("numero_appello"));
                    listaAppelli.add(dati);
                }
            }
        }
        return listaAppelli;
    }

    /**
     * Restituisce la lista di prenotazioni relative agli appelli di un docente.
     * @param cf codice fiscale del docente
     * @return lista di mappe con i dettagli delle prenotazioni
     * @throws SQLException in caso di errore
     */
    public List<Map<String, Object>> getPrenotazioni(String cf) throws SQLException {
        String query = "SELECT S.matricola, P.numero_appello, E.nome_esame, A.data_appello, P.voto " +
                "FROM STUDENTE S " +
                "JOIN PRENOTAZIONE P ON S.matricola = P.matricola " +
                "JOIN APPELLO A ON P.numero_appello = A.numero_appello " +
                "JOIN ESAME E ON A.codice_esame = E.codice_esame " +
                "JOIN INSEGNA I ON E.codice_esame = I.codice_esame " +
                "JOIN DOCENTE D ON I.cf = D.cf " +
                "WHERE D.cf = ? AND P.stato IN ('prenotato', 'voto_inserito', 'accettato')";

        List<Map<String, Object>> listaPrenotazioni = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, cf);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> dati = new HashMap<>();
                    dati.put("data_appello", rs.getString("data_appello"));
                    dati.put("nome_esame", rs.getString("nome_esame"));
                    dati.put("matricola", rs.getString("matricola"));
                    dati.put("numero_appello", rs.getString("numero_appello"));
                    dati.put("voto", rs.getString("voto"));
                    listaPrenotazioni.add(dati);
                }
            }
        }
        return listaPrenotazioni;
    }
}
