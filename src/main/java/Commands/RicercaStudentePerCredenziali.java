package Commands;
import Interfacce.IRicercaStudenteStrategy;
import Interfacce.IStudente;
import Models.UtenteFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class RicercaStudentePerCredenziali implements IRicercaStudenteStrategy {
    private String nome;
    private String cognome;

    public RicercaStudentePerCredenziali(String nome, String cognome, Connection connection){
        this.nome = nome;
        this.cognome = cognome;
    }

    public List<IStudente> ricercaStudente(Connection connection) throws SQLException {
        return UtenteFactory.creaStudenteDaCredenziali(nome, cognome, connection);
    }
}
