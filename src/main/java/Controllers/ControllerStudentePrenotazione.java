package Controllers;

import Commands.CommandGetAppelliPerStudente;
import Commands.CommandPrenotaEsame;
import Interfacce.ControllerBase;
import Interfacce.ControllerBaseStudente;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class ControllerStudentePrenotazione implements ControllerBase<ControllerStudente> {
    private ControllerStudente controllerStudente;

    @FXML private TableView   tablePrenotazione;
    @FXML private TableColumn colonnaDocente;
    @FXML private TableColumn colonnaData;
    @FXML private TableColumn colonnaNomeEsame;
    @FXML private TableColumn colonnaPrenotazione;
    @FXML private TableColumn colonnaNumeroAppello;
    @FXML private TableColumn colonnaDisponibile;

    @Override
    public void setController(ControllerStudente controllerStudente) throws SQLException {
        this.controllerStudente = controllerStudente;
        prenotaAppello();
    }


    private void configuraColonne() {
        colonnaDocente.setCellValueFactory(new PropertyValueFactory<>("credenzialiDocente"));
        colonnaData.setCellValueFactory(new PropertyValueFactory<>("dataAppello"));
        colonnaNomeEsame.setCellValueFactory(new PropertyValueFactory<>("nomeEsame"));
        colonnaNumeroAppello.setCellValueFactory(new PropertyValueFactory<>("numeroAppello"));
        colonnaDisponibile.setCellValueFactory(new PropertyValueFactory<>("disponibile"));
    }


    private List<StateItem> recuperaAppelli() throws SQLException {
        controllerStudente.studente.setCommand(new CommandGetAppelliPerStudente(controllerStudente.connection, controllerStudente.studente.getNomePiano()));
        List<Map<String, Object>> listaAppelli = (List<Map<String, Object>>) controllerStudente.studente.eseguiAzione();

        ObservableList<StateItem> listaStateItems = FXCollections.observableArrayList();

        for (Map<String, Object> appello : listaAppelli) {
            String credenzialiDocente = (String) appello.get("credenziali_docente");
            String dataAppello        = (String) appello.get("data_appello");
            String nomeEsame          = (String) appello.get("nome_esame");
            String numeroAppello      = (String) appello.get("numero_appello");
            String disponibile        = (Boolean) appello.get("disponibile") ? "Aperto" : "Chiuso";

            listaStateItems.add(new StateItem(controllerStudente.studente.getMatricola(), credenzialiDocente, dataAppello, nomeEsame, numeroAppello, disponibile));

        }

        return listaStateItems;
    }


    public class StateItem {

        private String credenzialiDocente;
        private String dataAppello;
        private String nomeEsame;
        private String numeroAppello;
        private String matricola;
        private String disponibile;

        public StateItem(String matricola, String credenzialiDocente, String dataAppello, String nomeEsame, String numeroAppello, String disponibile) {
            this.credenzialiDocente = credenzialiDocente;
            this.dataAppello        = dataAppello;
            this.nomeEsame          = nomeEsame;
            this.numeroAppello      = numeroAppello;
            this.matricola          = matricola;
            this.disponibile        = disponibile;
        }

        public String getCredenzialiDocente(){
            return credenzialiDocente;
        }
        public String getDataAppello(){
            return dataAppello;
        }
        public String getNomeEsame(){
            return nomeEsame;
        }
        public String getNumeroAppello(){
            return numeroAppello;
        }
        public String getMatricola() { return matricola; }
        public String getDisponibile() { return disponibile; }

    }


    private void eseguiPrenotazione(StateItem selectedItem){
        String matricola     = selectedItem.getMatricola();
        String numeroAppello = selectedItem.getNumeroAppello();

        Alert alert = controllerStudente.mostraConferma("Quest azione è irreversibile");
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                controllerStudente.studente.setCommand(new CommandPrenotaEsame(controllerStudente.connection, numeroAppello ,matricola));
                try {
                    controllerStudente.studente.eseguiAzione();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }


    public TableCell<StateItem, Void> creaBottonePrenotazione(){

        return new TableCell<>() {
            private final Button btn = new Button("Prenota");

            @Override
            protected void updateItem(java.lang.Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    StateItem rowData = getTableRow().getItem(); // Recupera l'elemento della riga
                    if (rowData != null && "Chiuso".equals(rowData.getDisponibile())) {
                        setGraphic(null);  // Nascondi il bottone se l'appello è chiuso
                    } else {
                        setGraphic(btn);  // Mostra il bottone se l'appello è aperto

                        btn.setOnAction(event -> {
                            eseguiPrenotazione(rowData);
                            try {
                                prenotaAppello();
                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }
                        });
                    }
                }
            }
        };
    }


    public void prenotaAppello() throws SQLException {
        configuraColonne();
        ObservableList<StateItem> listaStateItem = FXCollections.observableArrayList(recuperaAppelli());
        colonnaPrenotazione.setCellFactory(param -> creaBottonePrenotazione());
        tablePrenotazione.setItems(listaStateItem);
    }
}
