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

/**
 * Classe di utilità per la gestione del cambio di scena e caricamento di viste (FXML) nelle applicazioni JavaFX
 * del progetto Segreteria Studenti.
 *
 * Fornisce metodi statici per associare pulsanti a percorsi FXML, cambiare scena, caricare sotto-viste
 * e gestire la chiusura della sessione tornando alla login page.
 *
 * @author Emanuele Pacilio
 * @version 1.0
 */
public class UtilGestoreScena {

    /**
     * Restituisce la mappa associativa pulsanti - percorsi FXML per la segreteria.
     *
     * @param controller ControllerSegreteria che contiene i riferimenti ai pulsanti
     * @return mappa con Button come chiavi e percorsi FXML come valori
     */
    private static Map<Button, String> getSceneMapSegreteria(ControllerSegreteria controller) {
        return Map.of(
                controller.cercaStudenteButton, "/SegreteriaFXML/SegreteriaVisualizzaInfoForm.fxml",
                controller.confermaVotiButton, "/SegreteriaFXML/ConfermaVotiForm.fxml",
                controller.inserisciStudenteButton, "/SegreteriaFXML/InserisciStudenteForm.fxml",
                controller.cambiaPianoButton, "/SegreteriaFXML/SegreteriaCambiaPianoStudente.fxml"
        );
    }

    /**
     * Restituisce la mappa associativa pulsanti - percorsi FXML per il docente.
     *
     * @param controller ControllerDocente che contiene i riferimenti ai pulsanti
     * @return mappa con Button come chiavi e percorsi FXML come valori
     */
    private static Map<Button, String> getSceneMapDocente(ControllerDocente controller) {
        return Map.of(
                controller.inserisciAppelloButton, "/DocenteFXML/DocenteInserimentoAppello.fxml",
                controller.inserisciVotoButton, "/DocenteFXML/DocenteInserisciVoti.fxml"
        );
    }

    /**
     * Restituisce la mappa associativa pulsanti - percorsi FXML per lo studente.
     *
     * @param controller ControllerStudente che contiene i riferimenti ai pulsanti
     * @return mappa con Button come chiavi e percorsi FXML come valori
     */
    private static Map<Button, String> getSceneMapStudente(ControllerStudente controller) {
        return Map.of(
                controller.PrenotazioniButton, "/StudenteFXML/PrenotazioneForm.fxml",
                controller.EsitiEsamiButton, "/StudenteFXML/EsitiForm.fxml",
                controller.PianoStudiButton, "/StudenteFXML/PianoStudi.fxml"
        );
    }

    /**
     * Cambia la scena o carica una sub-vista nel contenitore, se il percorso è valido.
     *
     * @param actionEvent evento generato dal click sul pulsante
     * @param sceneMap mappa pulsanti-percorsi FXML
     * @param controller controller che implementa ICambioScena
     */
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

    /**
     * Cambia la vista principale in base al controller e al pulsante premuto.
     *
     * @param actionEvent evento generato dall'utente
     * @param controller controller della scena corrente che implementa ICambioScena
     */
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

    /**
     * Carica un sotto-view (FXML) all'interno di un AnchorPane contenitore.
     *
     * @param contenitore AnchorPane dove caricare la nuova vista
     * @param percorsoFXML percorso relativo al file FXML da caricare
     * @param controller controller principale da passare al sotto-controller
     */
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

    /**
     * Cambia completamente la scena principale dell'applicazione.
     *
     * @param actionEvent evento che ha scatenato il cambio
     * @param percorsoFXML percorso relativo del file FXML da caricare
     */
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

    /**
     * Carica un file FXML e ne restituisce il contenuto e il controller associato.
     *
     * @param <T> tipo del controller associato al FXML
     * @param percorsoFXML percorso relativo del file FXML da caricare
     * @return una coppia contenente il nodo radice e il controller, oppure null in caso di errore
     */
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

    /**
     * Gestisce l'evento di uscita, tornando alla pagina di login e mantenendo la connessione al database.
     *
     * @param event evento generato dall'azione di uscita
     * @param connection connessione al database da passare al controller di login
     */
    public static void handleExit(ActionEvent event, Connection connection) {
        String fxmlFile = "/loginPage.fxml";

        try {
            FXMLLoader loader = new FXMLLoader(UtilGestoreScena.class.getResource(fxmlFile));
            Parent root = loader.load();

            // Object controller = loader.getController();
//            if (controller instanceof ControllerLogin) {
//                ((ControllerLogin) controller).setConnection(connection);
//            }

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
