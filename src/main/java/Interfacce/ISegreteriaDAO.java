package Interfacce;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface ISegreteriaDAO {
    public void inserisciStudente(String[] dati) throws SQLException;
    public void cambiaPianoStudente(String matricola, String codiceCorso) throws SQLException;
    public Map<Integer, String> getPianiDiStudio() throws SQLException;
    public List<Map<String, Object>> getVotiDaConfermare() throws SQLException;
    public void confermaVoto(String matricola, String numeroAppello) throws SQLException;
}
