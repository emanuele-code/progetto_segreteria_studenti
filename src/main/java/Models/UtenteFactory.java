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


    public static List<ISetCommand> creaStudenteDaMatricola(String matricola, Connection connection) throws SQLException {
        List<Map<String, Object>> datiStudente = DAOStudente.getDatiPerMatricola(matricola, connection);
        return creaStudente(datiStudente);
    }

    public static List<ISetCommand> creaStudenteDaCredenziali(String nome, String cognome, Connection connection) throws SQLException {
        List<Map<String, Object>> datiStudente = DAOStudente.getDatiPerCredenziali(nome, cognome, connection);
        return creaStudente(datiStudente);
    }

    public static List<ISetCommand> creaStudente(List<Map<String, Object>> datiStudenti) {
        if(datiStudenti == null){
            return null;
        }
        List<ISetCommand> studenti = new ArrayList<>();

        for (Map<String, Object> dati : datiStudenti) {
            ISetCommand studente = new Studente(
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

        return studenti;
    }

    public static ISetCommand creaDocente(String cf, Connection connection) throws SQLException {
        Map<String, String> datiDocente = DAODocente.getDatiDocente(cf, connection);
        return new Docente(cf, datiDocente.get("nome"), datiDocente.get("cognome"), datiDocente.get("email"));
    }

    public static ISetCommand creaSegreteria() {
        return new Segreteria();
    }
}
