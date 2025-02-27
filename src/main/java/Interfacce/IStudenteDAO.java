package Interfacce;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface IStudenteDAO {
    public Map<String, Object> consultaPiano(String matricola) throws SQLException;
    public void prenotaEsame(String numeroAppello, String matricola) throws SQLException;
    public List<Map<String, Object>> getAppelliDisponibili(String nomePiano) throws SQLException;
    public List<Map<String, Object>> getAppelli(String nomePiano) throws SQLException;
    public List<Map<String, Object>> getEsiti(String matricola) throws SQLException;
    public void ConfermaVoti(String scelta, String matricola, String numeroAppello) throws SQLException;
    List<Map<String, Object>> getLibretto(String matricola) throws SQLException;
}
