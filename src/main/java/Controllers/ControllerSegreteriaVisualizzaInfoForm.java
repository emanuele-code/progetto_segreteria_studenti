package Controllers;

import Commands.CommandRicercaStudente;
import Commands.RicercaStudentePerCredenziali;
import Commands.RicercaStudentePerMatricola;
import Interfacce.ControllerBase;
import Interfacce.IRicercaStudenteStrategy;
import Interfacce.IStudente;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;



public class ControllerSegreteriaVisualizzaInfoForm implements ControllerBase<ControllerSegreteria> {

    @FXML private TableView   tableRicerca;
    @FXML private TableColumn ColonnaNome;
    @FXML private TableColumn ColonnaCognome;
    @FXML private TableColumn ColonnaNomeEsame;
    @FXML private TableColumn ColonnaVoto;
    @FXML private TableColumn ColonnaCFU;
    @FXML private TableColumn ColonnaTasse;
    @FXML private TableColumn ColonnaResidenza;
    @FXML private TableColumn ColonnaDataNascita;
    @FXML private TableColumn ColonnaNomePiano;

    @FXML private AnchorPane  visualizzaModalitaRicerca;
    @FXML private RadioButton RadioMatricola;
    @FXML private RadioButton RadioCredenziali;
    @FXML private TextField   cercaPerCognome;
    @FXML private TextField   cercaPerNome;
    @FXML private TextField   cercaMatricola;


    private ControllerSegreteria controllerSegreteria;
    private ObservableList<IStudente> studentiObservableList;


    public void setController(ControllerSegreteria controllerSegreteria) throws SQLException {
        this.controllerSegreteria = controllerSegreteria;
    }


    @FXML public void mostraFormModalitaRicerca(){
        visualizzaModalitaRicerca.setVisible(true);
        ToggleGroup group = new ToggleGroup();

        RadioMatricola.setToggleGroup(group);
        RadioCredenziali.setToggleGroup(group);

        group.selectedToggleProperty().addListener((observable, oldToggle, newToggle) -> {
            if (newToggle == RadioMatricola) {
                mostraCampoMatricola();  // Mostra il campo "matricola"
            } else if (newToggle == RadioCredenziali) {
                mostraCampoCredenziali();  // Mostra i campi "nome" e "cognome"
            }
        });

        if (RadioMatricola.isSelected()) {
            mostraCampoMatricola();
        } else {
            mostraCampoCredenziali();
        }

    }

    @FXML public void cercaStudente() throws SQLException {
        List<IStudente> listaStudenti = new ArrayList<>();

        if (RadioMatricola.isSelected()) {
            listaStudenti = cercaPerMatricola();
        } else if (RadioCredenziali.isSelected()) {
            listaStudenti = cercaPerCredenziali();
        }

        studentiObservableList = FXCollections.observableArrayList(listaStudenti);

        configuraColonne();

        visualizzaModalitaRicerca.setVisible(false);
    }

    private List<IStudente> cercaPerCredenziali() throws SQLException {
        String nome = cercaPerNome.getText().trim();
        String cognome = cercaPerCognome.getText().trim();


        if (nome.isEmpty() || cognome.isEmpty()) {
            ControllerAlert.mostraErrore("Compila tutti i campi prima di inserire lo studente.", "Errore", "Campi mancanti o errati");
            return new ArrayList<>();
        }

        IRicercaStudenteStrategy ricercaCredenziali = new RicercaStudentePerCredenziali(nome, cognome, controllerSegreteria.connection);
        controllerSegreteria.segreteria.setCommand(new CommandRicercaStudente(ricercaCredenziali, controllerSegreteria.connection));
        return (List<IStudente>) controllerSegreteria.segreteria.eseguiAzione();
    }

    private List<IStudente> cercaPerMatricola() throws SQLException {
        String matricola = cercaMatricola.getText().trim();

        if (matricola.isEmpty()) {
            ControllerAlert.mostraErrore("Inserisci sia il nome che il cognome.", "Errore", "Campi mancanti o errati");
            return new ArrayList<>();
        }

        IRicercaStudenteStrategy ricercaMatricola = new RicercaStudentePerMatricola(matricola);
        controllerSegreteria.segreteria.setCommand(new CommandRicercaStudente(ricercaMatricola, controllerSegreteria.connection));
        return (List<IStudente>) controllerSegreteria.segreteria.eseguiAzione();
    }

    private void mostraCampoMatricola() {
        // Rende visibile solo il campo "matricola" e nasconde gli altri
        cercaMatricola.setVisible(true);
        cercaPerCognome.setVisible(false);
        cercaPerNome.setVisible(false);
    }

    private void mostraCampoCredenziali() {
        // Rende visibili i campi "nome" e "cognome" e nasconde l'altro
        cercaMatricola.setVisible(false);
        cercaPerCognome.setVisible(true);
        cercaPerNome.setVisible(true);
    }

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
