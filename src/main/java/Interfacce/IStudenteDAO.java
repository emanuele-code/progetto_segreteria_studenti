package Interfacce;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface IStudenteDAO {
    void prenotaEsame(String numeroAppello, String matricola) throws SQLException;
    List<Map<String, Object>> getAppelli(String nomePiano, String matricola) throws SQLException;
    List<Map<String, Object>> getEsiti(String matricola) throws SQLException;
    void confermaVoti(String scelta, String matricola, String numeroAppello) throws SQLException;
    List<Map<String, Object>> getLibretto(String matricola) throws SQLException;
    void eliminaPrenotazione(String numeroAppello, String matricola) throws SQLException;
}
