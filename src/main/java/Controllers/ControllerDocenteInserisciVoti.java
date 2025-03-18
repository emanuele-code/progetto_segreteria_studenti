package Controllers;

import Commands.CommandInserisciVoto;
import Commands.CommandGetPrenotazioni;
import Interfacce.IControllerBase;
import Interfacce.IGetterDocente;
import Models.StateItem;
import Utils.UtilGestoreTableView;
import Utils.UtilAlert;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.util.Pair;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import static Utils.UtilGestoreScena.caricaFXML;

public class ControllerDocenteInserisciVoti implements IControllerBase<ControllerDocente> {
    private ControllerDocente controllerDocente;

    @FXML public TableView<StateItem> TableInserisciVoti;
    @FXML public Label labelMatricola;
    @FXML public Label labelEsame;
    @FXML public ComboBox<String> comboVoti;


    @Override
    public void setController(ControllerDocente controllerDocente) throws SQLException {
        this.controllerDocente = controllerDocente;
    }


    @FXML
    public void initialize() {

        Platform.runLater(() -> {
            try {

                Map<String, Function<StateItem, ?>> colonneMappa = Map.of(
                        "matricola", item -> item.getCampo("matricola").get(),
                        "esame", item -> item.getCampo("nome_esame").get(),
                        "voto", item -> item.getCampo("voto").get(),
                        "numero_appello", item -> item.getCampo("numero_appello").get(),
                        "data_appello", item -> item.getCampo("data_appello").get()
                );
                UtilGestoreTableView.configuraColonne(TableInserisciVoti, colonneMappa);

                UtilGestoreTableView.aggiungiColonnaBottone(TableInserisciVoti, "Inserisci Voto",
                        item -> true,
                        item -> "Inserisci",
                        item -> () -> mostraInputAlert(item)
                );

                ObservableList<StateItem> listaStateItems = FXCollections.observableArrayList(recuperaPrenotati());
                TableInserisciVoti.setItems(listaStateItems);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }


    private void valorizzaComboVoti(){
        ObservableList<String> voti = FXCollections.observableArrayList(
                "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "30L", "Assente", "Non Ammesso"
        );
        comboVoti.setItems(voti);
    }


    private List<StateItem> recuperaPrenotati() throws SQLException {
        controllerDocente.docente.setCommand(new CommandGetPrenotazioni(controllerDocente.connection, ((IGetterDocente)controllerDocente.docente).getCf()));
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


    private void setDati(String matricola, String esame) {
        labelMatricola.setText(matricola);
        labelEsame.setText(esame);
    }


    private void inserisciVoto(StateItem item) throws SQLException {
        String numeroAppello = (String) item.getCampo("numero_appello").get();
        String matricola = (String) item.getCampo("matricola").get();
        String voto = (String) item.getCampo("voto").get();

        controllerDocente.docente.setCommand(new CommandInserisciVoto(controllerDocente.connection, voto, matricola, numeroAppello));
        controllerDocente.docente.eseguiAzione();
    }


    public void mostraInputAlert(StateItem item) {
        String matricola = (String) item.getCampo("matricola").get();
        String nomeEsame = (String) item.getCampo("nome_esame").get();

        try {
            Pair<Parent, ControllerDocenteInserisciVoti> risultatoFXML = caricaFXML("/DocenteFXML/ModaleInserimentoVoto.fxml");
            ControllerDocenteInserisciVoti currentController = risultatoFXML.getValue();
            currentController.setController(this.controllerDocente);

            // Esegui l'inizializzazione e valorizza comboVoti solo dopo l'inizializzazione del controller
            Platform.runLater(() -> {
                currentController.setDati(matricola, nomeEsame);
                currentController.valorizzaComboVoti();
            });

            Alert alert = UtilAlert.mostraPagina("Inserisci Voto", "Compila i dettagli della votazione", risultatoFXML.getKey());
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

}