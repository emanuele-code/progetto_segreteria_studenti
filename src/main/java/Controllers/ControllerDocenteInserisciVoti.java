package Controllers;

import Commands.CommandInserisciVoto;
import Commands.CommandGetPrenotazioni;
import Interfacce.IControllerBase;
import Models.StateItem;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public class ControllerDocenteInserisciVoti implements IControllerBase<ControllerDocente> {
    private ControllerDocente controllerDocente;

    @FXML private TableView<StateItem> TableInserisciVoti;
    @FXML private Label labelMatricola;
    @FXML private Label labelEsame;
    @FXML private ComboBox<String> comboVoti;

    @FXML
    public void initialize() {
        Map<String, Function<StateItem, ?>> colonneMappa = Map.of(
                "matricola", item -> item.getCampo("matricola").get(),
                "esame", item -> item.getCampo("nome_esame").get(),
                "voto", item -> item.getCampo("voto").get(),
                "numero_appello", item -> item.getCampo("numero_appello").get(),
                "data_appello", item -> item.getCampo("data_appello").get()
        );
        ControllerGestoreTableView.configuraColonne(TableInserisciVoti, colonneMappa);

        ControllerGestoreTableView.aggiungiColonnaBottone(TableInserisciVoti, "Inserisci Voto",
                item -> "Inserisci",
                item -> () -> showInputAlert(item)
        );

        Platform.runLater(() -> {
            try {
                List<StateItem> listaStateItems = recuperaPrenotati();
                ControllerGestoreTableView.aggiornaDati(TableInserisciVoti, listaStateItems);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            ObservableList<String> voti = FXCollections.observableArrayList(
                    "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "30L", "Assente", "Non Ammesso"
            );
            comboVoti.setItems(voti);
        });
    }

    @Override
    public void setController(ControllerDocente controllerDocente) throws SQLException {
        this.controllerDocente = controllerDocente;
    }

    private List<StateItem> recuperaPrenotati() throws SQLException {
        controllerDocente.docente.setCommand(new CommandGetPrenotazioni(controllerDocente.connection, controllerDocente.docente.getCf()));
        List<Map<String, Object>> listaPrenotati = (List<Map<String, Object>>) controllerDocente.docente.eseguiAzione();

        ObservableList<StateItem> listStateItems = FXCollections.observableArrayList();
        for (Map<String, Object> prenotato : listaPrenotati) {
            StateItem item = new StateItem();
            item.setCampo("matricola", prenotato.get("matricola"));
            item.setCampo("nome_esame", prenotato.get("nome_esame"));
            item.setCampo("voto", prenotato.get("voto"));
            item.setCampo("numero_appello", prenotato.get("numero_appello"));
            item.setCampo("data_appello", prenotato.get("data_appello"));
            listStateItems.add(item);
        }
        return listStateItems;
    }

    private void inserisciVoto(StateItem item) throws SQLException {
        String numeroAppello = (String) item.getCampo("numero_appello").get();
        String matricola = (String) item.getCampo("matricola").get();
        String voto = (String) item.getCampo("voto").get();

        controllerDocente.docente.setCommand(new CommandInserisciVoto(controllerDocente.connection, voto, matricola, numeroAppello));
        controllerDocente.docente.eseguiAzione();
    }

    public void showInputAlert(StateItem item) {
        String matricola = (String) item.getCampo("matricola").get();
        String nomeEsame = (String) item.getCampo("nome_esame").get();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/DocenteFXML/ModaleInserimentoVoto.fxml"));
            Parent content = loader.load();

            ControllerDocenteInserisciVoti currentController = loader.getController();
            currentController.setController(this.controllerDocente);
            currentController.setDati(matricola, nomeEsame);

            Alert alert = showAlertPage(content);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                String votoSelezionato = currentController.comboVoti.getValue();
                if (votoSelezionato != null) {
                    item.setCampo("voto", votoSelezionato);
                    inserisciVoto(item);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Alert showAlertPage(Parent content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Inserisci Voto");
        alert.setHeaderText("Compila i dettagli della votazione");
        alert.getDialogPane().setContent(content);
        return alert;
    }

    private void setDati(String matricola, String esame) {
        labelMatricola.setText(matricola);
        labelEsame.setText(esame);
    }
}