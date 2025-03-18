package Interfacce;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface IDocenteDAO {
    void inserisciVoto(String cf, String voto, String numeroAppello) throws SQLException;
    void inserisciAppello(String dataAppello, String codiceEsame, String cf) throws SQLException;
    Map<String, String> getEsami(String cf) throws SQLException;
    void chiudiPrenotazione(String codiceAppello, Connection connection) throws SQLException;
    List<Map<String, Object>> getAppelli(String cf) throws SQLException;
    List<Map<String, Object>> getPrenotazioni(String cf) throws SQLException;
}
