package Dao;

import Interfacce.IAutenticazioneDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DAOAutenticazione implements IAutenticazioneDAO {
    private final String SEGRETERIA_HASHED_PASSWORD = "bf629c9d8a2c2369aad28d9bb563c66f3370cc70cf7a4a72f6ddd39f17fbf227";
    private final String SEGRETERIA_EMAIL = "segreteria@esempio.com";

    private Connection connection;

    public DAOAutenticazione(Connection connection) {
        this.connection = connection;
    }

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


    @Override
    public boolean isUtenteAutenticato(String utente, String credenziale, String password) {
        String query = "";
        switch(utente) {
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
