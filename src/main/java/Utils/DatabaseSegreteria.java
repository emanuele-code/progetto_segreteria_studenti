package Utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Classe utility per la gestione della connessione al database SQLite
 * utilizzato dall'applicazione Segreteria.
 * <p>
 * Implementa un singleton thread-safe per mantenere un'unica connessione condivisa.
 * </p>
 *
 * @author Emanuele Pacilio
 * @version 1.0
 */
public class DatabaseSegreteria {

    /**
     * URL di connessione al database SQLite.
     */
    private static final String URL = "jdbc:sqlite:DatabaseSegreteria.sqlite";

    /**
     * Connessione singleton al database.
     */
    private static Connection connection;

    /**
     * Costruttore privato per impedire l'istanza della classe.
     */
    private DatabaseSegreteria() {}

    /**
     * Restituisce la connessione al database.
     * <p>
     * Se non esiste ancora una connessione, la crea in modo thread-safe.
     *
     * @return la connessione attiva al database
     * @throws SQLException in caso di errori durante la connessione
     */
    public static Connection getConnection() throws SQLException {
        if (connection == null) {
            synchronized (DatabaseSegreteria.class) {
                if (connection == null) {
                    connection = DriverManager.getConnection(URL);
                }
            }
        }
        return connection;
    }
}
