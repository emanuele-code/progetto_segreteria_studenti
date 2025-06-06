package Controllers;

import Commands.CommandGetEsamiInsegnati;
import Interfacce.ICambioScena;
import Interfacce.IGetterDocente;
import Interfacce.ISetCommand;
import Models.UtenteFactory;
import Utils.UtilGestoreScena;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;

import java.sql.SQLException;
import java.util.Map;

/**
 * Controller dedicato alla gestione della vista docente.
 * <p>
 * Permette la visualizzazione e gestione degli esami insegnati dal docente,
 * nonché la navigazione tra diverse scene dell'applicazione.
 * </p>
 * Estende {@link ControllerLogin} e implementa l'interfaccia {@link ICambioScena}.
 *
 * @author Emanuele Pacilio
 * @version 1.0
 */
public class ControllerDocente extends ControllerLogin implements ICambioScena {

    protected ISetCommand docente;
    protected Map<String, String> mappa;

    @FXML public AnchorPane contenitoreSubView;
    @FXML public Button inserisciAppelloButton;
    @FXML public Button inserisciVotoButton;

    /**
     * Metodo collegato al cambio scena tramite pulsante.
     *
     * @param actionEvent l'evento scatenato dal clic del pulsante
     */
    @FXML
    public void switchForm(javafx.event.ActionEvent actionEvent) {
        UtilGestoreScena.switchForm(actionEvent, this);
    }

    /**
     * Metodo per gestire l'uscita dall'applicazione, chiudendo anche la connessione.
     *
     * @param event l'evento scatenato dalla richiesta di uscita
     */
    @FXML
    public void handleExit(javafx.event.ActionEvent event) {
        UtilGestoreScena.handleExit(event, connection);
    }

    /**
     * Converte il nome di un esame selezionato in un codice piano di studi.
     *
     * @param comboBox ComboBox contenente i nomi degli esami
     * @return il codice piano di studi corrispondente al nome selezionato
     */
    protected String convertiNomeToCodicePiano(ComboBox<String> comboBox){
        String codicePiano = null;
        for (Map.Entry<String, String> entry : mappa.entrySet()) {
            if (entry.getValue().equals(comboBox.getValue())) {
                codicePiano = entry.getKey();
                break;
            }
        }
        return codicePiano;
    }

    /**
     * Carica gli esami insegnati dal docente nel ComboBox passato come parametro.
     *
     * @param comboBox il ComboBox da popolare con i nomi degli esami
     * @return una mappa contenente codice e nome degli esami insegnati
     * @throws SQLException se si verifica un errore nell'accesso al database
     */
    protected Map<String, String> caricaEsami(ComboBox<String> comboBox) throws SQLException {
        docente.setCommand(new CommandGetEsamiInsegnati(connection, ((IGetterDocente)docente).getCf()));
        mappa = (Map<String, String>)docente.eseguiAzione();

        for (Map.Entry<String, String> entry : mappa.entrySet()) {
            comboBox.getItems().add(entry.getValue());
        }

        return mappa;
    }

    /**
     * Crea un'istanza del docente a partire dal codice fiscale.
     *
     * @param codiceFiscale il codice fiscale del docente
     * @throws SQLException se si verifica un errore durante l'accesso al database
     */
    protected void creaDocente(String codiceFiscale) throws SQLException {
        docente = UtenteFactory.creaDocente(codiceFiscale, connection);
    }

}
