package Dao;

import Interfacce.IStudenteDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class DAOStudente implements IStudenteDAO {
    private final Connection connection;

    public DAOStudente(Connection connection){
        this.connection = connection;
    }

    public List<Map<String, Object>> getEsiti(String matricola) throws SQLException {
        String query = "SELECT P.voto, A.data_appello, E.nome_esame, D.nome_docente, D.cognome_docente, E.cfu, A.numero_appello FROM PRENOTAZIONE P" +
                " JOIN APPELLO A ON A.numero_appello = P.numero_appello" +
                " JOIN DOCENTE D ON D.cf = A.cf" +
                " JOIN ESAME E ON E.codice_esame = A.codice_esame" +
                " WHERE matricola = ? AND stato = 'voto_inserito' AND voto not in ('Assente', 'Non Ammesso')";

        List<Map<String, Object>> listaEsiti = new ArrayList<>();

        try(PreparedStatement statement = connection.prepareStatement(query)){
            statement.setString(1, matricola);

            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    String voto           = rs.getString("voto");
                    String dataAppello    = rs.getString("data_appello");
                    String nomeEsame      = rs.getString("nome_esame");
                    String CFU      = rs.getString("CFU");
                    String nomeDocente    = rs.getString("nome_docente");
                    String cognomeDocente = rs.getString("cognome_docente");
                    String numeroAppello = rs.getString("numero_appello");

                    Map<String, Object> esitiDisponibili = new HashMap<>();
                    esitiDisponibili.put("voto", voto);
                    esitiDisponibili.put("data_appello", dataAppello);
                    esitiDisponibili.put("nome_esame", nomeEsame);
                    esitiDisponibili.put("CFU", CFU);
                    esitiDisponibili.put("credenziali_docente", nomeDocente + " " + cognomeDocente);
                    esitiDisponibili.put("numero_appello", numeroAppello);

                    listaEsiti.add(esitiDisponibili);
                }
            }
        }

        return listaEsiti;
    }


    public List<Map<String, Object>> getAppelliDisponibili(String nomePiano) throws SQLException {
        String query = " SELECT A.data_appello, E.nome_esame, D.nome_docente, A.numero_appello FROM PIANO_STUDI PS " +
                       " JOIN PREVEDE PR ON PR.codice_piano = PS.codice_piano" +
                       " JOIN ESAME E ON E.codice_esame = PR.codice_esame" +
                       " JOIN APPELLO A ON A.codice_esame = E.codice_esame" +
                       " JOIN DOCENTE D ON D.cf = A.cf" +
                       " WHERE nome_piano =  ? AND disponibile = 1";

        List<Map<String, Object>> listaAppelliDisponibili = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, nomePiano);

            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    String nomeEsame = rs.getString("nome_esame");
                    String dataAppello = rs.getString("data_appello");
                    String nomeDocente = rs.getString("nome_docente");
                    int numeroAppello = rs.getInt("numero_appello");

                    // Creazione della mappa per l'esame, il voto e il numero di CFU
                    Map<String, Object> appelloDisponibile = new HashMap<>();
                    appelloDisponibile.put("nome_esame", nomeEsame);
                    appelloDisponibile.put("data_appello", dataAppello);
                    appelloDisponibile.put("nome_docente", nomeDocente);
                    appelloDisponibile.put("numero_appello", numeroAppello);

                    listaAppelliDisponibili.add(appelloDisponibile);
                }
            }
        }
        return listaAppelliDisponibili;
    }


    public static List<Map<String, Object>> getDatiEsami(String matricola, String codicePiano, Connection connection) throws SQLException {
        String query = "SELECT distinct e.nome_esame, p.voto, e.cfu FROM STUDENTE s" +
                " JOIN PRENOTAZIONE p ON s.matricola = p.matricola " +
                " JOIN APPELLO a ON p.numero_appello = a.numero_appello" +
                " JOIN PIANO_STUDI ps ON ps.codice_piano = s.codice_piano" +
                " JOIN PREVEDE pr ON pr.codice_piano = ps.codice_piano" +
                " JOIN ESAME e ON e.codice_esame = pr.codice_esame" +
                " WHERE s.matricola = ? AND s.codice_piano = ?";


        List<Map<String, Object>> datiEsami = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, matricola);
            statement.setString(2, codicePiano);


            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    String nomeEsame = rs.getString("nome_esame");
                    int voto = rs.getInt("voto");
                    int cfu = rs.getInt("cfu");

                    // Creazione della mappa per l'esame, il voto e il numero di CFU
                    Map<String, Object> esameVotoCfu = new HashMap<>();
                    esameVotoCfu.put("nome_esame", nomeEsame);
                    esameVotoCfu.put("voto", voto);
                    esameVotoCfu.put("cfu", cfu);

                    datiEsami.add(esameVotoCfu);
                }
            }
        }
        return datiEsami;
    }


    public static List<Map<String, Object>> getDatiStudente(ResultSet rs, Connection connection) throws SQLException {
        List<Map<String, Object>> listaStudenti = new ArrayList<>();

        while (rs.next()) {
            String matricola = rs.getString("matricola");
            String nome = rs.getString("nome_studente");
            String cognome = rs.getString("cognome_studente");
            String data = rs.getString("data_nascita");
            String residenza = rs.getString("residenza");
            String tassePagate = rs.getString("stato_tasse");
            String pianoStudi = rs.getString("nome_piano");
            String codicePiano = rs.getString("codice_piano");

            // Recupera i dati degli esami per lo studente
            List<Map<String, Object>> datiEsami = getDatiEsami(matricola, codicePiano, connection);

            // Crea una mappa con i dati dello studente
            Map<String, Object> datiStudente = new HashMap<>();
            datiStudente.put("matricola", matricola);
            datiStudente.put("nome", nome);
            datiStudente.put("cognome", cognome);
            datiStudente.put("data", data);
            datiStudente.put("residenza", residenza);
            datiStudente.put("tassePagate", tassePagate);
            datiStudente.put("pianoStudi", pianoStudi);
            datiStudente.put("codicePiano", codicePiano);
            datiStudente.put("datiEsami", datiEsami);

            // Aggiungi lo studente alla lista
            listaStudenti.add(datiStudente);
        }
        return listaStudenti;
    }


    public static List<Map<String, Object>> getDatiPerMatricola(String matricola, Connection connection) throws SQLException {
        String query = "SELECT s.matricola, s.nome_studente, s.cognome_studente, s.data_nascita, " +
                "s.residenza, s.stato_tasse, ps.nome_piano, ps.cfu_totali, s.codice_piano FROM STUDENTE s " +
                "JOIN PIANO_STUDI ps ON s.codice_piano = ps.codice_piano " +
                "WHERE s.matricola = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, matricola);
            return getDatiStudente(statement.executeQuery(), connection);
        }
    }


    public static List<Map<String, Object>> getDatiPerCredenziali(String nome, String cognome, Connection connection) throws SQLException {
        String query = "SELECT s.matricola, s.nome_studente, s.cognome_studente, s.data_nascita, " +
                "s.residenza, s.stato_tasse, ps.nome_piano, ps.cfu_totali, s.codice_piano FROM STUDENTE s " +
                "JOIN PIANO_STUDI ps ON s.codice_piano = ps.codice_piano " +
                "WHERE s.nome_studente = ? and s.cognome_studente = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, nome);
            statement.setString(2, cognome);
            return getDatiStudente(statement.executeQuery(), connection);
        }
    }


    public List<Map<String, Object>> getAppelli(String nomePiano) throws SQLException {

        String selectQuery = "SELECT A.disponibile, D.nome_docente, D.cognome_docente, A.numero_appello, E.nome_esame, A.data_appello FROM APPELLO A " +
                " JOIN DOCENTE D ON D.cf = A.cf" +
                " JOIN ESAME E ON E.codice_esame = A.codice_esame" +
                " JOIN PREVEDE P ON P.codice_esame = E.codice_esame" +
                " JOIN PIANO_STUDI PI ON PI.codice_piano = P.codice_piano" +
                " WHERE PI.nome_piano = ?";



        List<Map<String, Object>> listaAppelli = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(selectQuery)) {
            statement.setString(1, nomePiano);
            try (ResultSet rs = statement.executeQuery()){
                while (rs.next()) {
                    String nomeDocente = rs.getString("nome_docente");
                    String cognomeDocente = rs.getString("cognome_docente");
                    String numeroAppello = rs.getString("numero_appello");
                    String nomeEsame = rs.getString("nome_esame");
                    String dataAppello = rs.getString("data_appello");
                    Boolean disponibile = rs.getBoolean("disponibile");


                    Map<String, Object> datiStudente = new HashMap<>();
                    datiStudente.put("data_appello", dataAppello);
                    datiStudente.put("nome_esame", nomeEsame);
                    datiStudente.put("numero_appello", numeroAppello);
                    datiStudente.put("credenziali_docente", nomeDocente + " " + cognomeDocente);
                    datiStudente.put("disponibile", disponibile);

                    listaAppelli.add(datiStudente);
                }
            }
        }


        return listaAppelli;
    }


    public List<Map<String, Object>> getLibretto(String matricola) throws SQLException {
        String query = "SELECT E.codice_esame, E.nome_esame, E.CFU, COALESCE(P.voto, 'Non sostenuto') as voto, A.data_appello, P.stato FROM PIANO_STUDI PS " +
                " JOIN PREVEDE PR ON PS.codice_piano = PR.codice_piano " +
                " JOIN ESAME E ON PR.codice_esame = E.codice_esame " +
                " LEFT JOIN APPELLO A ON E.codice_esame = A.codice_esame " +
                " LEFT JOIN PRENOTAZIONE P ON A.numero_appello = P.numero_appello AND P.matricola = ? " +
                " WHERE PS.codice_piano = (SELECT codice_piano FROM STUDENTE WHERE matricola = ?) " +
                " GROUP BY E.codice_esame, E.nome_esame, P.voto;";

        List<Map<String, Object>> libretto = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, matricola);
            statement.setString(2, matricola);

            try (ResultSet rs = statement.executeQuery()){
                while (rs.next()) {
                    String codiceEsame = rs.getString("codice_esame");
                    String nomeEsame   = rs.getString("nome_esame");
                    String CFU         = rs.getString("CFU");
                    String voto        = rs.getString("voto");
                    String dataAppello = rs.getString("data_appello");
                    String stato       = rs.getString("stato");


                    Map<String, Object> datiLibretto = new HashMap<>();
                    datiLibretto.put("codice_esame", codiceEsame);
                    datiLibretto.put("nome_esame", nomeEsame);
                    datiLibretto.put("CFU", CFU);
                    datiLibretto.put("voto", voto);
                    datiLibretto.put("data_appello", dataAppello);
                    datiLibretto.put("stato", stato);

                    libretto.add(datiLibretto);
                }
            }
        }

        System.out.println(libretto);

        return libretto;
    }


    public void ConfermaVoti(String scelta, String matricola, String numeroAppello) throws SQLException {
        String insertPrenotazioneQuery = "UPDATE PRENOTAZIONE SET stato = ?" +
                " WHERE matricola = ? AND numero_appello = ?";

        try (PreparedStatement insertPrenotazioneStmt = connection.prepareStatement(insertPrenotazioneQuery)) {
            insertPrenotazioneStmt.setString(1, scelta);
            insertPrenotazioneStmt.setString(3, matricola);
            insertPrenotazioneStmt.setString(2, numeroAppello);

            insertPrenotazioneStmt.executeUpdate();
        }
    }


    public void prenotaEsame(String numeroAppello, String matricola) throws SQLException {
        String insertPrenotazioneQuery = "INSERT INTO PRENOTAZIONE (matricola, numero_appello, data_prenotazione, voto) " +
                " VALUES (?, ?, current_date, NULL)";

        try (PreparedStatement insertPrenotazioneStmt = connection.prepareStatement(insertPrenotazioneQuery)) {
            insertPrenotazioneStmt.setString(1, matricola);
            insertPrenotazioneStmt.setString(2, numeroAppello);

            insertPrenotazioneStmt.executeUpdate();
        }
    }


    public Map<String, Object> consultaPiano(String matricola) throws SQLException {
        String query = "SELECT * FROM STUDENTE S " +
                " JOIN PIANO_STUDI PN ON PN.codice_piano = S.codice_piano " +
                " JOIN PREVEDE P ON PN.codice_piano = P.codice_piano " +
                " JOIN ESAME E ON E.codice_esame = P.codice_esame" +
                " WHERE matricola = ?";

        Map<String, Object> datiStudente = new HashMap<>();

        try(PreparedStatement statement = connection.prepareStatement(query)){
            statement.setString(1, matricola);

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    String codicePiano = rs.getString("codice_piano");
                    String cfuTotali = rs.getString("cfu_totali");
                    String nomePiano = rs.getString("nome_piano");
                    String residenza = rs.getString("residenza");
                    boolean tassePagate = "pagate".equals(rs.getString("stato_tasse"));
                    String pianoStudi = rs.getString("nome_piano");

                    List<Map<String, Object>> datiEsami = getDatiEsami(matricola, codicePiano, connection);

                    datiStudente.put("dati_esami", datiEsami);
                    datiStudente.put("codice_piano", codicePiano);
                    datiStudente.put("cfu_totali", cfuTotali);
                    datiStudente.put("nome_piano", nomePiano);
                    datiStudente.put("residenza", residenza);
                    datiStudente.put("tasse_pagate", tassePagate ? "Sì" : "No");
                }
                return datiStudente;
            }
        }
    }
}
