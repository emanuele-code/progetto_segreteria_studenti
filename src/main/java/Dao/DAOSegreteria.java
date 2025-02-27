package Dao;

import Interfacce.ISegreteriaDAO;
import Utils.Hash;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class DAOSegreteria implements ISegreteriaDAO {
    private final Connection connection;

    public DAOSegreteria(Connection connection){
        this.connection = connection;
    }

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
            statement.setString(7, Hash.hashPassword(dati[5]));

            statement.executeUpdate();
        }
    }

    public void cambiaPianoStudente(String matricola, String codiceCorso) throws SQLException {
        String query = "UPDATE STUDENTE SET CODICE_PIANO = ? WHERE MATRICOLA = ?";
        try(PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, codiceCorso);
            statement.setString(2, matricola);

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Il piano di studi è stato aggiornato con successo.");
            } else {
                System.out.println("Nessuno studente trovato con la matricola: " + matricola);
            }
        }
    }

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


    public List<Map<String, Object>> getVotiDaConfermare() throws SQLException {
        String selectQuery = "SELECT A.cf, data_appello, P.matricola, A.numero_appello, P.voto, nome_esame, cfu FROM STUDENTE S " +
                " JOIN PRENOTAZIONE P ON P.matricola = S.matricola " +
                " JOIN APPELLO A ON A.numero_appello = P.numero_appello " +
                " JOIN ESAME E ON E.codice_esame = A.codice_esame " +
                " WHERE stato = 'accettato'";

        List<Map<String, Object>> listaStudenti = new ArrayList<>();

        try (PreparedStatement selectStatement = connection.prepareStatement(selectQuery);
             ResultSet rs = selectStatement.executeQuery()) {

            while (rs.next()) {
                String dataAppello = rs.getString("data_appello");
                String matricola = rs.getString("matricola");
                String numeroAppello = rs.getString("numero_appello");
                String voto = rs.getString("voto");
                String cfDocente = rs.getString("cf");
                String nomeEsame = rs.getString("nome_esame");
                String cfu = rs.getString("cfu");


                Map<String, Object> datiStudente = new HashMap<>();
                datiStudente.put("data_appello", dataAppello);
                datiStudente.put("matricola", matricola);
                datiStudente.put("numero_appello", numeroAppello);
                datiStudente.put("voto", voto);
                datiStudente.put("cf_docente", cfDocente);
                datiStudente.put("nome_esame", nomeEsame);
                datiStudente.put("cfu", cfu);

                listaStudenti.add(datiStudente);
            }
        }

        return listaStudenti;
    }
}
