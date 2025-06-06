package Interfacce;

import java.sql.SQLException;
import javafx.event.ActionEvent;

/**
 * Interfaccia per la gestione del cambio di scena e dell'uscita nell'applicazione JavaFX.
 */
public interface ICambioScena {

    /**
     * Cambia la scena corrente in base all'evento di azione fornito.
     *
     * @param actionEvent evento che ha generato la richiesta di cambio scena
     * @throws SQLException se si verifica un errore di accesso al database durante il cambio scena
     */
    void switchForm(ActionEvent actionEvent) throws SQLException;

    /**
     * Gestisce l'evento di uscita dall'applicazione o dalla schermata corrente.
     *
     * @param event evento che ha generato la richiesta di uscita
     */
    void handleExit(ActionEvent event);
}
