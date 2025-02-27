package Controllers;

import Commands.CommandGetEsamiInsegnati;
import Interfacce.ICambioScena;
import Interfacce.IDocente;
import Models.UtenteFactory;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;
import java.sql.SQLException;
import java.util.Map;

public class ControllerDocente extends ControllerDB implements ICambioScena {
    protected IDocente docente;
    protected Map<String, String> mappa;

    @FXML public AnchorPane contenitoreSubView;
    @FXML public Button inserisciAppelloButton;
    @FXML public Button inserisciVotoButton;


    @FXML public void switchForm(javafx.event.ActionEvent actionEvent) throws SQLException {
        ControllerGestoreScena.switchForm(actionEvent, this);
    }

    @FXML public void handleExit(javafx.event.ActionEvent event) {
        ControllerGestoreScena.handleExit(event, connection);
    }

    protected String convertiNomeToCodicePiano(ComboBox<String> comboBox){
        String codicePiano = null;
        for (Map.Entry<String, String> entry : mappa.entrySet()) {

            if (entry.getValue().equals(comboBox.getValue())) {
                codicePiano = entry.getKey().toString();
                break;
            }
        }

        return codicePiano;
    }

    protected Map<String, String> caricaEsami(ComboBox<String> comboBox) throws SQLException {
        docente.setCommand(new CommandGetEsamiInsegnati(connection, docente.getCf()));
        mappa = (Map<String, String>)docente.eseguiAzione();


        for (Map.Entry<String, String> entry : mappa.entrySet()) {
            comboBox.getItems().add(entry.getValue());
        }

        return mappa;
    }

    protected void setCodiceFiscale(String codiceFiscale) throws SQLException {
        docente = UtenteFactory.creaDocente(codiceFiscale, connection);
    }


}
