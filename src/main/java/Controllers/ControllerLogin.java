package Controllers;

import Interfacce.IAutenticazioneDAO;
import Models.UtenteFactory;
import Proxy.ProxyAutenticazione;
import Utils.DatabaseSegreteria;
import Utils.UtilAlert;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Controller per la gestione del login degli utenti (Segreteria, Docente, Studente).
 */
public class ControllerLogin {
    protected Connection connessione;
    private IAutenticazioneDAO autenticazione;
    private String ruolo;
    private String credenziale;
    private String password;

    @FXML public TextField CredenzialiSegreteria;
    @FXML public TextField PasswordSegreteria;
    @FXML public TextField CredenzialiDocente;
    @FXML public TextField PasswordDocente;
    @FXML public TextField CredenzialiStudente;
    @FXML public TextField PasswordStudente;
    @FXML public TabPane   tabPane;

    /**
     * Inizializza il controller, impostando il proxy di autenticazione.
     * Viene eseguito automaticamente al caricamento della scena.
     */
    @FXML
    public void initialize() {
        Platform.runLater(() -> {
            try {
                this.connessione = DatabaseSegreteria.getConnection();
                this.autenticazione = new ProxyAutenticazione(DatabaseSegreteria.getConnection());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Imposta la connessione al database.
     *
     * @param connection la connessione al database da utilizzare
     */
//    @Override
//    public void setConnection(Connection connection) {
//        this.connessione = connection;
//    }

    /**
     * Gestisce l'evento di login, verifica le credenziali e carica la pagina successiva
     * in caso di autenticazione corretta. Mostra un alert in caso contrario.
     */
    @FXML
    private void handleLogin() {
        String tabId = getTabSelezionato();
        switch (tabId) {
            case "segreteria":
                credenziale = CredenzialiSegreteria.getText();
                password = PasswordSegreteria.getText();
                ruolo = tabId;
                break;
            case "docente":
                credenziale = CredenzialiDocente.getText();
                password = PasswordDocente.getText();
                ruolo = tabId;
                break;
            case "studente":
                credenziale = CredenzialiStudente.getText();
                password = PasswordStudente.getText();
                ruolo = tabId;
                break;
        }

        if (autenticazione.isUtenteAutenticato(ruolo, credenziale, password)) {
            apriProssimaPagina();
        } else {
            UtilAlert.mostraErrore(
                    "Le credenziali sono errate",
                    "Inserisci Credenziali corrette",
                    "Credenziali errate"
            );
        }
    }

    /**
     * Apre la scena successiva in base al ruolo autenticato.
     * Carica la FXML appropriata e passa le credenziali necessarie.
     */
    @FXML
    private void apriProssimaPagina() {
        String tabId = getTabSelezionato();
        String fxmlFile = "";

        switch (tabId) {
            case "segreteria":
                fxmlFile = "../SegreteriaFXML/SegreteriaPage.fxml";
                break;
            case "docente":
                fxmlFile = "../DocenteFXML/DocentePage.fxml";
                break;
            case "studente":
                fxmlFile = "../StudenteFXML/StudentePage.fxml";
                break;
        }

        caricaProssimaPagina(fxmlFile);
    }

    /**
     * Carica e mostra la nuova pagina FXML in base al percorso specificato.
     * Imposta la connessione e inizializza l'oggetto specifico del ruolo.
     *
     * @param fxmlFile il percorso al file FXML da caricare
     */
    private void caricaProssimaPagina(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();

            Object controller = loader.getController();

            if (ruolo.equals("docente")) {
                ((ControllerDocente) controller).creaDocente(credenziale);
            } else if (ruolo.equals("studente")) {
                ((ControllerStudente) controller).creaStudente(credenziale);
            }

            Scene currentScene = tabPane.getScene();
            Stage stage = (Stage) currentScene.getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Recupera l'ID del tab attualmente selezionato (segreteria, docente, studente).
     *
     * @return l'ID del tab selezionato
     */
    private String getTabSelezionato() {
        Tab selectedTab = tabPane.getSelectionModel().getSelectedItem();
        return selectedTab.getId();
    }
}
