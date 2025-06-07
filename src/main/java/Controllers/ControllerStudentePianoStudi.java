package Controllers;

import Commands.CommandGetLibretto;
import Interfacce.IControllerBase;
import Interfacce.IGetterStudente;
import Models.StateItem;
import Utils.UtilGestoreTableView;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Controller della schermata del piano di studi per lo studente.
 * Visualizza informazioni personali, stato tasse, media ponderata e il libretto esami.
 */
public class ControllerStudentePianoStudi implements IControllerBase<ControllerStudente> {

    private ControllerStudente controllerStudente;
    @FXML public TableView<StateItem> TableEsami;
    @FXML public Label LabelNome;
    @FXML public Label LabelCognome;
    @FXML public Label LabelMatricola;
    @FXML public Label LabelTasse;
    @FXML public Label LabelMedia;

    /**
     * Imposta il controller principale dello studente.
     *
     * @param controller il controller da iniettare.
     * @throws SQLException in caso di errori DB.
     */
    @Override
    public void setController(ControllerStudente controller) throws SQLException {
        this.controllerStudente = controller;
    }

    /**
     * Inizializza la vista popolando i dati anagrafici, la media e la tabella del libretto.
     */
    @FXML
    public void initialize() {
        Platform.runLater(() -> {
            try {
                LabelNome.setText(((IGetterStudente) controllerStudente.studente).getNome());
                LabelCognome.setText(((IGetterStudente) controllerStudente.studente).getCognome());
                LabelMatricola.setText(((IGetterStudente) controllerStudente.studente).getMatricola());
                LabelTasse.setText(((IGetterStudente) controllerStudente.studente).isTassePagate());
                LabelMedia.setText(calcolaMedia());

                Map<String, Function<StateItem, ?>> colonneMappa = Map.of(
                        "Codice", item -> item.getCampo("codice_esame").get(),
                        "Nome Esame", item -> item.getCampo("nome_esame").get(),
                        "CFU", item -> item.getCampo("CFU").get(),
                        "Voto", item -> item.getCampo("voto").get(),
                        "Data", item -> item.getCampo("data_appello").get()
                );

                UtilGestoreTableView.configuraColonne(TableEsami, colonneMappa);

                ObservableList<StateItem> listaStateItem = FXCollections.observableArrayList(recuperaLibretto());
                TableEsami.setItems(listaStateItem);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Calcola la media ponderata degli esami superati.
     * Ignora esami non sostenuti. Tratta "30L" come 32.
     *
     * @return la media formattata con 2 decimali, "30.00" se eccede, "N/A" se nessun voto, o "Errore" se fallisce.
     */
    private String calcolaMedia() {
        try {
            float risultato = 0;
            int CFUTotali = 0;
            List<StateItem> libretto = recuperaLibretto();

            for (StateItem item : libretto) {
                String voto = String.valueOf(item.getCampo("voto").get());
                if (!voto.equals("Non sostenuto")) {
                    int CFU = Integer.parseInt(String.valueOf(item.getCampo("CFU").get()));
                    CFUTotali += CFU;

                    if (voto.equals("30L")) {
                        risultato += CFU * 32;
                    } else {
                        risultato += CFU * Integer.parseInt(voto);
                    }
                }
            }

            if (CFUTotali == 0) {
                return "N/A";
            }

            float media = risultato / CFUTotali;
            return media > 30 ? "30.00" : String.format("%.2f", media);
        } catch (Exception e) {
            e.printStackTrace();
            return "Errore";
        }
    }

    /**
     * Recupera dal database l'elenco degli esami del libretto dello studente.
     *
     * @return una lista di {@link StateItem} contenente i dati degli esami.
     * @throws SQLException in caso di errore durante l'accesso al DB.
     */
    private List<StateItem> recuperaLibretto() throws SQLException {
        controllerStudente.studente.setCommand(
                new CommandGetLibretto(
                        controllerStudente.connessione,
                        ((IGetterStudente) controllerStudente.studente).getMatricola()
                )
        );

        List<Map<String, Object>> libretto =
                (List<Map<String, Object>>) controllerStudente.studente.eseguiAzione();

        ObservableList<StateItem> listStateItems = FXCollections.observableArrayList();

        for (Map<String, Object> esame : libretto) {
            StateItem item = new StateItem();
            item.setCampo("codice_esame", (String) esame.get("codice_esame"));
            item.setCampo("nome_esame", (String) esame.get("nome_esame"));
            item.setCampo("CFU", String.valueOf(esame.get("CFU"))); // Cast a stringa per sicurezza
            item.setCampo("voto", String.valueOf(esame.get("voto")));
            item.setCampo("data_appello", (String) esame.get("data_appello"));

            listStateItems.add(item);
        }

        return listStateItems;
    }
}
