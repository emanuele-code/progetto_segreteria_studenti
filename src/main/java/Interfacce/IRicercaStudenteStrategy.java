package Interfacce;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface IRicercaStudenteStrategy {
    List<ISetCommand> ricercaStudente(Connection connection) throws SQLException;

}
