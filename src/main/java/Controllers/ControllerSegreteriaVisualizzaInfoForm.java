package Controllers;

import Commands.CommandRicercaStudente;
import Strategy.RicercaStudentePerCredenziali;
import Strategy.RicercaStudentePerMatricola;
import Interfacce.IControllerBase;
import Interfacce.IGetterStudente;
import Interfacce.IRicercaStudenteStrategy;
import Utils.UtilAlert;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller per la visualizzazione delle informazioni sugli studenti nella vista della segreteria.
 * Supporta la ricerca per matricola o per credenziali (nome e cognome).
 */
public class ControllerSegreteriaVisualizzaInfoForm implements IControllerBase<ControllerSegreteria> {
    private ControllerSegreteria controllerSegreteria;
    private ObservableList<IGetterStudente> studentiObservableList;

    @FXML public TableView<IGetterStudente> tableRicerca;
    @FXML public TableColumn<IGetterStudente, String> ColonnaNome;
    @FXML public TableColumn<IGetterStudente, String> ColonnaCognome;
    @FXML public TableColumn<IGetterStudente, String> ColonnaNomeEsame;
    @FXML public TableColumn<IGetterStudente, String> ColonnaVoto;
    @FXML public TableColumn<IGetterStudente, String> ColonnaCFU;
    @FXML public TableColumn<IGetterStudente, String> ColonnaTasse;
    @FXML public TableColumn<IGetterStudente, String> ColonnaResidenza;
    @FXML public TableColumn<IGetterStudente, String> ColonnaDataNascita;
    @FXML public TableColumn<IGetterStudente, String> ColonnaNomePiano;

    @FXML public AnchorPane  visualizzaModalitaRicerca;
    @FXML public RadioButton RadioMatricola;
    @FXML public RadioButton RadioCredenziali;
    @FXML public TextField   cercaPerCognome;
    @FXML public TextField   cercaPerNome;
    @FXML public TextField   cercaMatricola;

    /**
     * Imposta il controller principale della segreteria.
     *
     * @param controllerSegreteria controller principale da collegare
     * @throws SQLException se si verifica un errore nella comunicazione con il database
     */
    public void setController(ControllerSegreteria controllerSegreteria) throws SQLException {
        this.controllerSegreteria = controllerSegreteria;
    }

    /**
     * Mostra l'interfaccia per scegliere la modalità di ricerca (matricola o credenziali).
     * Configura i RadioButton e la visibilità dei campi in base alla selezione.
     */
    @FXML
    public void mostraFormModalitaRicerca() {
        visualizzaModalitaRicerca.setVisible(true);
        ToggleGroup group = new ToggleGroup();

        RadioMatricola.setToggleGroup(group);
        RadioCredenziali.setToggleGroup(group);

        group.selectedToggleProperty().addListener((observable, oldToggle, newToggle) -> {
            if (newToggle == RadioMatricola) {
                mostraCampoMatricola();
            } else if (newToggle == RadioCredenziali) {
                mostraCampoCredenziali();
            }
        });

        if (RadioMatricola.isSelected()) {
            mostraCampoMatricola();
        } else {
            mostraCampoCredenziali();
        }
    }

    /**
     * Effettua la ricerca dello studente in base alla modalità selezionata (matricola o credenziali).
     * Popola la TableView con i risultati ottenuti.
     *
     * @throws SQLException se la query fallisce
     */
    @FXML
    public void cercaStudente() throws SQLException {
        List<IGetterStudente> listaStudenti = new ArrayList<>();

        if (RadioMatricola.isSelected()) {
            listaStudenti = cercaPerMatricola();
        } else if (RadioCredenziali.isSelected()) {
            listaStudenti = cercaPerCredenziali();
        }

        studentiObservableList = FXCollections.observableArrayList(listaStudenti);
        configuraColonne();
        visualizzaModalitaRicerca.setVisible(false);
    }

    /**
     * Esegue la ricerca per nome e cognome.
     *
     * @return lista di studenti trovati
     * @throws SQLException in caso di errore SQL
     */
    private List<IGetterStudente> cercaPerCredenziali() throws SQLException {
        String nome = cercaPerNome.getText().trim();
        String cognome = cercaPerCognome.getText().trim();

        if (nome.isEmpty() || cognome.isEmpty()) {
            UtilAlert.mostraErrore("Compila tutti i campi prima di inserire lo studente.",
                    "Errore", "Campi mancanti o errati");
            return new ArrayList<>();
        }

        IRicercaStudenteStrategy ricercaCredenziali =
                new RicercaStudentePerCredenziali(nome, cognome, controllerSegreteria.connessione);

        controllerSegreteria.segreteria.setCommand(
                new CommandRicercaStudente(ricercaCredenziali, controllerSegreteria.connessione));

        return (List<IGetterStudente>) controllerSegreteria.segreteria.eseguiAzione();
    }

    /**
     * Esegue la ricerca per matricola.
     *
     * @return lista di studenti trovati
     * @throws SQLException in caso di errore SQL
     */
    private List<IGetterStudente> cercaPerMatricola() throws SQLException {
        String matricola = cercaMatricola.getText().trim();

        if (matricola.isEmpty()) {
            UtilAlert.mostraErrore("Inserisci la matricola dello studente.",
                    "Errore", "Campo mancante");
            return new ArrayList<>();
        }

        IRicercaStudenteStrategy ricercaMatricola =
                new RicercaStudentePerMatricola(matricola);

        controllerSegreteria.segreteria.setCommand(
                new CommandRicercaStudente(ricercaMatricola, controllerSegreteria.connessione));

        return (List<IGetterStudente>) controllerSegreteria.segreteria.eseguiAzione();
    }

    /**
     * Mostra solo il campo per la ricerca tramite matricola.
     */
    private void mostraCampoMatricola() {
        cercaMatricola.setVisible(true);
        cercaPerCognome.setVisible(false);
        cercaPerNome.setVisible(false);
    }

    /**
     * Mostra i campi per la ricerca tramite nome e cognome.
     */
    private void mostraCampoCredenziali() {
        cercaMatricola.setVisible(false);
        cercaPerCognome.setVisible(true);
        cercaPerNome.setVisible(true);
    }

    /**
     * Configura le colonne della TableView con i dati provenienti da IGetterStudente.
     */
    private void configuraColonne() {
        ColonnaNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        ColonnaCognome.setCellValueFactory(new PropertyValueFactory<>("cognome"));
        ColonnaTasse.setCellValueFactory(new PropertyValueFactory<>("tassePagate"));
        ColonnaResidenza.setCellValueFactory(new PropertyValueFactory<>("residenza"));
        ColonnaDataNascita.setCellValueFactory(new PropertyValueFactory<>("dataNascita"));
        ColonnaNomePiano.setCellValueFactory(new PropertyValueFactory<>("nomePiano"));
        ColonnaNomeEsame.setCellValueFactory(new PropertyValueFactory<>("nomeEsami"));
        ColonnaVoto.setCellValueFactory(new PropertyValueFactory<>("votiEsami"));
        ColonnaCFU.setCellValueFactory(new PropertyValueFactory<>("cfuEsami"));

        tableRicerca.setItems(studentiObservableList);
    }
}
