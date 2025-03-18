package Controllers;

import Interfacce.ICambioScena;
import Interfacce.ISetCommand;
import Models.UtenteFactory;
import Utils.UtilGestoreScena;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

import java.sql.SQLException;

public class ControllerStudente extends ControllerLogin implements ICambioScena {
    protected ISetCommand studente;

    @FXML public Button     PrenotazioniButton;
    @FXML public Button     EsitiEsamiButton;
    @FXML public Button     PianoStudiButton;
    @FXML public AnchorPane contenitoreSubView;


    @FXML public void switchForm(javafx.event.ActionEvent actionEvent) {
        UtilGestoreScena.switchForm(actionEvent, this);
    }

    @FXML public void handleExit(javafx.event.ActionEvent event) {
        UtilGestoreScena.handleExit(event, connection);
    }

    public void creaStudente(String matricola) throws SQLException {
        studente = UtenteFactory.creaStudenteDaMatricola(matricola, connection).get(0);
    }

}
