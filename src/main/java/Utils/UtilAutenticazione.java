package Utils;

import Dao.DAOAutenticazione;
import Interfacce.IAutenticazioneDAO;

import java.sql.Connection;

public class UtilAutenticazione {
    private IAutenticazioneDAO autenticazioneDAO;

    public UtilAutenticazione(Connection connection) {
        autenticazioneDAO = new DAOAutenticazione(connection);
    }

    public boolean login(String utente, String credenziale, String password){
        String hashedPassword = UtilHash.hashPassword(password);
        return autenticazioneDAO.isUtenteAutenticato(utente, credenziale, hashedPassword);
    }

}
