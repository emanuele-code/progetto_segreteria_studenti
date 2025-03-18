package Interfacce;

public interface IAutenticazioneDAO {
    boolean isAutenticato(String query, String param1, String param2);
    boolean isUtenteAutenticato(String utente, String credenziale, String password);
}
