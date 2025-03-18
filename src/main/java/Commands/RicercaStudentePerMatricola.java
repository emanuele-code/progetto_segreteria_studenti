package Commands;

import Interfacce.IRicercaStudenteStrategy;
import Interfacce.ISetCommand;
import Models.UtenteFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class RicercaStudentePerMatricola implements IRicercaStudenteStrategy {
    private String matricola;

    public RicercaStudentePerMatricola(String matricola){
        this.matricola = matricola;
    }

    public List<ISetCommand> ricercaStudente(Connection connection) throws SQLException {
        return UtenteFactory.creaStudenteDaMatricola(matricola, connection);
    }
}
