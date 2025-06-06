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
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import static Utils.UtilGestoreScena.caricaFXML;
/**
 * Controller per la gestione dell'inserimento e chiusura degli appelli da parte del docente.
 */
public class ControllerDocenteInserimentoAppello implements IControllerBase<ControllerDocente> {
    private ControllerDocente controllerDocente;

    @FXML public TableView<StateItem> TabellaAppelli;
    @FXML public DatePicker           DataAppelloButton;
    @FXML public ComboBox<String>     ScegliEsameLista;

    /**
     * Inizializza la tabella degli appelli con i dati correnti e configura le colonne.
     */
    @FXML
    public void initialize() {
        Platform.runLater(() -> {
            try {
                // Configura colonne e popola la tabella con gli appelli
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

    /**
     * Imposta il controller principale associato a questa vista.
     *
     * @param controllerDocente il controller principale del docente
     * @throws SQLException in caso di errori di connessione al database
     */
    @Override
    public void setController(ControllerDocente controllerDocente) throws SQLException {
        this.controllerDocente = controllerDocente;
    }

    /**
     * Mostra un alert per inserire un nuovo appello e gestisce la conferma dell'utente.
     */
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

    /**
     * Verifica se la data fornita è valida: deve essere almeno 7 giorni dopo e non in un weekend.
     *
     * @param dataStr la data in formato stringa
     * @return true se la data è valida, false altrimenti
     */
    private boolean isDataValida(String dataStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate dataSelezionata = LocalDate.parse(dataStr, formatter);
        LocalDate oggi = LocalDate.now();

        boolean almenoSetteGiorniDopo = dataSelezionata.isAfter(oggi.plusDays(6));
        DayOfWeek giorno = dataSelezionata.getDayOfWeek();
        boolean nonEWeekend = !(giorno == DayOfWeek.SATURDAY || giorno == DayOfWeek.SUNDAY);
        return almenoSetteGiorniDopo && nonEWeekend;
    }

    /**
     * Esegue l'operazione di inserimento appello dopo aver validato la data.
     *
     * @param currentController il controller corrente utilizzato per recuperare i dati
     * @throws SQLException in caso di errore nell'inserimento
     */
    private void eseguiOperazione(ControllerDocenteInserimentoAppello currentController) throws SQLException {
        String data = currentController.getDatiSelezionati();

        if (!isDataValida(data)) {
            UtilAlert.mostraErrore("La data per l'appello non può essere di sabato, domenica o meno di 7 giorni prima dalla data d'inserimento", "Errore inserimento appello", "Errore inserimento appello");
            return;
        }

        String esame = currentController.controllerDocente.convertiNomeToCodicePiano(currentController.ScegliEsameLista);

        controllerDocente.docente.setCommand(new CommandInserisciAppello(controllerDocente.connection, data, esame, ((IGetterDocente)controllerDocente.docente).getCf()));

        controllerDocente.docente.eseguiAzione();
    }

    /**
     * Restituisce la data selezionata nel DatePicker come stringa.
     *
     * @return la data selezionata, oppure stringa vuota se nulla
     */
    public String getDatiSelezionati() {
        return DataAppelloButton.getValue() != null ? DataAppelloButton.getValue().toString() : "";
    }

    /**
     * Recupera la lista di appelli associati al docente corrente.
     *
     * @return lista di oggetti StateItem rappresentanti gli appelli
     * @throws SQLException in caso di errori durante il recupero dati
     */
    private List<StateItem> recuperaAppelli() throws SQLException {
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
        System.out.println(listaAppelli);
        return listaStateItems;
    }

    /**
     * Chiude l'appello selezionato dopo conferma dell'utente.
     *
     * @param item l'appello da chiudere
     */
    @FXML
    private void chiudiAppello(StateItem item) {
        String numeroAppello = item.getValore("numero_appello");

        Alert alert = UtilAlert.mostraConferma("Quest azione è irreversibile", "Conferma chiusura appello", "Sei sicuro di voler chiudere le prenotazioni?");
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
