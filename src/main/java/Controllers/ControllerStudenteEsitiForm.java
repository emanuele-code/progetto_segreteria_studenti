package Controllers;

import Commands.CommandConfermaVoti;
import Commands.CommandGetEsiti;
import Interfacce.IControllerBase;
import Models.StateItem;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ControllerStudenteEsitiForm implements IControllerBase<ControllerStudente> {

    private ControllerStudente controllerStudente;

    @FXML public TableView<StateItem>           TableEsiti;
    @FXML public TableColumn<StateItem, String> ColonnaNomeEsame;
    @FXML public TableColumn<StateItem, String> ColonnaCFU;
    @FXML public TableColumn<StateItem, String> ColonnaVoto;
    @FXML public TableColumn<StateItem, String> ColonnaDocente;
    @FXML public TableColumn<StateItem, String> ColonnaData;
    @FXML public TableColumn<StateItem, Void>   ColonnaConferma;

    @FXML public Label       labelVoto;
    @FXML public RadioButton AccettaVotoRadioButton;
    @FXML public RadioButton RifiutaVotoRadioButton;

    private ToggleGroup votoToggleGroup;


    @Override
    public void setController(ControllerStudente controller) throws SQLException {
        this.controllerStudente = controller;
    }


    @FXML
    public void initialize() {
        Platform.runLater(() -> {
            try {
                confermaVoti();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

    }


    private void setToggle(){
        votoToggleGroup = new ToggleGroup();

        AccettaVotoRadioButton.setToggleGroup(votoToggleGroup);
        RifiutaVotoRadioButton.setToggleGroup(votoToggleGroup);
    }


    private void configuraColonne(){
        ColonnaNomeEsame.setCellValueFactory(cellData -> cellData.getValue().getCampo("nomeEsame"));
        ColonnaCFU.setCellValueFactory(cellData -> cellData.getValue().getCampo("CFU"));
        ColonnaVoto.setCellValueFactory(cellData -> cellData.getValue().getCampo("voto"));
        ColonnaDocente.setCellValueFactory(cellData -> cellData.getValue().getCampo("credenziali"));
        ColonnaData.setCellValueFactory(cellData -> cellData.getValue().getCampo("dataAppello"));
        ColonnaConferma.setCellFactory(param -> creaBottoneInserimentoVoto());
    }


    private List<StateItem> recuperaEsiti() throws SQLException {
        controllerStudente.studente.setCommand(new CommandGetEsiti(controllerStudente.connection, controllerStudente.studente.getMatricola()));
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


    public void confermaVoti() throws SQLException {
        configuraColonne();
        ObservableList<StateItem> listaStateItem = FXCollections.observableArrayList(recuperaEsiti());
        ColonnaConferma.setCellFactory(param -> creaBottoneInserimentoVoto());
        TableEsiti.setItems(listaStateItem);
    }


    public TableCell<StateItem, Void> creaBottoneInserimentoVoto(){
        return new TableCell<>() {
            private final Button btn = new Button("Visualizza");

            @Override
            protected void updateItem(java.lang.Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(btn);
                    btn.setOnAction(event -> {
                        StateItem selectedItem = getTableRow().getItem();
                        showInputAlert(selectedItem);
                    });
                }
            }
        };
    }


    private String getSelectedToggleValue() {
        RadioButton selectedRadioButton = (RadioButton) votoToggleGroup.getSelectedToggle();
        return selectedRadioButton.getText().equals("Accetta Voto") ? "accettato" : "rifiutato";
    }


    public void showInputAlert(StateItem selectedItem) {
        try {
            // Carica il file FXML del contenuto del dialogo
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/StudenteFXML/FormAccettazioneVoto.fxml"));
            Parent content = loader.load();

            // Usa lo stesso controller attuale
            ControllerStudenteEsitiForm currentController = loader.getController();
            currentController.setController(this.controllerStudente);

            currentController.setDati((String) selectedItem.getCampo("voto").get());
            currentController.setToggle();

            Alert alert = showAlertPage(content);

            // Mostra l'Alert e ottieni il risultato
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // Recupera i dati dall'attuale controller
                String sceltaVoto = currentController.getSelectedToggleValue();
                confermaVoto(selectedItem, sceltaVoto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private Alert showAlertPage(Parent content){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Inserisci Voto");
        alert.setHeaderText("Compila i dettagli della votazione");
        alert.getDialogPane().setContent(content); // Imposta il contenuto FXML
        return alert;
    }


    private void confermaVoto(StateItem item, String scelta) throws SQLException {
        String numeroAppello = (String) item.getCampo("numero_appello").get();
        String matricola     = controllerStudente.studente.getMatricola();

        controllerStudente.studente.setCommand(new CommandConfermaVoti(controllerStudente.connection, scelta, matricola, numeroAppello));
        controllerStudente.studente.eseguiAzione();
    }


    private void setDati(String voto){
        labelVoto.setText(voto);
    }
}
