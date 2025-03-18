package Utils;

import javafx.scene.Parent;
import javafx.scene.control.Alert;

public class UtilAlert {


    public static Alert mostraConferma(String messaggio, String titolo, String testoHeader){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(titolo);
        alert.setHeaderText(testoHeader);
        alert.setContentText(messaggio);
        return alert;
    }


    public static Alert mostraErrore(String messaggio, String titolo, String testoHeader) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titolo);
        alert.setHeaderText(testoHeader);
        alert.setContentText(messaggio);
        alert.showAndWait();
        return alert;
    }


    public static Alert mostraInfo(String messaggio, String titolo, String testoHeader, String info){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titolo);
        alert.setHeaderText(testoHeader);
        alert.setContentText(messaggio + " " + info);
        alert.showAndWait();
        return alert;
    }


    public static Alert mostraPagina(String titolo, String header, Parent content){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(titolo);
        alert.setHeaderText(header);
        alert.getDialogPane().setContent(content); // Imposta il contenuto FXML
        return alert;
    }


}
