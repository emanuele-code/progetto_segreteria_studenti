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

public class DAODocente implements IDocenteDAO {
    private final Connection connection;

    public DAODocente(Connection connection){
        this.connection = connection;
    }

    public void inserisciVoto(String matricola, String voto, String numeroAppello) throws SQLException {
        String query = "UPDATE PRENOTAZIONE " +
                "SET voto = ?, stato = 'voto_inserito' " +
                "WHERE numero_appello = ? AND matricola = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, voto);
            statement.setString(2, numeroAppello);
            statement.setString(3, matricola);

            statement.executeUpdate();
        }
    }

    public static Map<String, String> getDatiDocente(String cf, Connection connection) throws SQLException {
        String query = "SELECT cf, nome_docente, cognome_docente, email FROM DOCENTE WHERE cf = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, cf);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    String nome = rs.getString("nome_docente");
                    String cognome = rs.getString("cognome_docente");
                    String email = rs.getString("email");

                    Map<String, String> datiDocente = new HashMap<>();
                    datiDocente.put("nome", nome);
                    datiDocente.put("cognome", cognome);
                    datiDocente.put("email", email);
                    return datiDocente;
                }
            }
        }
        return null; // Se non trovato
    }

    public void inserisciAppello(String dataAppello, String codiceEsame, String cf) throws SQLException {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate dataFormattata = LocalDate.parse(dataAppello, formatter);

        if(dataFormattata.isAfter(LocalDate.now().minusDays(7))){
            UtilAlert.mostraErrore("L'appello va inserito almeno 7 giorni prima la data d'esame", "Errore Data Appello", "Non è consentito inserire un appello in questa data");
        }

        String query = " SELECT COUNT(*) FROM DOCENTE D" +
                                    " JOIN INSEGNA I ON I.cf = d.cf" +
                                    " JOIN ESAME E ON E.codice_esame = i.codice_esame" +
                                    " WHERE D.cf = ? AND E.codice_esame = ?;";
        try (PreparedStatement verificaStatement = connection.prepareStatement(query)) {
            verificaStatement.setString(1, cf);
            verificaStatement.setString(2, codiceEsame);
        }

        query = "INSERT INTO APPELLO (data_appello, codice_esame, cf) VALUES (?,?,?)";
        try(PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, dataAppello);
            statement.setString(2, codiceEsame);
            statement.setString(3, cf);

            statement.executeUpdate();
        }
    }

    public void chiudiPrenotazione(String numeroAppello, Connection connection) throws SQLException {
        String query = " UPDATE APPELLO SET disponibile = 0 " +
                       " WHERE numero_appello = ?";

        try (PreparedStatement updateStatement = connection.prepareStatement(query)) {
            updateStatement.setString(1, numeroAppello);
            updateStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Map<String, String> getEsami(String cf) throws SQLException {
        Map<String, String> esami = new HashMap<>();

        String query = "SELECT E.codice_esame, E.nome_esame, cfu FROM ESAME E " +
                " JOIN INSEGNA I ON I.codice_esame = E.codice_esame" +
                " WHERE I.cf = ?";

        try(PreparedStatement statement = connection.prepareStatement(query)){
            statement.setString(1, cf);

            try (ResultSet rs = statement.executeQuery()) {
                while(rs.next()){
                    String codiceEsame = rs.getString("codice_esame");
                    String nomeEsame = rs.getString("nome_esame");

                    System.out.println("Codice Esame: " + codiceEsame + " - Nome Esame: " + nomeEsame);


                    esami.put(codiceEsame, nomeEsame);
                }
            }
        }

        return esami;
    }

    public List<Map<String, Object>> getAppelli(String cf) throws SQLException {

        String selectQuery = " SELECT A.numero_appello, A.data_appello, E.nome_esame, A.disponibile, COALESCE(P.numero_iscritti, 0) AS numero_iscritti, D.nome_docente, D.cognome_docente FROM APPELLO A " +
                " JOIN ESAME E ON E.codice_esame = A.codice_esame " +
                " JOIN DOCENTE D ON D.CF = A.cf " +
                " LEFT JOIN ( " +
                "    SELECT numero_appello, COUNT(*) AS numero_iscritti FROM PRENOTAZIONE " +
                "    GROUP BY numero_appello " +
                ") P ON A.numero_appello = P.numero_appello " +
                " WHERE A.cf = ? AND A.data_appello <= date('now')";



        List<Map<String, Object>> listaAppelli = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(selectQuery)) {
            statement.setString(1, cf);
            try (ResultSet rs = statement.executeQuery()){
                while (rs.next()) {
                    String dataAppello = rs.getString("data_appello");
                    String nomeEsame = rs.getString("nome_esame");
                    String disponibile = rs.getString("disponibile");
                    String numeroIscritti = rs.getString("numero_iscritti");
                    String nomeDocente = rs.getString("nome_docente");
                    String cognomeDocente = rs.getString("cognome_docente");
                    String numeroAppello = rs.getString("numero_appello");


                    Map<String, Object> datiStudente = new HashMap<>();
                    datiStudente.put("data_appello", dataAppello);
                    datiStudente.put("nome_esame", nomeEsame);
                    datiStudente.put("disponibile", disponibile);
                    datiStudente.put("numero_iscritti", numeroIscritti);
                    datiStudente.put("credenziali_docente", nomeDocente + " " + cognomeDocente);
                    datiStudente.put("numero_appello", numeroAppello);

                    listaAppelli.add(datiStudente);
                }
            }
        }
        return listaAppelli;
    }

    public List<Map<String, Object>> getPrenotazioni(String cf) throws SQLException {
        String selectQuery = " SELECT S.matricola, P.numero_appello, E.nome_esame, A.data_appello, P.voto\n" +
                " FROM STUDENTE S\n" +
                " JOIN PRENOTAZIONE P ON S.matricola = P.matricola\n" +
                " JOIN APPELLO A ON P.numero_appello = A.numero_appello\n" +
                " JOIN ESAME E ON A.codice_esame = E.codice_esame\n" +
                " JOIN INSEGNA I ON E.codice_esame = I.codice_esame\n" +
                " JOIN DOCENTE D ON I.cf = D.cf\n" +
                " WHERE D.cf = ? AND P.stato IN ('prenotato', 'voto_inserito', 'accettato');";



        List<Map<String, Object>> listaPrenotazioni = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(selectQuery)) {
            statement.setString(1, cf);
            try (ResultSet rs = statement.executeQuery()){
                while (rs.next()) {
                    String dataAppello = rs.getString("data_appello");
                    String nomeEsame = rs.getString("nome_esame");
                    String matricola = rs.getString("matricola");
                    String numeroAppello = rs.getString("numero_appello");
                    String voto = rs.getString("voto");

                    Map<String, Object> datiPrenotazioni = new HashMap<>();
                    datiPrenotazioni.put("data_appello", dataAppello);
                    datiPrenotazioni.put("nome_esame", nomeEsame);
                    datiPrenotazioni.put("matricola", matricola);
                    datiPrenotazioni.put("numero_appello", numeroAppello);
                    datiPrenotazioni.put("voto", voto);

                    listaPrenotazioni.add(datiPrenotazioni);
                }
            }
        }
        return listaPrenotazioni;
    }
}