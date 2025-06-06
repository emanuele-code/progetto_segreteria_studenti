package Controllers;

import Commands.CommandGetPianiStudio;
import Interfacce.ICambioScena;
import Interfacce.ISetCommand;
import Models.UtenteFactory;
import Utils.UtilGestoreScena;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.sql.SQLException;
import java.util.Map;

/**
 * Controller dedicato alla gestione della pagina principale della Segreteria.
 * Estende {@link ControllerLogin} ed implementa {@link ICambioScena} per gestire il cambio delle sub-view.
 */
public class ControllerSegreteria extends ControllerLogin implements ICambioScena {
    protected ISetCommand segreteria = UtenteFactory.creaSegreteria();
    protected Map<Integer, String> mappa;

    @FXML public Button cercaStudenteButton;
    @FXML public Button confermaVotiButton;
    @FXML public Button inserisciStudenteButton;
    @FXML public Button cambiaPianoButton;
    @FXML public AnchorPane contenitoreSubView;

    /**
     * Gestisce lo switch tra subform/view all'interno della pagina Segreteria.
     *
     * @param actionEvent evento generato dal pulsante che richiama il cambio form
     */
    @FXML
    public void switchForm(javafx.event.ActionEvent actionEvent) {
        UtilGestoreScena.switchForm(actionEvent, this);
    }

    /**
     * Gestisce la chiusura dell'applicazione o il logout.
     *
     * @param event evento di uscita (click su logout/chiudi)
     */
    @FXML
    public void handleExit(javafx.event.ActionEvent event) {
        UtilGestoreScena.handleExit(event, connection);
    }

    /**
     * Converte il nome di un piano di studio selezionato nel {@link ComboBox}
     * nel relativo codice (ID del piano).
     *
     * @param comboBox combo box contenente i nomi dei piani di studio
     * @return codice (ID) del piano selezionato, oppure {@code null} se non trovato
     */
    protected String convertiNomeToCodicePiano(ComboBox<String> comboBox) {
        String codicePiano = null;
        for (Map.Entry<Integer, String> entry : mappa.entrySet()) {
            if (entry.getValue().equals(comboBox.getValue())) {
                codicePiano = entry.getKey().toString();
                break;
            }
        }
        return codicePiano;
    }

    /**
     * Carica tutti i piani di studio dal database e li inserisce nel {@link ComboBox} fornito.
     * Inoltre aggiorna la mappa interna per future conversioni nome ↔ codice.
     *
     * @param comboBox combo box da popolare con i nomi dei piani di studio
     * @return mappa contenente l'associazione codice ↔ nome dei piani
     * @throws SQLException se si verifica un errore nel recupero dei dati dal database
     */
    protected Map<Integer, String> caricaPianiDiStudio(ComboBox<String> comboBox) throws SQLException {
        segreteria.setCommand(new CommandGetPianiStudio(connection));
        mappa = (Map<Integer, String>) segreteria.eseguiAzione();
        comboBox.getItems().clear();

        for (Map.Entry<Integer, String> entry : mappa.entrySet()) {
            comboBox.getItems().add(entry.getValue());
        }

        return mappa;
    }
}
