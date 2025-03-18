package Controllers;

import Commands.CommandChiudiPrenotazione;
import Commands.CommandGetAppelliPerDocente;
import Commands.CommandInserisciAppello;
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
import javafx.scene.control.TableView;
import javafx.scene.control.*;
import javafx.util.Pair;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import static Utils.UtilGestoreScena.caricaFXML;

public class ControllerDocenteInserimentoAppello implements IControllerBase<ControllerDocente> {
    private ControllerDocente controllerDocente;

    @FXML public TableView<StateItem> TabellaAppelli;
    @FXML public DatePicker           DataAppelloButton;
    @FXML public ComboBox<String>     ScegliEsameLista;


    @FXML
    public void initialize() {
        Platform.runLater(() -> {
            try {
                Map<String, Function<StateItem, ?>> colonneMappa = Map.of(
                        "data_appello", item -> item.getCampo("data_appello").get(),
                        "numero_appello", item -> item.getCampo("numero_appello").get(),
                        "nome_esame", item -> item.getCampo("nome_esame").get(),
                        "numero_iscritti", item -> item.getCampo("numero_iscritti").get(),
                        "disponibile", item -> item.getCampo("disponibile").get(),
                        "docente", item -> item.getCampo("credenziali_docente").get()
                );
                UtilGestoreTableView.configuraColonne(TabellaAppelli, colonneMappa);

                UtilGestoreTableView.aggiungiColonnaBottone(TabellaAppelli, "Chiudi appello",
                        item -> true,
                        item -> "Chiudi",
                        item -> () -> chiudiAppello(item)
                );

                ObservableList<StateItem> listaStateItems = FXCollections.observableArrayList(recuperaAppelli());
                TabellaAppelli.setItems(listaStateItems);

            } catch(SQLException e){
                throw new RuntimeException(e);
            }
        });
    }


    @Override
    public void setController(ControllerDocente controllerDocente) throws SQLException {
        this.controllerDocente = controllerDocente;
    }


    @FXML
    public void mostraInputAlert() {
        try {
            Pair<Parent, ControllerDocenteInserimentoAppello> risultatoFXML = caricaFXML("/DocenteFXML/DocenteConfermaInserimentoAppello.fxml");
            ControllerDocenteInserimentoAppello currentController = risultatoFXML.getValue();
            currentController.setController(this.controllerDocente);

            currentController.controllerDocente.caricaEsami(currentController.ScegliEsameLista);
            Alert alert = UtilAlert.mostraPagina("Inserisci Appello", "Compila i dettagli dell'appello", risultatoFXML.getKey());

            Optional<ButtonType> risultato = alert.showAndWait();
            if (risultato.isPresent() && risultato.get() == ButtonType.OK) {
                eseguiOperazione(currentController);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void eseguiOperazione(ControllerDocenteInserimentoAppello currentController) throws SQLException {
        String data = currentController.getDatiSelezionati();
        String esame = currentController.controllerDocente.convertiNomeToCodicePiano(currentController.ScegliEsameLista);

        controllerDocente.docente.setCommand(new CommandInserisciAppello(controllerDocente.connection, data, esame, ((IGetterDocente)controllerDocente.docente).getCf()));
        controllerDocente.docente.eseguiAzione();
    }


    public String getDatiSelezionati() {
        return DataAppelloButton.getValue() != null ? DataAppelloButton.getValue().toString() : "";
    }


    private List<StateItem> recuperaAppelli() throws SQLException{
        controllerDocente.docente.setCommand(new CommandGetAppelliPerDocente(controllerDocente.connection, ((IGetterDocente)controllerDocente.docente).getCf()));
        List<Map<String, Object>> listaAppelli = (List<Map<String, Object>>) controllerDocente.docente.eseguiAzione();

        ObservableList<StateItem> listaStateItems = FXCollections.observableArrayList();

        for (Map<String, Object> appello : listaAppelli) {
            StateItem item = new StateItem();

            item.setCampo("data_appello"        , (String) appello.get("data_appello"));
            item.setCampo("nome_esame"          , (String) appello.get("nome_esame"));
            item.setCampo("disponibile"         , (String) appello.get("disponibile"));
            item.setCampo("numero_iscritti"     , (String) appello.get("numero_iscritti"));
            item.setCampo("credenziali_docente" , (String)  appello.get("credenziali_docente"));
            item.setCampo("numero_appello"      , (String)  appello.get("numero_appello"));

            listaStateItems.add(item);
        }
        return listaStateItems;

    }


    @FXML
    private void chiudiAppello(StateItem item){
        String numeroAppello = item.getValore("numero_appello");

        Alert alert = UtilAlert.mostraConferma("Quest azione è irreversibile", "Conferma Inserimento", "Sei sicuro di voler confermare l'inserimento?");
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