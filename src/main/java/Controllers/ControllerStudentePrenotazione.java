package Controllers;

import Commands.CommandGetAppelliPerStudente;
import Commands.CommandPrenotaEsame;
import Interfacce.IControllerBase;
import Models.StateItem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class ControllerStudentePrenotazione implements IControllerBase<ControllerStudente> {
    private ControllerStudente controllerStudente;

    @FXML public TableView<StateItem>           tablePrenotazione;
    @FXML public TableColumn<StateItem, String> colonnaDocente;
    @FXML public TableColumn<StateItem, String> colonnaData;
    @FXML public TableColumn<StateItem, String> colonnaNomeEsame;
    @FXML public TableColumn<StateItem, String> colonnaNumeroAppello;
    @FXML public TableColumn<StateItem, String> colonnaDisponibile;
    @FXML public TableColumn<StateItem, Void>   colonnaPrenotazione;  // Per il bottone

    @Override
    public void setController(ControllerStudente controllerStudente) throws SQLException {
        this.controllerStudente = controllerStudente;
        prenotaAppello();
    }

    private void configuraColonne() {
        colonnaDocente.setCellValueFactory(cellData -> cellData.getValue().getCampo("credenzialiDocente"));
        colonnaData.setCellValueFactory(cellData -> cellData.getValue().getCampo("dataAppello"));
        colonnaNomeEsame.setCellValueFactory(cellData -> cellData.getValue().getCampo("nomeEsame"));
        colonnaNumeroAppello.setCellValueFactory(cellData -> cellData.getValue().getCampo("numeroAppello"));
        colonnaDisponibile.setCellValueFactory(cellData -> cellData.getValue().getCampo("disponibile"));
    }

    private List<StateItem> recuperaAppelli() throws SQLException {
        controllerStudente.studente.setCommand(new CommandGetAppelliPerStudente(controllerStudente.connection, controllerStudente.studente.getNomePiano()));
        List<Map<String, Object>> listaAppelli = (List<Map<String, Object>>) controllerStudente.studente.eseguiAzione();

        ObservableList<StateItem> listaStateItems = FXCollections.observableArrayList();

        for (Map<String, Object> appello : listaAppelli) {
            StateItem item = new StateItem();
            item.setCampo("matricola", controllerStudente.studente.getMatricola());
            item.setCampo("credenzialiDocente", (String) appello.get("credenziali_docente"));
            item.setCampo("dataAppello", (String) appello.get("data_appello"));
            item.setCampo("nomeEsame", (String) appello.get("nome_esame"));
            item.setCampo("numeroAppello", (String) appello.get("numero_appello"));
            item.setCampo("disponibile", ((Boolean) appello.get("disponibile")) ? "Aperto" : "Chiuso");

            listaStateItems.add(item);
        }

        return listaStateItems;
    }

    private void eseguiPrenotazione(StateItem item) {
        String matricola = item.getValore("matricola");
        String numeroAppello = item.getValore("numeroAppello");

        Alert alert = ControllerAlert.mostraConferma("Quest'azione è irreversibile", "Conferma Prenotazione", "Sei sicuro di voler confermare la prenotazione?");
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                controllerStudente.studente.setCommand(new CommandPrenotaEsame(controllerStudente.connection, numeroAppello, matricola));
                try {
                    controllerStudente.studente.eseguiAzione();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    public TableCell<StateItem, Void> creaBottonePrenotazione() {
        return new TableCell<>() {
            private final Button btn = new Button("Prenota");

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    StateItem rowData = getTableRow().getItem();
                    if (rowData != null && "Chiuso".equals(rowData.getValore("disponibile"))) {
                        setGraphic(null);
                    } else {
                        setGraphic(btn);
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
