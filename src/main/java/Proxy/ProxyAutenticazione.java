package Proxy;

import Dao.DAOAutenticazione;
import Interfacce.IAutenticazioneDAO;
import Utils.UtilHash;

import java.sql.Connection;

public class ProxyAutenticazione implements IAutenticazioneDAO {
    private DAOAutenticazione daoAutenticazione;
    private Connection connection;

    public ProxyAutenticazione(Connection connection) {
        this.connection = connection;
    }

    private DAOAutenticazione getDaoAutenticazione() {
        if (daoAutenticazione == null) {
            daoAutenticazione = new DAOAutenticazione(connection);
        }
        return daoAutenticazione;
    }

    @Override
    public boolean isAutenticato(String query, String param1, String param2) {
        return getDaoAutenticazione().isAutenticato(query, param1, param2);
    }

    @Override
    public boolean isUtenteAutenticato(String utente, String credenziale, String password) {
        String hashedPassword = UtilHash.hashPassword(password);
        return getDaoAutenticazione().isUtenteAutenticato(utente, credenziale, hashedPassword);
    }
}
