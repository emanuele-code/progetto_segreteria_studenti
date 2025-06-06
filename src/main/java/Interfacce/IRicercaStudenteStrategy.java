package Interfacce;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Interfaccia che definisce la strategia per la ricerca di studenti nel database.
 */
public interface IRicercaStudenteStrategy {

    /**
     * Esegue la ricerca dello studente utilizzando la connessione fornita.
     *
     * @param connection la connessione al database da utilizzare per la ricerca
     * @return una lista di oggetti ISetCommand che rappresentano i risultati della ricerca
     * @throws SQLException in caso di errori durante l'accesso al database
     */
    List<ISetCommand> ricercaStudente(Connection connection) throws SQLException;
}
