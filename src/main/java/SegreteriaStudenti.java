import java.io.IOException;
import java.sql.Connection;

import Controllers.ControllerLogin;
import Utils.DatabaseSegreteria;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Classe principale dell'applicazione Segreteria Studenti.
 * <p>
 * Estende {@link Application} di JavaFX e gestisce l'avvio dell'interfaccia grafica.
 * All'avvio, stabilisce la connessione al database e carica la pagina di login.
 * </p>
 * <p>
 * Contiene anche un main di avvio dell'applicazione.
 * </p>
 *
 * @author Emanuele Pacilio
 * @version 1.0
 */
public class SegreteriaStudenti extends Application {

    /**
     * Metodo principale di avvio dell'applicazione JavaFX.
     *
     * @param primaryStage Lo stage principale su cui caricare la scena.
     * @throws Exception se si verifica un errore durante il caricamento della GUI o la connessione al database.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            Connection connection = DatabaseSegreteria.getConnection();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("loginPage.fxml"));
            Parent root = loader.load();
            ControllerLogin controller = loader.getController();
            controller.setConnection(connection);

            primaryStage.setTitle("Gestione Segreteria");
            primaryStage.setScene(new Scene(root));
            primaryStage.setResizable(false); // Impedisce il ridimensionamento della finestra
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Metodo main per lanciare l'applicazione JavaFX.
     *
     * @param args Argomenti della linea di comando.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
