package Controllers;

import Commands.CommandInserisciVoto;
import Commands.CommandGetPrenotazioni;
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

public class ControllerDocenteInserisciVoti implements ControllerBase<ControllerDocente> {

    @FXML private TableView   TableInserisciVoti;
    @FXML private TableColumn ColonnaMatricola;
    @FXML private TableColumn ColonnaEsame;
    @FXML private TableColumn ColonnaVoto;
    @FXML private TableColumn ColonnaNumeroAppello;
    @FXML private TableColumn ColonnaDataAppello;
    @FXML private TableColumn ColonnaInserisciVoto;
    @FXML private Label       labelMatricola;
    @FXML private Label       labelEsame;
    @FXML private ComboBox<String>    comboVoti;

    private ControllerDocente controllerDocente;

    @FXML
    public void initialize() {
        // Attendere che JavaFX abbia caricato completamente la UI
        Platform.runLater(() -> {
            ObservableList<String> voti = FXCollections.observableArrayList(
                    "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "30L", "Assente", "Non Ammesso"
            );
            comboVoti.setItems(voti); // Ora comboVoti NON è null
        });
    }

    @Override
    public void setController(ControllerDocente controllerDocente) throws SQLException {
        this.controllerDocente = controllerDocente;
    }

    public void valorizzaTabellaAppelli() throws SQLException {
        configuraColonne();
        ObservableList<ControllerDocenteInserisciVoti.StateItem> listaStateItems = FXCollections.observableArrayList(recuperaPrenotati());
        TableInserisciVoti.setItems(listaStateItems);
    }

    public class StateItem {

        private String matricola;
        private String nomeEsame;
        private String voto;
        private String numeroAppello;
        private String dataAppello;

        public StateItem(String matricola, String nomeEsame, String voto, String numeroAppello, String dataAppello) {
            this.matricola     = matricola;
            this.nomeEsame     = nomeEsame;
            this.voto          = voto;
            this.numeroAppello = numeroAppello;
            this.dataAppello   = dataAppello;
        }

        public String getMatricola(){
            return matricola;
        }
        public String getNomeEsame(){
            return nomeEsame;
        }
        public String getVoto(){ return voto; }
        public String getNumeroAppello(){ return numeroAppello; }
        public String getDataAppello(){ return dataAppello; }
    }

    public TableCell<ControllerDocenteInserisciVoti.StateItem, Void> creaBottoneInserimentoVoto(){
        return new TableCell<>() {
            private final Button btn = new Button("Inserisci");

            @Override
            protected void updateItem(java.lang.Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    StateItem rowData = getTableRow().getItem();
                    if(rowData != null && rowData.getVoto() != null){
                        setGraphic(null);
                    } else {
                        setGraphic(btn);  // Mostriamo il bottone

                        btn.setOnAction(event -> {
                            ControllerDocenteInserisciVoti.StateItem selectedItem = getTableRow().getItem();
                            showInputAlert(selectedItem);
                        });
                    }
                }
            }
        };
    }

    private void configuraColonne(){
        ColonnaMatricola.setCellValueFactory(new PropertyValueFactory<>("matricola"));
        ColonnaEsame.setCellValueFactory(new PropertyValueFactory<>("nomeEsame"));
        ColonnaVoto.setCellValueFactory(new PropertyValueFactory<>("voto"));
        ColonnaNumeroAppello.setCellValueFactory(new PropertyValueFactory<>("numeroAppello"));
        ColonnaDataAppello.setCellValueFactory(new PropertyValueFactory<>("DataAppello"));
        ColonnaInserisciVoto.setCellFactory(param -> creaBottoneInserimentoVoto());
    }

    private List<StateItem> recuperaPrenotati() throws SQLException {
        controllerDocente.docente.setCommand(new CommandGetPrenotazioni(controllerDocente.connection, controllerDocente.docente.getCf()));
        List<Map<String, Object>> listaPrenotati = (List<Map<String, Object>>) controllerDocente.docente.eseguiAzione();

        ObservableList<ControllerDocenteInserisciVoti.StateItem> listStateItems = FXCollections.observableArrayList();

        for (Map<String, Object> prenotato : listaPrenotati) {
            String matricola      = (String) prenotato.get("matricola");
            String nomeEsame      = (String) prenotato.get("nome_esame");
            String voto           = (String) prenotato.get("voto");
            String numeroAppello  = (String) prenotato.get("numero_appello");
            String dataAppello    = (String) prenotato.get("data_appello");

            listStateItems.add(new ControllerDocenteInserisciVoti.StateItem(matricola, nomeEsame, voto, numeroAppello, dataAppello));
        }
        return listStateItems;
    }

    private void inserisciVoto(StateItem item) throws SQLException {
        String numeroAppello = item.getNumeroAppello();
        String matricola     = item.getMatricola();

        controllerDocente.docente.setCommand(new CommandInserisciVoto(controllerDocente.connection, item.getVoto(), matricola,  numeroAppello));
        controllerDocente.docente.eseguiAzione();
    }


    public void showInputAlert(StateItem selectedItem) {
        try {
            // Carica il file FXML del contenuto del dialogo
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/DocenteFXML/ModaleInserimentoVoto.fxml"));
            Parent content = loader.load();

            // Usa lo stesso controller attuale
            ControllerDocenteInserisciVoti currentController = loader.getController();
            currentController.setController(this.controllerDocente);

            currentController.setDati(selectedItem.getMatricola(), selectedItem.getNomeEsame());

            Alert alert = showAlertPage(content);

            // Mostra l'Alert e ottieni il risultato
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                //Recupera i dati dall'attuale controller
                String votoSelezionato = currentController.comboVoti.getValue();
                if(votoSelezionato != null){
                    selectedItem.voto = votoSelezionato;
                    inserisciVoto(selectedItem);
                }
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

    private void setDati(String matricola, String esame){
        labelMatricola.setText(matricola);
        labelEsame.setText(esame);
    }
}
