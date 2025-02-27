import java.io.IOException;
import java.sql.Connection;

import Controllers.LoginController;
import Utils.DatabaseSegreteria;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SegreteriaStudenti extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
         try {
            Connection connection = DatabaseSegreteria.getConnection();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("loginPage.fxml"));
            Parent root = loader.load();
            LoginController controller = loader.getController();
            controller.setConnection(connection);

            primaryStage.setTitle("Gestione Segreteria");
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        } catch(IOException e) {
             e.printStackTrace();
        }
    }


    public static void main( String[] args ) {
        launch(args);

/*
        boolean flag = true;
        try (Scanner scanner = new Scanner(System.in); Connection connection = DatabaseSegreteria.getConnection()) {
            while(flag){
                Autenticazione auth = new Autenticazione(connection);

                System.out.println("Scegli un'opzione:");
                System.out.println("1. Segreteria");
                System.out.println("2. Studente");
                System.out.println("3. Docente");
                System.out.println("4. Termina");

                int scelta = scanner.nextInt();
                scanner.nextLine();

                UtenteFacade menuFacade = new UtenteFacade(scanner, connection, auth);
                flag = menuFacade.gestisciRuolo(scelta);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


 */
    }
}
