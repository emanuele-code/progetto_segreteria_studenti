package Controllers;

import Commands.CommandGetEsamiInsegnati;
import Interfacce.ICambioScena;
import Interfacce.IGetterDocente;
import Interfacce.ISetCommand;
import Models.UtenteFactory;
import Utils.UtilGestoreScena;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;
import java.sql.SQLException;
import java.util.Map;

public class ControllerDocente extends ControllerLogin implements ICambioScena {
    protected ISetCommand docente;
    protected Map<String, String> mappa;

    @FXML public AnchorPane contenitoreSubView;
    @FXML public Button inserisciAppelloButton;
    @FXML public Button inserisciVotoButton;


    @FXML public void switchForm(javafx.event.ActionEvent actionEvent) {
        UtilGestoreScena.switchForm(actionEvent, this);
    }

    @FXML public void handleExit(javafx.event.ActionEvent event) {
        UtilGestoreScena.handleExit(event, connection);
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
        docente.setCommand(new CommandGetEsamiInsegnati(connection, ((IGetterDocente)docente).getCf()));
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
