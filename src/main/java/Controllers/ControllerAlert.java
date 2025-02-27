package Controllers;

import javafx.scene.control.Alert;

public class ControllerAlert {


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

}
