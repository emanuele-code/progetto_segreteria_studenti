package Controllers;


import Commands.CommandGetPianiStudio;
import Interfacce.ICambioScena;
import Interfacce.ISegreteria;
import Models.UtenteFactory;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import java.util.Map;
import java.sql.SQLException;


public class ControllerSegreteria extends ControllerDB implements ICambioScena {
    protected ISegreteria segreteria = UtenteFactory.creaSegreteria(connection);
    protected Map<Integer, String> mappa;

    @FXML public Button     cercaStudenteButton;
    @FXML public Button     confermaVotiButton;
    @FXML public Button     inserisciStudenteButton;
    @FXML public Button     cambiaPianoButton;
    @FXML public AnchorPane contenitoreSubView;

    @FXML public void switchForm(javafx.event.ActionEvent actionEvent) {
        ControllerGestoreScena.switchForm(actionEvent, this);
    }

    @FXML public void handleExit(javafx.event.ActionEvent event) {
        ControllerGestoreScena.handleExit(event, connection);
    }

    protected String convertiNomeToCodicePiano(ComboBox<String> comboBox){
        String codicePiano = null;
        for (Map.Entry<Integer, String> entry : mappa.entrySet()) {

            if (entry.getValue().equals(comboBox.getValue())) {
                codicePiano = entry.getKey().toString();
                break;
            }
        }

        return codicePiano;
    }

    protected Map<Integer, String> caricaPianiDiStudio(ComboBox<String> comboBox) throws SQLException {
        segreteria.setCommand(new CommandGetPianiStudio(connection));
        mappa = (Map<Integer, String>)segreteria.eseguiAzione();
        comboBox.getItems().clear();

        for (Map.Entry<Integer, String> entry : mappa.entrySet()) {
            comboBox.getItems().add(entry.getValue());
        }
        return mappa;
    }



}
