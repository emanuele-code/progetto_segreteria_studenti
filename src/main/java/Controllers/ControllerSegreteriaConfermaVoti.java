package Controllers;

import Commands.CommandConfermaVoto;
import Commands.CommandGetVotiDaConfermare;
import Interfacce.IControllerBase;
import Models.StateItem;
import Utils.UtilGestoreTableView;
import Utils.UtilAlert;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Controller responsabile per la gestione della conferma voti da parte della segreteria.
 * Questa classe si occupa di visualizzare i voti da confermare e consente l'approvazione finale.
 */
public class ControllerSegreteriaConfermaVoti implements IControllerBase<ControllerSegreteria> {

    private ControllerSegreteria controllerSegreteria;

    @FXML public TableView<StateItem> tableConferme;

    /**
     * Inizializza la tabella dei voti da confermare con le relative colonne e pulsanti di conferma.
     * Viene eseguito automaticamente dal framework JavaFX al caricamento della FXML.
     */
    @FXML
    public void initialize() {
        Platform.runLater(() -> {
            try {
                // Configura colonne della TableView
                Map<String, Function<StateItem, ?>> colonneMappa = Map.of(
                        "Matricola", item -> item.getCampo("matricola").get(),
                        "Appello", item -> item.getCampo("numero_appello").get(),
                        "Voto", item -> item.getCampo("voto").get(),
                        "Docente", item -> item.getCampo("credenziali_docente").get(),
                        "Data Appello", item -> item.getCampo("data_appello").get(),
                        "Esame", item -> item.getCampo("nome_esame").get(),
                        "CFU", item -> item.getCampo("cfu").get()
                );

                UtilGestoreTableView.configuraColonne(tableConferme, colonneMappa);

                // Aggiunge pulsante "Conferma" per ogni riga
                UtilGestoreTableView.aggiungiColonnaBottone(tableConferme, "Conferma",
                        item -> true,
                        item -> "Conferma",
                        item -> () -> eseguiConfermaVoto(item)
                );

                // Popola la tabella con i dati da confermare
                ObservableList<StateItem> listaStateItems = FXCollections.observableArrayList(getVotiDaConfermare());
                tableConferme.setItems(listaStateItems);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Imposta il controller principale della segreteria, usato per accedere a connessione e comandi.
     *
     * @param controllerSegreteria controller della vista principale
     * @throws SQLException in caso di errori durante l'inizializzazione
     */
    @Override
    public void setController(ControllerSegreteria controllerSegreteria) throws SQLException {
        this.controllerSegreteria = controllerSegreteria;
    }

    /**
     * Recupera l'elenco dei voti da confermare dal database.
     *
     * @return lista di {@link StateItem} rappresentanti i voti in sospeso
     * @throws SQLException se il comando di recupero fallisce
     */
    private List<StateItem> getVotiDaConfermare() throws SQLException {
        controllerSegreteria.segreteria.setCommand(new CommandGetVotiDaConfermare(controllerSegreteria.connessione));
        List<Map<String, Object>> listaVoti = (List<Map<String, Object>>) controllerSegreteria.segreteria.eseguiAzione();

        ObservableList<StateItem> listaStateItems = FXCollections.observableArrayList();

        for (Map<String, Object> voti : listaVoti) {
            StateItem item = new StateItem();
            item.setCampo("matricola",           (String) voti.get("matricola"));
            item.setCampo("numero_appello",      (String) voti.get("numero_appello"));
            item.setCampo("voto",                (String) voti.get("voto"));
            item.setCampo("credenziali_docente", (String) voti.get("credenziali_docente"));
            item.setCampo("data_appello",        (String) voti.get("data_appello"));
            item.setCampo("nome_esame",          (String) voti.get("nome_esame"));
            item.setCampo("cfu",                 (String) voti.get("cfu"));

            listaStateItems.add(item);
        }

        return listaStateItems;
    }

    /**
     * Esegue la conferma del voto per lo studente selezionato.
     * Mostra un popup di conferma prima dell'invio.
     *
     * @param item riga della tabella selezionata contenente i dati del voto
     */
    private void eseguiConfermaVoto(StateItem item) {
        String matricola     = (String) item.getCampo("matricola").get();
        String numeroAppello = (String) item.getCampo("numero_appello").get();

        Alert alert = UtilAlert.mostraConferma(
                "Quest'azione è irreversibile",
                "Conferma voto",
                "Sei sicuro di voler inserire il voto?"
        );

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                controllerSegreteria.segreteria.setCommand(
                        new CommandConfermaVoto(controllerSegreteria.connessione, matricola, numeroAppello)
                );
                try {
                    controllerSegreteria.segreteria.eseguiAzione();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}
