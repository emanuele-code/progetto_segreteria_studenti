package Dao;

import Interfacce.ISegreteriaDAO;
import Utils.UtilHash;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Classe che implementa le funzionalità della segreteria universitaria.
 * Permette l'inserimento di studenti, aggiornamento del piano di studi,
 * conferma dei voti e recupero di piani di studio e voti da confermare.
 */
public class DAOSegreteria implements ISegreteriaDAO {
    private final Connection connection;

    /**
     * Costruttore che accetta una connessione al database.
     *
     * @param connection Connessione JDBC al database
     */
    public DAOSegreteria(Connection connection){
        this.connection = connection;
    }

    /**
     * Inserisce uno studente nel database.
     *
     * @param dati Array di stringhe contenente:
     *             <ul>
     *                 <li>dati[0] - nome</li>
     *                 <li>dati[1] - cognome</li>
     *                 <li>dati[2] - data di nascita</li>
     *                 <li>dati[3] - residenza</li>
     *                 <li>dati[4] - codice piano</li>
     *                 <li>dati[5] - password</li>
     *             </ul>
     * @throws SQLException in caso di errore durante l'inserimento
     */
    public void inserisciStudente(String[] dati)  throws SQLException {
        String query = "INSERT INTO STUDENTE (nome_studente, cognome_studente, data_nascita, residenza, stato_tasse, codice_piano, password) VALUES\n" +
                "(?,?,?,?,?,?,?)";
        try(PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, dati[0]);
            statement.setString(2, dati[1]);
            statement.setString(3, dati[2]);
            statement.setString(4, dati[3]);
            statement.setString(5, "non pagate");
            statement.setString(6, dati[4]);
            statement.setString(7, UtilHash.hashPassword(dati[5]));

            statement.executeUpdate();
        }
    }

    /**
     * Aggiorna il piano di studi di uno studente.
     *
     * @param matricola Matricola dello studente
     * @param codiceCorso Codice del nuovo piano di studi
     * @throws SQLException in caso di errore nell'aggiornamento
     */
    public void cambiaPianoStudente(String matricola, String codiceCorso) throws SQLException {
        String query = "UPDATE STUDENTE SET CODICE_PIANO = ? WHERE MATRICOLA = ?";
        try(PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, codiceCorso);
            statement.setString(2, matricola);

            statement.executeUpdate();
        }
    }

    /**
     * Conferma il voto accettato dallo studente per un determinato appello.
     *
     * @param matricola Matricola dello studente
     * @param numeroAppello Numero identificativo dell'appello
     * @throws SQLException in caso di errore nell'aggiornamento
     */
    public void confermaVoto(String matricola, String numeroAppello) throws SQLException {
        String query = " UPDATE PRENOTAZIONE " +
                " SET stato = 'confermato' " +
                " WHERE matricola = ? AND numero_appello = ? AND stato = 'accettato'";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, matricola);
            statement.setString(2, numeroAppello);

            statement.executeUpdate();
        }
    }

    /**
     * Restituisce la lista dei piani di studio disponibili.
     *
     * @return Mappa con codice piano come chiave e nome piano come valore
     * @throws SQLException in caso di errore durante la query
     */
    public Map<Integer, String> getPianiDiStudio() throws SQLException {
        Map<Integer, String> pianiDiStudio = new HashMap<>();
        String query = "SELECT codice_piano, nome_piano FROM PIANO_STUDI"; // Cambia il nome della tabella se necessario

        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                int codice = rs.getInt("codice_piano");
                String nome = rs.getString("nome_piano");
                pianiDiStudio.put(codice, nome); // Aggiunge codice e nome alla mappa
            }
        }
        return pianiDiStudio;
    }

    /**
     * Recupera la lista dei voti accettati che devono ancora essere confermati dalla segreteria.
     *
     * @return Lista di mappe con i dettagli dello studente e dell'appello
     * @throws SQLException in caso di errore durante l'esecuzione della query
     */
    public List<Map<String, Object>> getVotiDaConfermare() throws SQLException {
        String selectQuery = "SELECT D.nome_docente, D.cognome_docente, data_appello, P.matricola, A.numero_appello, P.voto, nome_esame, cfu FROM STUDENTE S " +
                " JOIN PRENOTAZIONE P ON P.matricola = S.matricola " +
                " JOIN APPELLO A ON A.numero_appello = P.numero_appello " +
                " JOIN DOCENTE D ON D.cf = A.cf " +
                " JOIN ESAME E ON E.codice_esame = A.codice_esame " +
                " WHERE stato = 'accettato'";

        List<Map<String, Object>> listaStudenti = new ArrayList<>();

        try (PreparedStatement selectStatement = connection.prepareStatement(selectQuery);
             ResultSet rs = selectStatement.executeQuery()) {

            while (rs.next()) {
                String dataAppello    = rs.getString("data_appello");
                String matricola      = rs.getString("matricola");
                String numeroAppello  = rs.getString("numero_appello");
                String voto           = rs.getString("voto");
                String nomeDocente    = rs.getString("nome_docente");
                String cognomeDocente = rs.getString("cognome_docente");
                String nomeEsame      = rs.getString("nome_esame");
                String cfu            = rs.getString("cfu");

                Map<String, Object> datiStudente = new HashMap<>();

                datiStudente.put("data_appello", dataAppello);
                datiStudente.put("matricola", matricola);
                datiStudente.put("numero_appello", numeroAppello);
                datiStudente.put("voto", voto);
                datiStudente.put("credenziali_docente", nomeDocente + " " + cognomeDocente);
                datiStudente.put("nome_esame", nomeEsame);
                datiStudente.put("cfu", cfu);

                listaStudenti.add(datiStudente);
            }
        }

        return listaStudenti;
    }
}
