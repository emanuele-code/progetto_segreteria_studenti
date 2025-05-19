package Controllers;

import Interfacce.IAutenticazioneDAO;
import Proxy.ProxyAutenticazione;
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

public class ControllerLogin extends ControllerDB {
    //private UtilAutenticazione autenticazione;
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


    @FXML
    public void initialize(){
        Platform.runLater(() -> {
            try {
                this.autenticazione = new ProxyAutenticazione(connection);
                //this.autenticazione = new UtilAutenticazione(connection);
            } catch(Exception e){
                throw new RuntimeException(e);
            }
        });
    }


    @Override
    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    @FXML private void handleLogin() {
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

        if(autenticazione.isUtenteAutenticato(ruolo, credenziale, password)){
            apriProssimaPagina();
        } else {
            UtilAlert.mostraErrore("Le credenziali sono errate", "Inserisci Credenziali corrette", "Credenziali errate");
        }
    }

    @FXML private void apriProssimaPagina() {
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

    private void caricaProssimaPagina(String fxmlFile){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();

            Object controller = loader.getController();
            if (controller instanceof ControllerDB) {
                ((ControllerDB) controller).setConnection(this.connection);
            }

            if(ruolo.equals("docente")){
                ((ControllerDocente) controller).setCodiceFiscale(credenziale);
            } else if(ruolo.equals("studente")){
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

    private String getTabSelezionato() {
        Tab selectedTab = tabPane.getSelectionModel().getSelectedItem();
        return selectedTab.getId();
    }
}
