package Strategy;

import Interfacce.IRicercaStudenteStrategy;
import Interfacce.ISetCommand;
import Models.UtenteFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Strategia di ricerca studente basata sulle credenziali (nome e cognome).
 * <p>
 * Implementa l'interfaccia {@link IRicercaStudenteStrategy} per
 * effettuare la ricerca utilizzando nome e cognome dello studente.
 * </p>
 *
 * <p>
 * Utilizza {@link UtenteFactory} per creare gli oggetti studente dai dati recuperati.
 * </p>
 *
 * @author Emanuele Pacilio
 * @version 1.0
 */
public class RicercaStudentePerCredenziali implements IRicercaStudenteStrategy {

    private String nome;
    private String cognome;

    /**
     * Costruisce la strategia di ricerca con nome e cognome specificati.
     *
     * @param nome       Nome dello studente da ricercare.
     * @param cognome    Cognome dello studente da ricercare.
     * @param connection Connessione al database (non utilizzata nel costruttore).
     */
    public RicercaStudentePerCredenziali(String nome, String cognome, Connection connection) {
        this.nome = nome;
        this.cognome = cognome;
    }

    /**
     * Esegue la ricerca dello studente nel database tramite nome e cognome.
     *
     * @param connection Connessione al database da utilizzare per la ricerca.
     * @return Lista di comandi {@link ISetCommand} che rappresentano gli studenti trovati.
     * @throws SQLException se si verifica un errore durante la ricerca nel database.
     */
    @Override
    public List<ISetCommand> ricercaStudente(Connection connection) throws SQLException {
        return UtenteFactory.creaStudenteDaCredenziali(nome, cognome, connection);
    }
}
