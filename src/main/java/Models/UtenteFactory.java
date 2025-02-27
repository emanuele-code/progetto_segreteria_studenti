package Models;

import Dao.DAODocente;
import Dao.DAOStudente;
import Interfacce.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UtenteFactory {


    public static List<IStudente> creaStudenteDaMatricola(String matricola, Connection connection) throws SQLException {
        List<Map<String, Object>> datiStudente = DAOStudente.getDatiPerMatricola(matricola, connection);
        return creaStudente(datiStudente);
    }

    public static List<IStudente> creaStudenteDaCredenziali(String nome, String cognome, Connection connection) throws SQLException {
        List<Map<String, Object>> datiStudente = DAOStudente.getDatiPerCredenziali(nome, cognome, connection);
        return creaStudente(datiStudente);
    }

    private static List<IStudente> creaStudente(List<Map<String, Object>> datiStudenti) throws SQLException {
        if(datiStudenti == null){
            return null;
        }
        List<IStudente> studenti = new ArrayList<>();

        for (Map<String, Object> dati : datiStudenti) {
            IStudente studente = new Studente(
                    (String) dati.get("matricola"),
                    (String) dati.get("nome"),
                    (String) dati.get("cognome"),
                    (String) dati.get("data"),
                    (String) dati.get("residenza"),
                    (String) dati.get("tassePagate"),
                    (String) dati.get("pianoStudi"),
                    (List<Map<String, Object>>) dati.get("datiEsami")
            );
            studenti.add(studente);
        }
        for (IStudente studente : studenti){
            System.out.println(studente);
        }

        return studenti;
    }

    public static IDocente creaDocente(String cf, Connection connection) throws SQLException {
        Map<String, String> datiDocente = DAODocente.getDatiDocente(cf, connection);
        return new Docente(cf, datiDocente.get("nome"), datiDocente.get("cognome"), datiDocente.get("email"), connection);
    }

    public static ISegreteria creaSegreteria(Connection connection) {
        return new Segreteria(connection);
    }
}
