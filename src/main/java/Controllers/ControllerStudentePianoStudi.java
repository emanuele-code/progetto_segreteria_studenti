package Controllers;

import Commands.CommandGetLibretto;
import Interfacce.IControllerBase;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class ControllerStudentePianoStudi implements IControllerBase<ControllerStudente> {

    @FXML private TableView   TableEsami;
    @FXML private TableColumn ColonnaCodice;
    @FXML private TableColumn ColonnaNomeEsame;
    @FXML private TableColumn ColonnaCFU;
    @FXML private TableColumn ColonnaVoto;
    @FXML private TableColumn ColonnaData;

    @FXML private Label LabelNome;
    @FXML private Label LabelCognome;
    @FXML private Label LabelMatricola;
    @FXML private Label LabelTasse;

    @FXML
    public void initialize(){
        Platform.runLater(() -> {
            LabelNome.setText(controllerStudente.studente.getNome());
            LabelCognome.setText(controllerStudente.studente.getCognome());
            LabelMatricola.setText(controllerStudente.studente.getMatricola());
            LabelTasse.setText(controllerStudente.studente.isTassePagate());

            configuraColonne();
            try {
                ObservableList<StateItem> listaStateItem = FXCollections.observableArrayList(recuperaLibretto());
                TableEsami.setItems(listaStateItem);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private ControllerStudente controllerStudente;

    @Override
    public void setController(ControllerStudente controller) throws SQLException {
        this.controllerStudente = controller;
    }

    private void configuraColonne(){
        ColonnaCodice.setCellValueFactory(new PropertyValueFactory<>("codiceEsame"));
        ColonnaNomeEsame.setCellValueFactory(new PropertyValueFactory<>("nomeEsame"));
        ColonnaCFU.setCellValueFactory(new PropertyValueFactory<>("CFU"));
        ColonnaVoto.setCellValueFactory(new PropertyValueFactory<>("voto"));
        ColonnaData.setCellValueFactory(new PropertyValueFactory<>("dataAppello"));
    }

    public class StateItem {

        private String codiceEsame;
        private String nomeEsame;
        private String CFU;
        private String voto;
        private String dataAppello;

        public StateItem(String codiceEsame, String nomeEsame, String CFU, String voto, String dataAppello) {
            this.codiceEsame = codiceEsame;
            this.nomeEsame   = nomeEsame;
            this.CFU         = CFU;
            this.voto        = voto;
            this.dataAppello = dataAppello;
        }

        public String getCodiceEsame(){
            return codiceEsame;
        }
        public String getNomeEsame(){
            return nomeEsame;
        }
        public String getVoto(){ return voto; }
        public String getCFU(){ return CFU; }
        public String getDataAppello(){ return dataAppello; }
    }

    private List<StateItem> recuperaLibretto() throws SQLException {
        controllerStudente.studente.setCommand(new CommandGetLibretto(controllerStudente.connection, controllerStudente.studente.getMatricola()));
        List<Map<String, Object>> libretto = (List<Map<String, Object>>) controllerStudente.studente.eseguiAzione();

        ObservableList<ControllerStudentePianoStudi.StateItem> listStateItems = FXCollections.observableArrayList();
        for (Map<String, Object> esame : libretto) {
            String codiceEsame = (String) esame.get("codice_esame");
            String nomeEsame   = (String) esame.get("nome_esame");
            String CFU         = (String) esame.get("CFU");
            String voto        = (String) esame.get("voto");
            String dataAppello = (String) esame.get("data_appello");

            listStateItems.add(new ControllerStudentePianoStudi.StateItem(codiceEsame, nomeEsame, CFU, voto, dataAppello));
        }
        return listStateItems;
    }

}
