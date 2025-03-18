package Controllers;

import Commands.CommandEliminaPrenotazione;
import Commands.CommandGetAppelliPerStudente;
import Commands.CommandPrenotaEsame;
import Interfacce.IControllerBase;
import Interfacce.IGetterStudente;
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

public class ControllerStudentePrenotazione implements IControllerBase<ControllerStudente> {
    private ControllerStudente controllerStudente;

    @FXML public TableView<StateItem>           tablePrenotazione;


    @Override
    public void setController(ControllerStudente controllerStudente) throws SQLException {
        this.controllerStudente = controllerStudente;
    }


    @FXML
    public void initialize() {
        Platform.runLater(() -> {
            try {
                Map<String, Function<StateItem, ?>> colonneMappa = Map.of(
                        "Docente", item -> item.getCampo("credenzialiDocente").get(),
                        "Data Appello", item -> item.getCampo("dataAppello").get(),
                        "Esame", item -> item.getCampo("nomeEsame").get(),
                        "Disponibilità", item -> item.getCampo("disponibile").get(),
                        "Prenotato", item -> item.getCampo("prenota").get()
                );
                UtilGestoreTableView.configuraColonne(tablePrenotazione, colonneMappa);

                UtilGestoreTableView.aggiungiColonnaBottone(tablePrenotazione,
                        "Stato",
                        item -> item.getCampo("disponibile").get().equals("Aperto"),
                        item -> item.getCampo("prenota").get().equals("Prenotato") ? "Annulla" : "Prenota",
                        item -> item.getCampo("prenota").get().equals("Prenotato")
                                ? () -> eliminaPrenotazione(item)
                                : () -> eseguiPrenotazione(item)
                );

                ObservableList<StateItem> listaStateItem = FXCollections.observableArrayList(recuperaAppelli());
                tablePrenotazione.setItems(listaStateItem);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }


    private List<StateItem> recuperaAppelli() throws SQLException {
        String nomePiano = ((IGetterStudente) controllerStudente.studente).getNomePiano();
        String matricola = ((IGetterStudente) controllerStudente.studente).getMatricola();

        controllerStudente.studente.setCommand(new CommandGetAppelliPerStudente(controllerStudente.connection, nomePiano, matricola));
        List<Map<String, Object>> listaAppelli = (List<Map<String, Object>>) controllerStudente.studente.eseguiAzione();

        ObservableList<StateItem> listaStateItems = FXCollections.observableArrayList();

        for (Map<String, Object> appello : listaAppelli) {
            StateItem item = new StateItem();
            item.setCampo("matricola", matricola);
            item.setCampo("credenzialiDocente", appello.get("credenziali_docente"));
            item.setCampo("dataAppello", appello.get("data_appello"));
            item.setCampo("nomeEsame", appello.get("nome_esame"));
            item.setCampo("numeroAppello", appello.get("numero_appello"));
            item.setCampo("disponibile", ((Boolean) appello.get("disponibile")) ? "Aperto" : "Chiuso");

            // Controllo se lo studente è prenotato
            boolean isPrenotato = (Boolean) appello.get("prenotato");

            // Imposto la colonna "prenota"
            item.setCampo("prenota", isPrenotato ? "Prenotato" : "Prenota");

            listaStateItems.add(item);
        }

        return listaStateItems;
    }


    private void eseguiPrenotazione(StateItem item) {
        String matricola = item.getValore("matricola");
        String numeroAppello = item.getValore("numeroAppello");

        Alert alert = UtilAlert.mostraConferma("Quest'azione è irreversibile", "Conferma Prenotazione", "Sei sicuro di voler confermare la prenotazione?");
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                controllerStudente.studente.setCommand(new CommandPrenotaEsame(controllerStudente.connection, numeroAppello, matricola));
                try {
                    controllerStudente.studente.eseguiAzione();
                    ObservableList<StateItem> listaStateItem = FXCollections.observableArrayList(recuperaAppelli());
                    tablePrenotazione.setItems(listaStateItem);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }


    private void eliminaPrenotazione(StateItem item) {
        String matricola = item.getValore("matricola");
        String numeroAppello = item.getValore("numeroAppello");


        Alert alert = UtilAlert.mostraConferma("Quest'azione è irreversibile", "Elimina Prenotazione", "Sei sicuro di voler eliminare la prenotazione?");
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                controllerStudente.studente.setCommand(new CommandEliminaPrenotazione(controllerStudente.connection, matricola, numeroAppello));
                try {
                    controllerStudente.studente.eseguiAzione();
                    ObservableList<StateItem> listaStateItem = FXCollections.observableArrayList(recuperaAppelli());
                    tablePrenotazione.setItems(listaStateItem);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

}
