package Dao;

import Interfacce.IStudenteDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Classe DAOStudente che implementa l'interfaccia IStudenteDAO.
 * Gestisce tutte le operazioni relative agli studenti nel database.
 */
public class DAOStudente implements IStudenteDAO {
    private final Connection connection;

    /**
     * Costruttore della classe DAOStudente.
     *
     * @param connection Connessione al database.
     */
    public DAOStudente(Connection connection){
        this.connection = connection;
    }

    /**
     * Recupera la lista degli esiti per uno studente a partire dalla sua matricola.
     *
     * @param matricola Matricola dello studente.
     * @return Lista di mappe contenenti i dettagli degli esiti (voto, data, nome esame, docente, ecc.).
     * @throws SQLException In caso di errori SQL.
     */
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

    /**
     * Recupera i dati degli esami per uno studente dato il codice del piano di studi.
     *
     * @param matricola Matricola dello studente.
     * @param codicePiano Codice del piano di studi.
     * @param connection Connessione al database.
     * @return Lista di mappe con nome esame, voto e CFU.
     * @throws SQLException In caso di errori SQL.
     */
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

    /**
     * Estrae i dati completi degli studenti da un ResultSet.
     *
     * @param rs ResultSet contenente i dati base degli studenti.
     * @param connection Connessione al database.
     * @return Lista di mappe contenenti i dati dello studente e i relativi esami.
     * @throws SQLException In caso di errori SQL.
     */
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

            List<Map<String, Object>> datiEsami = getDatiEsami(matricola, codicePiano, connection);

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

