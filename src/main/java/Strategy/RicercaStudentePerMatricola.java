package Strategy;

import Interfacce.IRicercaStudenteStrategy;
import Interfacce.ISetCommand;
import Models.UtenteFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Strategia di ricerca studente basata sulla matricola.
 * <p>
 * Implementa l'interfaccia {@link IRicercaStudenteStrategy} per
 * effettuare la ricerca utilizzando la matricola dello studente.
 * </p>
 * <p>
 * Utilizza {@link UtenteFactory} per creare gli oggetti studente dai dati recuperati.
 * </p>
 *
 * @author Emanuele Pacilio
 * @version 1.0
 */
public class RicercaStudentePerMatricola implements IRicercaStudenteStrategy {

    private String matricola;

    /**
     * Costruisce la strategia di ricerca con la matricola specificata.
     *
     * @param matricola Matricola dello studente da ricercare.
     */
    public RicercaStudentePerMatricola(String matricola) {
        this.matricola = matricola;
    }

    /**
     * Esegue la ricerca dello studente nel database tramite matricola.
     *
     * @param connection Connessione al database da utilizzare per la ricerca.
     * @return Lista di comandi {@link ISetCommand} che rappresentano gli studenti trovati.
     * @throws SQLException se si verifica un errore durante la ricerca nel database.
     */
    @Override
    public List<ISetCommand> ricercaStudente(Connection connection) throws SQLException {
        return UtenteFactory.creaStudenteDaMatricola(matricola, connection);
    }
}
