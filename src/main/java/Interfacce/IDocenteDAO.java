package Interfacce;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface IDocenteDAO {
    public void inserisciVoto(String cf, String voto, String numeroAppello) throws SQLException;
    public void inserisciAppello(String dataAppello, String codiceEsame, String cf) throws SQLException;
    public Map<String, String> getEsami(String cf) throws SQLException;
    public void chiudiPrenotazione(String codiceAppello, Connection connection) throws SQLException;
    public List<Map<String, Object>> getAppelli(String cf) throws SQLException;
    public List<Map<String, Object>> getPrenotazioni(String cf) throws SQLException;
}
