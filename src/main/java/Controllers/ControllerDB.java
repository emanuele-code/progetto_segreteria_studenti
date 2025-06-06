package Controllers;

import java.sql.Connection;

/**
 * Classe astratta base per i controller che gestiscono l'accesso al database.
 * <p>
 * Fornisce un metodo per impostare una connessione {@link Connection} che può
 * essere utilizzata dalle classi derivate per interagire con il database.
 * </p>
 *
 * @author Emanuele Pacilio
 * @version 1.0
 */
public abstract class ControllerDB {
    protected Connection connection;

    /**
     * Imposta la connessione al database.
     *
     * @param connection l'oggetto {@link Connection} da utilizzare per l'accesso al database
     */
    public void setConnection(Connection connection) {
        this.connection = connection;
    }
}
