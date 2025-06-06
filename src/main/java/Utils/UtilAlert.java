package Utils;

import javafx.scene.Parent;
import javafx.scene.control.Alert;

/**
 * Classe di utilità per la creazione e visualizzazione di finestre di dialogo di tipo Alert in JavaFX.
 * Offre metodi statici per mostrare alert di conferma, errore, informazioni e per mostrare contenuti personalizzati.
 *
 * @author Emanuele Pacilio
 * @version 1.0
 */
public class UtilAlert {

    /**
     * Crea un Alert di tipo conferma con titolo, header e messaggio specificati.
     *
     * @param messaggio  Il testo del messaggio da visualizzare
     * @param titolo     Il titolo della finestra di dialogo
     * @param testoHeader Il testo dell'intestazione dell'alert
     * @return l'oggetto Alert configurato ma non mostrato
     */
    public static Alert mostraConferma(String messaggio, String titolo, String testoHeader){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(titolo);
        alert.setHeaderText(testoHeader);
        alert.setContentText(messaggio);
        return alert;
    }

    /**
     * Crea e mostra un Alert di tipo errore con titolo, header e messaggio specificati.
     *
     * @param messaggio  Il testo del messaggio di errore
     * @param titolo     Il titolo della finestra di dialogo
     * @param testoHeader Il testo dell'intestazione dell'alert
     * @return l'oggetto Alert mostrato all'utente
     */
    public static Alert mostraErrore(String messaggio, String titolo, String testoHeader) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titolo);
        alert.setHeaderText(testoHeader);
        alert.setContentText(messaggio);
        alert.showAndWait();
        return alert;
    }

    /**
     * Crea e mostra un Alert di tipo informazione con titolo, header, messaggio e informazioni aggiuntive.
     *
     * @param messaggio  Il testo principale del messaggio
     * @param titolo     Il titolo della finestra di dialogo
     * @param testoHeader Il testo dell'intestazione dell'alert
     * @param info       Ulteriori informazioni da aggiungere al messaggio
     * @return l'oggetto Alert mostrato all'utente
     */
    public static Alert mostraInfo(String messaggio, String titolo, String testoHeader, String info){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titolo);
        alert.setHeaderText(testoHeader);
        alert.setContentText(messaggio + " " + info);
        alert.showAndWait();
        return alert;
    }

    /**
     * Crea un Alert di tipo conferma con contenuto personalizzato (ad esempio un nodo JavaFX).
     *
     * @param titolo  Il titolo della finestra di dialogo
     * @param header  Il testo dell'intestazione dell'alert
     * @param content Il contenuto da inserire nel corpo dell'alert (ad esempio un layout FXML)
     * @return l'oggetto Alert configurato ma non mostrato
     */
    public static Alert mostraPagina(String titolo, String header, Parent content){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(titolo);
        alert.setHeaderText(header);
        alert.getDialogPane().setContent(content); // Imposta il contenuto FXML
        return alert;
    }
}
