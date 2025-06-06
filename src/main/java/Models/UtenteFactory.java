package Models;

import Dao.DAODocente;
import Dao.DAOStudente;
import Interfacce.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Classe di utilità per la creazione di istanze di utenti del sistema (Studenti, Docenti, Segreteria).
 * Fornisce metodi statici per istanziare gli oggetti appropriati a partire da dati provenienti dal database.
 */
public class UtenteFactory {

    /**
     * Crea una lista di oggetti Studente a partire da una matricola specifica.
     *
     * @param matricola  la matricola dello studente
     * @param connection la connessione al database
     * @return una lista di studenti trovati associati alla matricola
     * @throws SQLException se si verifica un errore durante l'accesso al database
     */
    public static List<ISetCommand> creaStudenteDaMatricola(String matricola, Connection connection) throws SQLException {
        List<Map<String, Object>> datiStudente = DAOStudente.getDatiPerMatricola(matricola, connection);
        return creaStudente(datiStudente);
    }

    /**
     * Crea una lista di oggetti Studente a partire da nome e cognome.
     *
     * @param nome       il nome dello studente
     * @param cognome    il cognome dello studente
     * @param connection la connessione al database
     * @return una lista di studenti trovati con le credenziali specificate
     * @throws SQLException se si verifica un errore durante l'accesso al database
     */
    public static List<ISetCommand> creaStudenteDaCredenziali(String nome, String cognome, Connection connection) throws SQLException {
        List<Map<String, Object>> datiStudente = DAOStudente.getDatiPerCredenziali(nome, cognome, connection);
        return creaStudente(datiStudente);
    }

    /**
     * Crea una lista di oggetti Studente da una lista di mappe contenenti dati anagrafici ed esami.
     *
     * @param datiStudenti lista di mappe con i dati degli studenti
     * @return lista di oggetti Studente implementanti {@link ISetCommand}
     */
    public static List<ISetCommand> creaStudente(List<Map<String, Object>> datiStudenti) {
        if (datiStudenti == null) {
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

    /**
     * Crea un oggetto Docente dato un codice fiscale.
     *
     * @param cf         codice fiscale del docente
     * @param connection connessione al database
     * @return oggetto Docente implementante {@link ISetCommand}
     * @throws SQLException se si verifica un errore durante l'accesso al database
     */
    public static ISetCommand creaDocente(String cf, Connection connection) throws SQLException {
        Map<String, String> datiDocente = DAODocente.getDatiDocente(cf, connection);
        return new Docente(cf, datiDocente.get("nome"), datiDocente.get("cognome"), datiDocente.get("email"));
    }

    /**
     * Crea un oggetto Segreteria.
     *
     * @return istanza della classe {@link Segreteria} che implementa {@link ISetCommand}
     */
    public static ISetCommand creaSegreteria() {
        return new Segreteria();
    }
}
