package Controllers;

import Commands.CommandConfermaVoti;
import Commands.CommandGetEsiti;
import Interfacce.IControllerBase;
import Interfacce.IGetterStudente;
import Models.StateItem;
import Utils.UtilGestoreScena;
import Utils.UtilGestoreTableView;
import Utils.UtilAlert;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.util.Pair;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

/**
 * Controller per la gestione della form degli esiti degli esami dello studente.
 * Permette di visualizzare gli esiti, accettare o rifiutare i voti, e confermare la scelta al sistema.
 * <p>
 * Implementa {@link IControllerBase} per l'iniezione del controller principale {@link ControllerStudente}.
 * Utilizza il pattern Command per ottenere e confermare esiti.
 */
public class ControllerStudenteEsitiForm implements IControllerBase<ControllerStudente> {

    private ControllerStudente controllerStudente;
    private ToggleGroup votoToggleGroup;
    @FXML public TableView<StateItem> TableEsiti;
    @FXML public Label labelVoto;
    @FXML public RadioButton AccettaVotoRadioButton;
    @FXML public RadioButton RifiutaVotoRadioButton;

    /**
     * Inietta il controller dello studente.
     *
     * @param controller Il controller da collegare.
     * @throws SQLException in caso di errori DB.
     */
    @Override
    public void setController(ControllerStudente controller) throws SQLException {
        this.controllerStudente = controller;
    }

    /**
     * Inizializza la tabella con gli esiti degli esami.
     * Viene eseguito automaticamente da JavaFX.
     */
    @FXML
    public void initialize() {
        Platform.runLater(() -> {
            try {
                Map<String, Function<StateItem, ?>> colonneMappa = Map.of(
                        "Esame", item -> item.getCampo("nome_esame").get(),
                        "CFU", item -> item.getCampo("CFU").get(),
                        "voto", item -> item.getCampo("voto").get(),
                        "Docente", item -> item.getCampo("credenziali_docente").get(),
                        "Data Appello", item -> item.getCampo("data_appello").get()
                );

                UtilGestoreTableView.configuraColonne(TableEsiti, colonneMappa);

                UtilGestoreTableView.aggiungiColonnaBottone(TableEsiti, "Conferma",
                        item -> true,
                        item -> "Visualizza",
                        item -> () -> mostraInputAlert(item)
                );

                ObservableList<StateItem> listaStateItem = FXCollections.observableArrayList(recuperaEsiti());
                TableEsiti.setItems(listaStateItem);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Ritorna il valore selezionato tra i radio button.
     *
     * @return "accettato" o "rifiutato" in base alla selezione.
     */
    private String getSelectedToggleValue() {
        RadioButton selectedRadioButton = (RadioButton) votoToggleGroup.getSelectedToggle();
        return selectedRadioButton.getText().equals("Accetta Voto") ? "accettato" : "rifiutato";
    }

    /**
     * Inizializza il gruppo di toggle per i radio button di scelta voto.
     */
    private void setToggle() {
        votoToggleGroup = new ToggleGroup();

        AccettaVotoRadioButton.setToggleGroup(votoToggleGroup);
        RifiutaVotoRadioButton.setToggleGroup(votoToggleGroup);
    }

    /**
     * Recupera gli esiti dello studente dal database.
     *
     * @return lista di esiti come {@link StateItem}.
     * @throws SQLException se la query fallisce.
     */
    private List<StateItem> recuperaEsiti() throws SQLException {
        controllerStudente.studente.setCommand(
                new CommandGetEsiti(controllerStudente.connessione,
                        ((IGetterStudente) controllerStudente.studente).getMatricola())
        );

        List<Map<String, Object>> listaEsiti = (List<Map<String, Object>>) controllerStudente.studente.eseguiAzione();

        ObservableList<StateItem> listStateItems = FXCollections.observableArrayList();
        for (Map<String, Object> esiti : listaEsiti) {
            StateItem item = new StateItem();
            item.setCampo("nome_esame",          (String) esiti.get("nome_esame"));
            item.setCampo("CFU",                 (String) esiti.get("CFU"));
            item.setCampo("voto",                (String) esiti.get("voto"));
            item.setCampo("credenziali_docente", (String) esiti.get("credenziali_docente"));
            item.setCampo("data_appello",        (String) esiti.get("data_appello"));
            item.setCampo("numero_appello",      (String) esiti.get("numero_appello"));
            listStateItems.add(item);
        }
        return listStateItems;
    }

    /**
     * Mostra una finestra modale per accettare o rifiutare il voto selezionato.
     *
     * @param selectedItem elemento selezionato dalla tabella.
     */
    public void mostraInputAlert(StateItem selectedItem) {
        try {
            Pair<Parent, ControllerStudenteEsitiForm> risultatoFXML =
                    UtilGestoreScena.caricaFXML("/StudenteFXML/FormAccettazioneVoto.fxml");
            ControllerStudenteEsitiForm currentController = risultatoFXML.getValue();
            currentController.setController(this.controllerStudente);

            currentController.setDati((String) selectedItem.getCampo("voto").get());
            currentController.setToggle();

            Alert alert = UtilAlert.mostraPagina("Inserisci Voto",
                    "Compila i dettagli della votazione", risultatoFXML.getKey());

            Optional<ButtonType> risultato = alert.showAndWait();
            if (risultato.isPresent() && risultato.get() == ButtonType.OK) {
                String sceltaVoto = currentController.getSelectedToggleValue();
                confermaVoto(selectedItem, sceltaVoto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Invia al database la conferma o rifiuto del voto per l'appello selezionato.
     *
     * @param item   l'esito selezionato.
     * @param scelta "accettato" o "rifiutato".
     * @throws SQLException in caso di errore.
     */
    private void confermaVoto(StateItem item, String scelta) throws SQLException {
        String numeroAppello = (String) item.getCampo("numero_appello").get();
        String matricola     = ((IGetterStudente) controllerStudente.studente).getMatricola();

        controllerStudente.studente.setCommand(
                new CommandConfermaVoti(controllerStudente.connessione, scelta, matricola, numeroAppello));
        controllerStudente.studente.eseguiAzione();
    }

    /**
     * Imposta il testo del voto nella finestra modale.
     *
     * @param voto valore del voto da visualizzare.
     */
    private void setDati(String voto) {
        labelVoto.setText(voto);
    }
}