            listaStudenti.add(datiStudente);
        }
        return listaStudenti;
    }

    /**
     * Recupera i dati di uno studente a partire dalla sua matricola.
     *
     * @param matricola Matricola dello studente.
     * @param connection Connessione al database.
     * @return Lista con i dati completi dello studente.
     * @throws SQLException In caso di errori SQL.
     */
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

    /**
     * Recupera i dati di uno studente a partire da nome e cognome.
     *
     * @param nome Nome dello studente.
     * @param cognome Cognome dello studente.
     * @param connection Connessione al database.
     * @return Lista con i dati completi dello studente.
     * @throws SQLException In caso di errori SQL.
     */
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

    /**
     * Recupera tutti gli appelli relativi al piano di studi di uno studente.
     *
     * @param nomePiano Nome del piano di studi.
     * @param matricola Matricola dello studente.
     * @return Lista di appelli disponibili e prenotati.
     * @throws SQLException In caso di errori SQL.
     */
    public List<Map<String, Object>> getAppelli(String nomePiano, String matricola) throws SQLException {
        String selectQuery = "SELECT A.disponibile, D.nome_docente, D.cognome_docente, A.numero_appello, E.nome_esame, A.data_appello, " +
                "CASE WHEN PRT.matricola IS NOT NULL THEN TRUE ELSE FALSE END AS prenotato " +
                "FROM APPELLO A " +
                "JOIN DOCENTE D ON D.cf = A.cf " +
                "JOIN ESAME E ON E.codice_esame = A.codice_esame " +
                "JOIN PREVEDE P ON P.codice_esame = E.codice_esame " +
                "JOIN PIANO_STUDI PI ON PI.codice_piano = P.codice_piano " +
                "LEFT JOIN PRENOTAZIONE PRT ON PRT.numero_appello = A.numero_appello AND PRT.matricola = ? " +
                "WHERE PI.nome_piano = ? AND A.data_appello > date('now')";


        List<Map<String, Object>> listaAppelli = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(selectQuery)) {
            statement.setString(1, matricola);
            statement.setString(2, nomePiano);

            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    String nomeDocente = rs.getString("nome_docente");
                    String cognomeDocente = rs.getString("cognome_docente");
                    String numeroAppello = rs.getString("numero_appello");
                    String nomeEsame = rs.getString("nome_esame");
                    String dataAppello = rs.getString("data_appello");
                    Boolean disponibile = rs.getBoolean("disponibile");
                    Boolean isPrenotato = rs.getBoolean("prenotato");

                    Map<String, Object> datiAppello = new HashMap<>();
                    datiAppello.put("data_appello", dataAppello);
                    datiAppello.put("nome_esame", nomeEsame);
                    datiAppello.put("numero_appello", numeroAppello);
                    datiAppello.put("credenziali_docente", nomeDocente + " " + cognomeDocente);
                    datiAppello.put("disponibile", disponibile);
                    datiAppello.put("prenotato", isPrenotato);

                    listaAppelli.add(datiAppello);
                }
            }
        }

        return listaAppelli;
    }

    /**
     * Recupera il libretto di uno studente con tutti gli esami presenti nel piano di studi,
     * lo stato della prenotazione, il voto (se presente), e la data dell'appello.
     *
     * @param matricola Matricola dello studente.
     * @return Lista di esami con informazioni dettagliate.
     * @throws SQLException In caso di errori SQL.
     */
    public List<Map<String, Object>> getLibretto(String matricola) throws SQLException {

        String query = "WITH EsamiConPriorita AS (\n" +
                "    SELECT \n" +
                "        E.codice_esame, \n" +
                "        E.nome_esame, \n" +
                "        E.CFU, \n" +
                "        A.data_appello, \n" +
                "        P.stato,\n" +
                "        COALESCE(CASE WHEN P.stato = 'confermato' THEN P.voto ELSE NULL END, 'Non sostenuto') AS voto,\n" +
                "        ROW_NUMBER() OVER (\n" +
                "            PARTITION BY E.codice_esame\n" +
                "            ORDER BY \n" +
                "                CASE WHEN P.stato = 'confermato' THEN 1 ELSE 2 END,\n" +
                "                A.data_appello DESC\n" +
                "        ) AS rn\n" +
                "    FROM PIANO_STUDI PS\n" +
                "    JOIN PREVEDE PR ON PS.codice_piano = PR.codice_piano\n" +
                "    JOIN ESAME E ON PR.codice_esame = E.codice_esame\n" +
                "    LEFT JOIN APPELLO A ON E.codice_esame = A.codice_esame\n" +
                "    LEFT JOIN PRENOTAZIONE P ON A.numero_appello = P.numero_appello AND P.matricola = ?\n" +
                "    WHERE PS.codice_piano = (\n" +
                "        SELECT codice_piano \n" +
                "        FROM STUDENTE \n" +
                "        WHERE matricola = ?\n" +
                "    )\n" +
                ")\n" +
                "SELECT codice_esame, nome_esame, CFU, data_appello, stato, voto\n" +
                "FROM EsamiConPriorita\n" +
                "WHERE rn = 1;\n";

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
        return libretto;
    }

    /**
     * Conferma o rifiuta un voto per uno specifico appello di uno studente.
     *
     * @param scelta Stato da assegnare alla prenotazione ("confermato", "rifiutato", ecc.).
     * @param matricola Matricola dello studente.
     * @param numeroAppello Numero dell'appello.
     * @throws SQLException In caso di errori SQL.
     */
    public void confermaVoti(String scelta, String matricola, String numeroAppello) throws SQLException {
        String insertPrenotazioneQuery = "UPDATE PRENOTAZIONE SET stato = ?" +
                " WHERE matricola = ? AND numero_appello = ?";

        try (PreparedStatement insertPrenotazioneStmt = connection.prepareStatement(insertPrenotazioneQuery)) {
            insertPrenotazioneStmt.setString(1, scelta);
            insertPrenotazioneStmt.setString(2, matricola);
            insertPrenotazioneStmt.setString(3, numeroAppello);

            insertPrenotazioneStmt.executeUpdate();
        }
    }

    /**
     * Prenota uno studente ad un appello.
     *
     * @param numeroAppello Numero dell'appello.
     * @param matricola Matricola dello studente.
     * @throws SQLException In caso di errori SQL.
     */
    public void prenotaEsame(String numeroAppello, String matricola) throws SQLException {
        String insertPrenotazioneQuery = "INSERT INTO PRENOTAZIONE (matricola, numero_appello, data_prenotazione, voto) " +
                " VALUES (?, ?, current_date, NULL)";

        try (PreparedStatement insertPrenotazioneStmt = connection.prepareStatement(insertPrenotazioneQuery)) {
            insertPrenotazioneStmt.setString(1, matricola);
            insertPrenotazioneStmt.setString(2, numeroAppello);

            insertPrenotazioneStmt.executeUpdate();
        }
    }

    /**
     * Elimina la prenotazione di uno studente ad un appello.
     *
     * @param numeroAppello Numero dell'appello.
     * @param matricola Matricola dello studente.
     * @throws SQLException In caso di errori SQL.
     */
    public void eliminaPrenotazione(String numeroAppello, String matricola) throws SQLException {
        String deletePrenotazioneQuery = "DELETE FROM PRENOTAZIONE WHERE matricola = ? AND numero_appello = ?";

        try (PreparedStatement deletePrenotazioneStmt = connection.prepareStatement(deletePrenotazioneQuery)) {
            deletePrenotazioneStmt.setString(1, matricola);
            deletePrenotazioneStmt.setString(2, numeroAppello);

            deletePrenotazioneStmt.executeUpdate();
        }
    }

}
