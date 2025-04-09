package Controllers;

import Commands.CommandInserisciStudente;
import Interfacce.IControllerBase;
import Utils.UtilAlert;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ControllerSegreteriaInserisciStudente implements IControllerBase<ControllerSegreteria> {

    private ControllerSegreteria controllerSegreteria;

    @FXML private TextField        TextNomeInserisciStudente;
    @FXML private TextField        TextCognomeInserisciStudente;
    @FXML private TextField        TextResidenzaInserisciStudente;
    @FXML private TextField        TextPasswordInserisciStudente;
    @FXML private DatePicker       DataNascitaInserisciStudente;
    @FXML private ComboBox<String> PianoStudiDropDown;


    @FXML
    public void initialize() {
        Platform.runLater(() -> {
            try {
                controllerSegreteria.caricaPianiDiStudio(PianoStudiDropDown);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

    }


    @Override
    public void setController(ControllerSegreteria controllerSegreteria) throws SQLException {
        this.controllerSegreteria = controllerSegreteria;
    }


    private boolean validaStudente(){
        return  TextNomeInserisciStudente.getText().trim().isEmpty() ||
                TextCognomeInserisciStudente.getText().trim().isEmpty() ||
                TextResidenzaInserisciStudente.getText().trim().isEmpty() ||
                DataNascitaInserisciStudente.getValue() == null ||
                TextPasswordInserisciStudente.getText().trim().isEmpty() ||
                PianoStudiDropDown.getSelectionModel().getSelectedItem() == null;
    }


    @FXML public void inserisciStudente() throws SQLException {
        if (validaStudente()) {
            UtilAlert.mostraErrore("Compila tutti i campi prima di inserire lo studente.", "Errore", "Campi mancanti o errati");
            return;
        }


        String codicePiano = controllerSegreteria.convertiNomeToCodicePiano(PianoStudiDropDown);

        String[] dati = new String[6];
        dati[0] = TextNomeInserisciStudente.getText().trim();
        dati[1] = TextCognomeInserisciStudente.getText().trim();
        dati[2] = DataNascitaInserisciStudente.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        dati[3] = TextResidenzaInserisciStudente.getText().trim();
        dati[4] = codicePiano.toString();
        dati[5] = TextPasswordInserisciStudente.getText().trim();




        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate dataNascita = LocalDate.parse(dati[2], formatter);
        if(dataNascita.isAfter(LocalDate.now().minusYears(18))) {
            UtilAlert.mostraErrore("Lo studente deve avere almeno 18 anni per essere immatricolato", "Errore Studente minorenne", "lo studente deve avere almeno 18 anni");
            return;
        }

        controllerSegreteria.segreteria.setCommand(new CommandInserisciStudente(controllerSegreteria.connection, dati));
        controllerSegreteria.segreteria.eseguiAzione();
        UtilAlert.mostraInfo("Studente inserito con successo", "Inserimento studente confermato", "Lo studente è stato inserito",
                "Nome: " + dati[0]  + "\n" + "Cognome: " + dati[1]  + "\n" + "Residenza: " + dati[3]  + "\n" + "Data Nascita: " + dati[2]  + "\n");
    }

}
