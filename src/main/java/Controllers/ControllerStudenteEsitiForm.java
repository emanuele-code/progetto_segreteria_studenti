package Controllers;

import Commands.CommandConfermaVoti;
import Commands.CommandGetEsiti;
import Interfacce.ControllerBase;
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

public class ControllerStudenteEsitiForm implements ControllerBase<ControllerStudente> {

    private ControllerStudente controllerStudente;

    @FXML private TableView   TableEsiti;
    @FXML private TableColumn ColonnaNomeEsame;
    @FXML private TableColumn ColonnaCFU;
    @FXML private TableColumn ColonnaVoto;
    @FXML private TableColumn ColonnaDocente;
    @FXML private TableColumn ColonnaData;
    @FXML private TableColumn ColonnaConferma;
    @FXML private Label       labelVoto;
    @FXML private RadioButton AccettaVotoRadioButton;
    @FXML private RadioButton RifiutaVotoRadioButton;

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
        ColonnaNomeEsame.setCellValueFactory(new PropertyValueFactory<>("nomeEsame"));
        ColonnaCFU.setCellValueFactory(new PropertyValueFactory<>("CFU"));
        ColonnaVoto.setCellValueFactory(new PropertyValueFactory<>("voto"));
        ColonnaDocente.setCellValueFactory(new PropertyValueFactory<>("credenziali"));
        ColonnaData.setCellValueFactory(new PropertyValueFactory<>("dataAppello"));
        ColonnaConferma.setCellFactory(param -> creaBottoneInserimentoVoto());
    }


    public class StateItem {

        private String nomeEsame;
        private String CFU;
        private String voto;
        private String credenziali;
        private String dataAppello;
        private String numeroAppello;

        public StateItem(String nomeEsame, String CFU, String voto, String credenziali, String dataAppello, String numeroAppello) {
            this.nomeEsame     = nomeEsame;
            this.CFU           = CFU;
            this.voto          = voto;
            this.credenziali   = credenziali;
            this.dataAppello   = dataAppello;
            this.numeroAppello = numeroAppello;
        }

        public String getNomeEsame(){
            return nomeEsame;
        }
        public String getVoto(){ return voto; }
        public String getCFU(){ return CFU; }
        public String getCredenziali(){ return credenziali; }
        public String getDataAppello(){ return dataAppello; }
        public String getNumeroAppello(){ return numeroAppello; }
    }


    private List<StateItem> recuperaEsiti() throws SQLException {
        controllerStudente.studente.setCommand(new CommandGetEsiti(controllerStudente.connection, controllerStudente.studente.getMatricola()));
        List<Map<String, Object>> listaEsiti = (List<Map<String, Object>>) controllerStudente.studente.eseguiAzione();

        ObservableList<ControllerStudenteEsitiForm.StateItem> listStateItems = FXCollections.observableArrayList();
        for (Map<String, Object> esiti : listaEsiti) {
            String nomeEsame     = (String) esiti.get("nome_esame");
            String CFU           = (String) esiti.get("CFU");
            String voto          = (String) esiti.get("voto");
            String credenziali   = (String) esiti.get("credenziali_docente");
            String dataAppello   = (String) esiti.get("data_appello");
            String numeroAppello = (String) esiti.get("numero_appello");

            listStateItems.add(new ControllerStudenteEsitiForm.StateItem(nomeEsame, CFU, voto, credenziali, dataAppello, numeroAppello));
        }
        return listStateItems;
    }


    public void confermaVoti() throws SQLException {
        configuraColonne();
        ObservableList<StateItem> listaStateItem = FXCollections.observableArrayList(recuperaEsiti());
        ColonnaConferma.setCellFactory(param -> creaBottoneInserimentoVoto());
        TableEsiti.setItems(listaStateItem);
    }


    public TableCell<ControllerStudenteEsitiForm.StateItem, Void> creaBottoneInserimentoVoto(){
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
                        ControllerStudenteEsitiForm.StateItem selectedItem = getTableRow().getItem();
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

            currentController.setDati(selectedItem.voto);
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
        String numeroAppello = item.getNumeroAppello();
        String matricola     = controllerStudente.studente.getMatricola();

        controllerStudente.studente.setCommand(new CommandConfermaVoti(controllerStudente.connection, scelta, matricola, numeroAppello));
        controllerStudente.studente.eseguiAzione();
    }


    private void setDati(String voto){
        labelVoto.setText(voto);
    }
}
