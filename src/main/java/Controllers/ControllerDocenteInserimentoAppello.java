package Controllers;

import Commands.CommandChiudiPrenotazione;
import Commands.CommandGetAppelliPerDocente;
import Commands.CommandInserisciAppello;
import Interfacce.IControllerBase;
import Models.StateItem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.TableView;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ControllerDocenteInserimentoAppello implements IControllerBase<ControllerDocente> {
    private ControllerDocente controllerDocente;

    @FXML private TableView<StateItem>           TabellaAppelli;
    @FXML private TableColumn<StateItem, String> ColonnaDataAppello;
    @FXML private TableColumn<StateItem, String> ColonnaEsame;
    @FXML private TableColumn<StateItem, String> ColonnaNumeroIscritti;
    @FXML private TableColumn<StateItem, String> ColonnaDisponibile;
    @FXML private TableColumn<StateItem, String> ColonnaDocente;
    @FXML private TableColumn<StateItem, String> ColonnaNumeroAppello;
    @FXML private TableColumn<StateItem, Void>   ColonnaChiudiAppello;

    @FXML private DatePicker       DataAppelloButton;
    @FXML private ComboBox<String> ScegliEsameLista;


    @FXML
    public void showInputAlert() {
        try {
            // Carica il file FXML del contenuto del dialogo
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/DocenteFXML/DocenteConfermaInserimentoAppello.fxml"));
            Parent content = loader.load();

            // Usa lo stesso controller attuale
            ControllerDocenteInserimentoAppello currentController = loader.getController();
            currentController.setController(this.controllerDocente);

            currentController.controllerDocente.caricaEsami(currentController.ScegliEsameLista);

            Alert alert = showAlertPage(content);

            // Mostra l'Alert e ottieni il risultato
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // Recupera i dati dall'attuale controller
                eseguiOperazione(currentController);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void eseguiOperazione(ControllerDocenteInserimentoAppello currentController) throws SQLException {
        String data = currentController.getSelectedData();
        String esame = currentController.controllerDocente.convertiNomeToCodicePiano(currentController.ScegliEsameLista);

        controllerDocente.docente.setCommand(new CommandInserisciAppello(controllerDocente.connection, data, esame, controllerDocente.docente.getCf()));
        controllerDocente.docente.eseguiAzione();
    }

    public String getSelectedData() {
        return DataAppelloButton.getValue() != null ? DataAppelloButton.getValue().toString() : "";
    }

    @Override
    public void setController(ControllerDocente controllerDocente) throws SQLException {
        this.controllerDocente = controllerDocente;
        valorizzaTabellaAppelli();
    }

    public void valorizzaTabellaAppelli() throws SQLException {
        configuraColonne();
        ObservableList<StateItem> listaStateItems = FXCollections.observableArrayList(recuperaAppelli());
        TabellaAppelli.setItems(listaStateItems);
    }

    private Alert showAlertPage(Parent content){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Inserisci Appello");
        alert.setHeaderText("Compila i dettagli dell'appello");
        alert.getDialogPane().setContent(content); // Imposta il contenuto FXML
        return alert;
    }

    private List<StateItem> recuperaAppelli() throws SQLException{
        controllerDocente.docente.setCommand(new CommandGetAppelliPerDocente(controllerDocente.connection, controllerDocente.docente.getCf()));
        List<Map<String, Object>> listaAppelli = (List<Map<String, Object>>) controllerDocente.docente.eseguiAzione();

        ObservableList<StateItem> listStateItems = FXCollections.observableArrayList();

        for (Map<String, Object> appello : listaAppelli) {
            StateItem item = new StateItem();

            item.setCampo("data_appello"        , (String) appello.get("data_appello"));
            item.setCampo("nome_esame"          , (String) appello.get("nome_esame"));
            item.setCampo("disponibile"         , (String) appello.get("disponibile"));
            item.setCampo("numero_iscritti"     , (String) appello.get("numero_iscritti"));
            item.setCampo("credenziali_docente" , (String)  appello.get("credenziali_docente"));
            item.setCampo("numero_appello"      , (String)  appello.get("numero_appello"));

            listStateItems.add(item);
        }
        return listStateItems;

    }

    private void configuraColonne(){
        ColonnaDataAppello.setCellValueFactory(cellData -> cellData.getValue().getCampo("data_appello"));
        ColonnaNumeroAppello.setCellValueFactory(cellData -> cellData.getValue().getCampo("numero_appello"));
        ColonnaEsame.setCellValueFactory(cellData -> cellData.getValue().getCampo("nome_esame"));
        ColonnaNumeroIscritti.setCellValueFactory(cellData -> cellData.getValue().getCampo("numero_iscritti"));
        ColonnaDisponibile.setCellValueFactory(cellData -> cellData.getValue().getCampo("disponibile"));
        ColonnaDocente.setCellValueFactory(cellData -> cellData.getValue().getCampo("credenziali_docente"));
        ColonnaChiudiAppello.setCellFactory(param -> creaBottoneConferma());
    }


    public TableCell<StateItem, Void> creaBottoneConferma(){
        return new TableCell<>() {
            private final Button btn = new Button("Chiudi");

            @Override
            protected void updateItem(java.lang.Void selectedItem, boolean empty) {
                super.updateItem(selectedItem, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(btn);  // Mostriamo il bottone

                    btn.setOnAction(event -> {
                        StateItem item = getTableRow().getItem();
                        chiudiAppello(item);
                    });
                }
            }
        };
    }

    @FXML
    private void chiudiAppello(StateItem item){
        String numeroAppello = item.getValore("numero_appello");

        Alert alert = ControllerAlert.mostraConferma("Quest azione è irreversibile", "Conferma Inserimento", "Sei sicuro di voler confermare l'inserimento?");
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    controllerDocente.docente.setCommand(new CommandChiudiPrenotazione(numeroAppello, controllerDocente.connection));
                    controllerDocente.docente.eseguiAzione();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }


}
