package Utils;

import Dao.DAOAutenticazione;
import Interfacce.IAutenticazioneDAO;

import java.sql.Connection;

public class Autenticazione {
    private IAutenticazioneDAO autenticazioneDAO;

    public Autenticazione(Connection connection) {
        autenticazioneDAO = new DAOAutenticazione(connection);
    }

    public boolean login(String utente, String credenziale, String password){
        String hashedPassword = Hash.hashPassword(password);
        return autenticazioneDAO.isUtenteAutenticato(utente, credenziale, hashedPassword);
    }

}
