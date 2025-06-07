package Controllers;

import Commands.CommandCambiaPianoStudente;
import Interfacce.IControllerBase;
import Utils.UtilAlert;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.sql.SQLException;

/**
 * Controller responsabile del cambio piano di studi per uno studente.
 * È una subview gestita dalla segreteria.
 */
public class ControllerSegreteriaCambioPiano implements IControllerBase<ControllerSegreteria> {

    /**
     * Controller principale della segreteria, usato per accedere a metodi e connessione.
     */
    private ControllerSegreteria controllerSegreteria;

    @FXML public ComboBox<String> CambiaPianoStudiDropDown;
    @FXML public TextField TextMatricolaCambiaPiano;

    /**
     * Inizializza la vista caricando i piani di studio nella {@link ComboBox}.
     * Questo viene eseguito all'avvio della subview.
     */
    @FXML
    public void initialize() {
        Platform.runLater(() -> {
            try {
                controllerSegreteria.caricaPianiDiStudio(CambiaPianoStudiDropDown);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Imposta il controller principale della segreteria.
     *
     * @param controllerSegreteria riferimento al controller padre
     */
    @Override
    public void setController(ControllerSegreteria controllerSegreteria) {
        this.controllerSegreteria = controllerSegreteria;
    }

    /**
     * Cambia il piano di studi dello studente usando il comando {@link CommandCambiaPianoStudente}.
     * Verifica che tutti i campi siano correttamente compilati prima di eseguire l'azione.
     *
     * @throws SQLException se si verifica un errore nell'esecuzione del comando
     */
    @FXML
    public void cambiaPianoStudi() throws SQLException {
        // Validazione input
        if (CambiaPianoStudiDropDown.getSelectionModel().getSelectedItem() == null ||
                TextMatricolaCambiaPiano.getText().trim().isEmpty()) {

            UtilAlert.mostraErrore(
                    "Compila tutti i campi prima di inserire lo studente.",
                    "Errore",
                    "Campi mancanti o errati"
            );
            return;
        }

        String codicePiano = controllerSegreteria.convertiNomeToCodicePiano(CambiaPianoStudiDropDown);
        String matricola = TextMatricolaCambiaPiano.getText().trim();

        controllerSegreteria.segreteria.setCommand(
                new CommandCambiaPianoStudente(controllerSegreteria.connessione, matricola, codicePiano)
        );
        controllerSegreteria.segreteria.eseguiAzione();

        UtilAlert.mostraInfo(
                "Il piano di studi è stato cambiato con successo per la matricola:",
                "Conferma Cambio Piano",
                "Cambio Piano completato",
                matricola
        );
    }
}
