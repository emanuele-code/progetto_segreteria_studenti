package Controllers;

import Commands.CommandCambiaPianoStudente;
import Interfacce.ControllerBase;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.sql.SQLException;

public class ControllerSegreteriaCambioPiano implements ControllerBase<ControllerSegreteria> {

    private ControllerSegreteria controllerSegreteria;
    @FXML private ComboBox<String> CambiaPianoStudiDropDown;
    @FXML private TextField TextMatricolaCambiaPiano;

    @FXML
    public void cambiaPianoStudi(javafx.event.ActionEvent actionEvent) throws SQLException {
        if (CambiaPianoStudiDropDown.getSelectionModel().getSelectedItem() == null || TextMatricolaCambiaPiano.getText().trim().isEmpty()) {
            ControllerAlert.mostraErrore("Compila tutti i campi prima di inserire lo studente.", "Errore", "Campi mancanti o errati");
            return;
        }

        String codicePiano = controllerSegreteria.convertiNomeToCodicePiano(CambiaPianoStudiDropDown);
        String matricola = TextMatricolaCambiaPiano.getText();

        controllerSegreteria.segreteria.setCommand(new CommandCambiaPianoStudente(controllerSegreteria.connection, matricola, codicePiano));
        controllerSegreteria.segreteria.eseguiAzione();

        ControllerAlert.mostraInfo("Il piano di studi è stato cambiato con successo per la matricola: ", "Conferma Cambio Piano", "Cambio Piano completato", matricola);
    }



    public void setController(ControllerSegreteria controllerSegreteria) throws SQLException {
        this.controllerSegreteria = controllerSegreteria;
        controllerSegreteria.caricaPianiDiStudio(CambiaPianoStudiDropDown);
    }
}