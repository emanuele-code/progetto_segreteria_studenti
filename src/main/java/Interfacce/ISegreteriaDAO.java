package Interfacce;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface ISegreteriaDAO {
    void inserisciStudente(String[] dati) throws SQLException;
    void cambiaPianoStudente(String matricola, String codiceCorso) throws SQLException;
    void confermaVoto(String matricola, String numeroAppello) throws SQLException;
    Map<Integer, String> getPianiDiStudio() throws SQLException;
    List<Map<String, Object>> getVotiDaConfermare() throws SQLException;
}
