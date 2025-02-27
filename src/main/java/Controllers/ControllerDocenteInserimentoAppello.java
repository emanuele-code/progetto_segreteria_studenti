package Controllers;

import Commands.CommandChiudiPrenotazione;
import Commands.CommandGetAppelliPerDocente;
import Commands.CommandInserisciAppello;
import Interfacce.ControllerBase;
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
import java.util.Objects;
import java.util.Optional;

public class ControllerDocenteInserimentoAppello implements ControllerBase<ControllerDocente> {
    private ControllerDocente controllerDocente;

    @FXML private TableView   TabellaAppelli;
    @FXML private TableColumn ColonnaDataAppello;
    @FXML private TableColumn ColonnaEsame;
    @FXML private TableColumn ColonnaNumeroIscritti;
    @FXML private TableColumn ColonnaDisponibile;
    @FXML private TableColumn ColonnaDocente;
    @FXML private TableColumn ColonnaChiudiAppello;
    @FXML private TableColumn ColonnaNumeroAppello;

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
            String dataAppello        = (String) appello.get("data_appello");
            String nomeEsame          = (String) appello.get("nome_esame");
            String disponibile        = (String) appello.get("disponibile");
            String numeroIscritti     = (String) appello.get("numero_iscritti");
            String credenzialiDocente = (String)  appello.get("credenziali_docente");
            String numeroAppello      = (String)  appello.get("numero_appello");

            listStateItems.add(new ControllerDocenteInserimentoAppello.StateItem(dataAppello, nomeEsame, numeroIscritti, disponibile, credenzialiDocente, numeroAppello));
        }
        return listStateItems;

    }

    private void configuraColonne(){
        ColonnaDataAppello.setCellValueFactory(new PropertyValueFactory<>("DataAppello"));
        ColonnaNumeroAppello.setCellValueFactory(new PropertyValueFactory<>("numeroAppello"));
        ColonnaEsame.setCellValueFactory(new PropertyValueFactory<>("nomeEsame"));
        ColonnaNumeroIscritti.setCellValueFactory(new PropertyValueFactory<>("numeroIscritti"));
        ColonnaDisponibile.setCellValueFactory(new PropertyValueFactory<>("disponibile"));
        ColonnaDocente.setCellValueFactory(new PropertyValueFactory<>("credenzialiDocente"));
        ColonnaChiudiAppello.setCellFactory(param -> creaBottoneConferma());
    }

    public class StateItem {

        private String dataAppello;
        private String nomeEsame;
        private String numeroIscritti;
        private String disponibile;
        private String credenzialiDocente;
        private String numeroAppello;

        public StateItem(String dataAppello, String nomeEsame, String numeroIscritti, String disponibile, String credenzialiDocente, String numeroAppello) {
            this.dataAppello = dataAppello;
            this.nomeEsame = nomeEsame;
            this.numeroIscritti = numeroIscritti;
            this.disponibile = disponibile;
            this.credenzialiDocente = credenzialiDocente;
            this.numeroAppello = numeroAppello;
        }

        public String getDataAppello(){
            return dataAppello;
        }
        public String getNomeEsame(){
            return nomeEsame;
        }
        public String getNumeroIscritti(){
            return numeroIscritti;
        }
        public String getDisponibile(){
            return Objects.equals(disponibile, "1") ? "Aperto" : "Chiuso";
        }
        public String getCredenzialiDocente(){ return credenzialiDocente; }
        public String getNumeroAppello(){ return numeroAppello; }
    }

    public TableCell<ControllerDocenteInserimentoAppello.StateItem, Void> creaBottoneConferma(){
        return new TableCell<>() {
            private final Button btn = new Button("Chiudi");

            @Override
            protected void updateItem(java.lang.Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(btn);  // Mostriamo il bottone

                    btn.setOnAction(event -> {
                        ControllerDocenteInserimentoAppello.StateItem selectedItem = getTableRow().getItem();
                        chiudiAppello(selectedItem);
                    });
                }
            }
        };
    }

    @FXML
    private void chiudiAppello(StateItem item){
        String numeroAppello = item.getNumeroAppello();

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
