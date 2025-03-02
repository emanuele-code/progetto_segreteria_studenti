package Controllers;

import Commands.CommandConfermaVoto;
import Commands.CommandGetVotiDaConfermare;
import Interfacce.IControllerBase;
import Models.StateItem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class ControllerSegreteriaConfermaVoti implements IControllerBase<ControllerSegreteria> {

    private ControllerSegreteria controllerSegreteria;

    @FXML public TableView<StateItem>           tableConferme;
    @FXML public TableColumn<StateItem, String> ColonnaConfermaMatricola;
    @FXML public TableColumn<StateItem, String> ColonnaConfermaDocente;
    @FXML public TableColumn<StateItem, String> ColonnaConfermaDataAppello;
    @FXML public TableColumn<StateItem, String> ColonnaConfermaNomeEsame;
    @FXML public TableColumn<StateItem, String> ColonnaConfermaCFU;
    @FXML public TableColumn<StateItem, String>   ColonnaConfermaVoto;
    @FXML public TableColumn<StateItem, String> ColonnaConfermaCodiceAppello;
    @FXML public TableColumn<StateItem, Void> ColonnaConferma;




    private List<StateItem> getVotiDaConfermare() throws SQLException {
        controllerSegreteria.segreteria.setCommand(new CommandGetVotiDaConfermare(controllerSegreteria.connection));
        List<Map<String, Object>> listaVoti = (List<Map<String, Object>>) controllerSegreteria.segreteria.eseguiAzione();

        ObservableList<StateItem> listaStateItems = FXCollections.observableArrayList();

        for (Map<String, Object> voti : listaVoti) {
            StateItem item = new StateItem();
            item.setCampo("matricola",      (String) voti.get("matricola"));
            item.setCampo("numero_appello", (String) voti.get("numero_appello"));
            item.setCampo("voto",           (String) voti.get("voto"));
            item.setCampo("cf_docente",     (String) voti.get("cf_docente"));
            item.setCampo("data_appello",   (String) voti.get("data_appello"));
            item.setCampo("nome_esame",     (String) voti.get("nome_esame"));
            item.setCampo("cfu",            (String) voti.get("cfu"));

            listaStateItems.add(item);
        }

        return listaStateItems;
    }

    private void configuraColonne(){
        ColonnaConfermaMatricola.setCellValueFactory(cellData -> cellData.getValue().getCampo("matricola"));
        ColonnaConfermaCodiceAppello.setCellValueFactory(cellData -> cellData.getValue().getCampo("numero_appello"));
        ColonnaConfermaVoto.setCellValueFactory(cellData -> cellData.getValue().getCampo("voto"));
        ColonnaConfermaDocente.setCellValueFactory(cellData -> cellData.getValue().getCampo("docente"));
        ColonnaConfermaDataAppello.setCellValueFactory(cellData -> cellData.getValue().getCampo("data_appello"));
        ColonnaConfermaNomeEsame.setCellValueFactory(cellData -> cellData.getValue().getCampo("nome_esame"));
        ColonnaConfermaCFU.setCellValueFactory(cellData -> cellData.getValue().getCampo("cfu"));

    }

    private void eseguiConfermaVoto(StateItem item){
        String matricola     = (String) item.getCampo("matricola").get();
        String numeroAppello = (String) item.getCampo("numero_appello").get();

        Alert alert = ControllerAlert.mostraConferma("Quest azione è irreversibile", "Conferma voto", "Sei sicuro di voler inserire il voto?");
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                controllerSegreteria.segreteria.setCommand(new CommandConfermaVoto(controllerSegreteria.connection, matricola, numeroAppello));
                try {
                    controllerSegreteria.segreteria.eseguiAzione();

                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }


    public void setController(ControllerSegreteria controllerSegreteria) throws SQLException {
        this.controllerSegreteria = controllerSegreteria;
        confermaVoti();
    }

    public TableCell<StateItem, Void> creaBottoneConferma(){
        return new TableCell<>() {
            private final Button btn = new Button("Conferma");

            @Override
            protected void updateItem(java.lang.Void slectedItem, boolean empty) {
                super.updateItem(slectedItem, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(btn);  // Mostriamo il bottone

                    btn.setOnAction(event -> {
                        StateItem item = getTableRow().getItem();
                        eseguiConfermaVoto(item);
                        try {
                            confermaVoti();
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    });
                }
            }
        };
    }

    public void confermaVoti() throws SQLException {

        configuraColonne();
        ObservableList<StateItem> listaStateItems = FXCollections.observableArrayList(getVotiDaConfermare());
        ColonnaConferma.setCellFactory(param -> creaBottoneConferma());
        tableConferme.setItems(listaStateItems);
    }



}
