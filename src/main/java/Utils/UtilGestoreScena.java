package Utils;

import Controllers.*;
import Interfacce.IControllerBase;
import Interfacce.ICambioScena;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

public class UtilGestoreScena {


    private static Map<Button, String> getSceneMapSegreteria(ControllerSegreteria controller) {
        return Map.of(
                controller.cercaStudenteButton, "/SegreteriaFXML/SegreteriaVisualizzaInfoForm.fxml",
                controller.confermaVotiButton, "/SegreteriaFXML/ConfermaVotiForm.fxml",
                controller.inserisciStudenteButton, "/SegreteriaFXML/InserisciStudenteForm.fxml",
                controller.cambiaPianoButton, "/SegreteriaFXML/SegreteriaCambiaPianoStudente.fxml"
        );
    }


    private static Map<Button, String> getSceneMapDocente(ControllerDocente controller) {
        return Map.of(
                controller.inserisciAppelloButton, "/DocenteFXML/DocenteInserimentoAppello.fxml",
                controller.inserisciVotoButton, "/DocenteFXML/DocenteInserisciVoti.fxml"
        );
    }


    private static Map<Button, String> getSceneMapStudente(ControllerStudente controller) {
        return Map.of(
                controller.PrenotazioniButton, "/StudenteFXML/PrenotazioneForm.fxml",
                controller.EsitiEsamiButton, "/StudenteFXML/EsitiForm.fxml",
                controller.PianoStudiButton, "/StudenteFXML/PianoStudi.fxml"
        );
    }


    private static void cambiaScenaSeValida(ActionEvent actionEvent, Map<Button, String> sceneMap, ICambioScena controller) {
        Button sourceButton = (Button) actionEvent.getSource();
        String percorsoFXML = sceneMap.get(sourceButton);

        if (percorsoFXML != null) {
            if(controller instanceof ControllerStudente){
                caricaSubView(((ControllerStudente) controller).contenitoreSubView, percorsoFXML, controller);
                return;
            } else if(controller instanceof ControllerSegreteria){
                caricaSubView(((ControllerSegreteria) controller).contenitoreSubView, percorsoFXML, controller);
                return;
            } else if(controller instanceof ControllerDocente){
                caricaSubView(((ControllerDocente) controller).contenitoreSubView, percorsoFXML, controller);
                return;
            }
            cambiaScena(actionEvent, percorsoFXML);
        }
    }


    public static void switchForm(ActionEvent actionEvent, ICambioScena controller) {
        Map<Button, String> sceneMap = null;

        if (controller instanceof ControllerSegreteria) {
            sceneMap = getSceneMapSegreteria((ControllerSegreteria) controller);
        } else if (controller instanceof ControllerDocente) {
            sceneMap = getSceneMapDocente((ControllerDocente) controller);
        } else if (controller instanceof ControllerStudente) {
            sceneMap = getSceneMapStudente((ControllerStudente) controller);
        }

        if (sceneMap != null) {
            cambiaScenaSeValida(actionEvent, sceneMap, controller);
        }
    }


    public static void caricaSubView(AnchorPane contenitore, String percorsoFXML, ICambioScena controller) {
        try {
            FXMLLoader loader = new FXMLLoader(UtilGestoreScena.class.getResource(percorsoFXML));
            Parent nuovaVista = loader.load();

            IControllerBase<ICambioScena> subController = loader.getController();
            subController.setController(controller);

            contenitore.getChildren().clear();
            contenitore.getChildren().add(nuovaVista);
            contenitore.setVisible(true);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public static void cambiaScena(ActionEvent actionEvent, String percorsoFXML) {
        try {
            FXMLLoader loader = new FXMLLoader(UtilGestoreScena.class.getResource(percorsoFXML));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));

            stage.show();
        } catch (IOException e) {
            System.err.println("Errore durante il cambio scena: " + e.getMessage());
            e.printStackTrace();
        }
    }


    public static <T> Pair<Parent, T> caricaFXML(String percorsoFXML) {
        try {
            FXMLLoader loader = new FXMLLoader(UtilGestoreScena.class.getResource(percorsoFXML));
            Parent content = loader.load();
            T controller = loader.getController();
            return new Pair<>(content, controller); // Restituisce la vista e il controller
        } catch (IOException e) {
            e.printStackTrace();
            return null; // Oppure lanciare un'eccezione personalizzata
        }
    }


    public static void handleExit(ActionEvent event, Connection connection) {
        String fxmlFile = "/loginPage.fxml";

        try {
            FXMLLoader loader = new FXMLLoader(UtilGestoreScena.class.getResource(fxmlFile));
            Parent root = loader.load();

            Object controller = loader.getController();
            if (controller instanceof ControllerLogin) {
                ((ControllerLogin) controller).setConnection(connection);
            }

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
