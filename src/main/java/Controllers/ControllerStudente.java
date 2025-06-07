package Controllers;

import Interfacce.ICambioScena;
import Interfacce.ISetCommand;
import Models.UtenteFactory;
import Utils.DatabaseSegreteria;
import Utils.UtilGestoreScena;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

import java.sql.SQLException;

/**
 * Controller dedicato all'interfaccia studente. Estende {@link ControllerLogin} per accedere
 * ai dati di connessione e implementa {@link ICambioScena} per la gestione della navigazione tra viste.
 */
public class ControllerStudente extends ControllerLogin implements ICambioScena {
    protected ISetCommand studente;

    @FXML public Button     PrenotazioniButton;
    @FXML public Button     EsitiEsamiButton;
    @FXML public Button     PianoStudiButton;
    @FXML public AnchorPane contenitoreSubView;


    /**
     * Cambia la vista all'interno dell'interfaccia studente.
     * Invocato al click dei pulsanti per navigare tra i form.
     *
     * @param actionEvent evento generato dal pulsante cliccato
     */
    @FXML
    public void switchForm(javafx.event.ActionEvent actionEvent) {
        UtilGestoreScena.switchForm(actionEvent, this);
    }

    /**
     * Gestisce l'uscita sicura dall'applicazione.
     *
     * @param event evento generato dal pulsante di logout/uscita
     */
    @FXML
    public void handleExit(javafx.event.ActionEvent event) {
        UtilGestoreScena.handleExit(event, connessione);
    }

    /**
     * Crea l'istanza dello studente a partire dalla matricola specificata.
     * Usa {@link UtenteFactory} per ottenere l'oggetto studente associato.
     *
     * @param matricola codice identificativo dello studente
     * @throws SQLException in caso di errore nel recupero dal database
     */
    public void creaStudente(String matricola) throws SQLException {
        studente = UtenteFactory.creaStudenteDaMatricola(matricola, DatabaseSegreteria.getConnection()).get(0);
    }
}
