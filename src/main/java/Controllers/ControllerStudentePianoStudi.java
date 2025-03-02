package Controllers;

import Commands.CommandGetLibretto;
import Interfacce.IControllerBase;
import Models.StateItem;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class ControllerStudentePianoStudi implements IControllerBase<ControllerStudente> {
    private ControllerStudente controllerStudente;

    @FXML public TableView<StateItem> TableEsami;
    @FXML public TableColumn<StateItem, String> ColonnaCodice;
    @FXML public TableColumn<StateItem, String> ColonnaNomeEsame;
    @FXML public TableColumn<StateItem, String> ColonnaCFU;
    @FXML public TableColumn<StateItem, String> ColonnaVoto;
    @FXML public TableColumn<StateItem, String> ColonnaData;

    @FXML public Label LabelNome;
    @FXML public Label LabelCognome;
    @FXML public Label LabelMatricola;
    @FXML public Label LabelTasse;

    @FXML
    public void initialize() {
        Platform.runLater(() -> {
            LabelNome.setText(controllerStudente.studente.getNome());
            LabelCognome.setText(controllerStudente.studente.getCognome());
            LabelMatricola.setText(controllerStudente.studente.getMatricola());
            LabelTasse.setText(controllerStudente.studente.isTassePagate());

            configuraColonne();
            try {
                ObservableList<StateItem> listaStateItem = FXCollections.observableArrayList(recuperaLibretto());
                TableEsami.setItems(listaStateItem);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public void setController(ControllerStudente controller) throws SQLException {
        this.controllerStudente = controller;
    }

    private void configuraColonne() {
        ColonnaCodice.setCellValueFactory(cellData -> cellData.getValue().getCampo("codice_esame"));
        ColonnaNomeEsame.setCellValueFactory(cellData -> cellData.getValue().getCampo("nome_esame"));
        ColonnaCFU.setCellValueFactory(cellData -> cellData.getValue().getCampo("CFU"));
        ColonnaVoto.setCellValueFactory(cellData -> cellData.getValue().getCampo("voto"));
        ColonnaData.setCellValueFactory(cellData -> cellData.getValue().getCampo("data_appello"));
    }

    private List<StateItem> recuperaLibretto() throws SQLException {
        controllerStudente.studente.setCommand(new CommandGetLibretto(controllerStudente.connection, controllerStudente.studente.getMatricola()));
        List<Map<String, Object>> libretto = (List<Map<String, Object>>) controllerStudente.studente.eseguiAzione();

        ObservableList<StateItem> listStateItems = FXCollections.observableArrayList();

        for (Map<String, Object> esame : libretto) {
            StateItem item = new StateItem();
            item.setCampo("codice_esame", (String) esame.get("codice_esame"));
            item.setCampo("nome_esame", (String) esame.get("nome_esame"));
            item.setCampo("CFU", String.valueOf(esame.get("CFU"))); // Convertiamo in stringa per sicurezza
            item.setCampo("voto", String.valueOf(esame.get("voto")));
            item.setCampo("data_appello", (String) esame.get("data_appello"));

            listStateItems.add(item);
        }
        return listStateItems;
    }
}
