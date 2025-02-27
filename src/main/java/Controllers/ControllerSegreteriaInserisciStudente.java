package Controllers;

import Commands.CommandInserisciStudente;
import Interfacce.ControllerBase;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;

public class ControllerSegreteriaInserisciStudente implements ControllerBase<ControllerSegreteria> {

    private ControllerSegreteria controllerSegreteria;

    @FXML private TextField        TextNomeInserisciStudente;
    @FXML private TextField        TextCognomeInserisciStudente;
    @FXML private TextField        TextResidenzaInserisciStudente;
    @FXML private TextField        TextPasswordInserisciStudente;
    @FXML private DatePicker       DataNascitaInserisciStudente;
    @FXML private ComboBox<String> PianoStudiDropDown;


    public void setController(ControllerSegreteria controllerSegreteria) throws SQLException {
        this.controllerSegreteria = controllerSegreteria;
        controllerSegreteria.caricaPianiDiStudio(PianoStudiDropDown);
    }


    private boolean validaStudente(){
        return  TextNomeInserisciStudente.getText().trim().isEmpty() ||
                TextCognomeInserisciStudente.getText().trim().isEmpty() ||
                TextResidenzaInserisciStudente.getText().trim().isEmpty() ||
                DataNascitaInserisciStudente.getValue() == null ||
                TextPasswordInserisciStudente.getText().trim().isEmpty() ||
                PianoStudiDropDown.getSelectionModel().getSelectedItem() == null;
    }

    @FXML public void inserisciStudente(javafx.event.ActionEvent actionEvent) throws SQLException {
        System.out.println(validaStudente());
        if (validaStudente()) {
            ControllerAlert.mostraErrore("Compila tutti i campi prima di inserire lo studente.", "Errore", "Campi mancanti o errati");
            return;
        }


        String codicePiano = controllerSegreteria.convertiNomeToCodicePiano(PianoStudiDropDown);

        String[] dati = new String[6];
        dati[0] = TextNomeInserisciStudente.getText().trim();
        dati[1] = TextCognomeInserisciStudente.getText().trim();
        dati[2]  = DataNascitaInserisciStudente.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        dati[3] = TextResidenzaInserisciStudente.getText().trim();
        dati[4] = codicePiano.toString();
        dati[5]  = TextPasswordInserisciStudente.getText().trim();

        controllerSegreteria.segreteria.setCommand(new CommandInserisciStudente(controllerSegreteria.connection, dati));
        controllerSegreteria.segreteria.eseguiAzione();
    }

}
