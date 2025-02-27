package Controllers;

import Utils.Autenticazione;
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

public class LoginController {
    private Connection connection;
    private Autenticazione autenticazione;
    private String ruolo;
    private String credenziale;
    private String password;

    public void setConnection(Connection connection) {
        this.connection = connection;
        this.autenticazione = new Autenticazione(connection);
    }

    @FXML private TextField CredenzialiSegreteria;

    @FXML private TextField PasswordSegreteria;

    @FXML private TextField CredenzialiDocente;

    @FXML private TextField PasswordDocente;

    @FXML private TextField CredenzialiStudente;

    @FXML private TextField PasswordStudente;

    @FXML private TabPane tabPane;

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

        if(autenticazione.login(ruolo, credenziale, password)){
            apriProssimaPagina();
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
