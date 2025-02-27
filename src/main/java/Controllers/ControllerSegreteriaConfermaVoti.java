package Controllers;

import Commands.CommandConfermaVoto;
import Commands.CommandGetVotiDaConfermare;
import Interfacce.IControllerBase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class ControllerSegreteriaConfermaVoti implements IControllerBase<ControllerSegreteria> {

    private ControllerSegreteria controllerSegreteria;

    @FXML private TableView   tableConferme;
    @FXML private TableColumn ColonnaConfermaMatricola;
    @FXML private TableColumn ColonnaConfermaDocente;
    @FXML private TableColumn ColonnaConfermaDataAppello;
    @FXML private TableColumn ColonnaConfermaNomeEsame;
    @FXML private TableColumn ColonnaConfermaCFU;
    @FXML private TableColumn ColonnaConferma;
    @FXML private TableColumn ColonnaConfermaCodiceAppello;
    @FXML private TableColumn ColonnaConfermaVoto;




    private List<StateItem> getVotiDaConfermare() throws SQLException {
        controllerSegreteria.segreteria.setCommand(new CommandGetVotiDaConfermare(controllerSegreteria.connection));
        List<Map<String, Object>> listaVoti = (List<Map<String, Object>>) controllerSegreteria.segreteria.eseguiAzione();

        ObservableList<StateItem> listaStateItems = FXCollections.observableArrayList();

        for (Map<String, Object> voti : listaVoti) {
            String matricola     = (String) voti.get("matricola");
            String numeroAppello = (String) voti.get("numero_appello");
            String voto          = (String) voti.get("voto");
            String docente       = (String) voti.get("cf_docente");
            String dataAppello   = (String) voti.get("data_appello");
            String nomeEsame     = (String) voti.get("nome_esame");
            String cfu           = (String) voti.get("cfu");

            listaStateItems.add(new StateItem(matricola, numeroAppello, voto, docente, dataAppello, nomeEsame, cfu));
        }

        return listaStateItems;
    }

    private void configuraColonne(){
        ColonnaConfermaMatricola.setCellValueFactory(new PropertyValueFactory<>("Matricola"));
        ColonnaConfermaCodiceAppello.setCellValueFactory(new PropertyValueFactory<>("numeroAppello"));
        ColonnaConfermaVoto.setCellValueFactory(new PropertyValueFactory<>("voto"));
        ColonnaConfermaDocente.setCellValueFactory(new PropertyValueFactory<>("docente"));
        ColonnaConfermaDataAppello.setCellValueFactory(new PropertyValueFactory<>("dataAppello"));
        ColonnaConfermaNomeEsame.setCellValueFactory(new PropertyValueFactory<>("nomeEsame"));
        ColonnaConfermaCFU.setCellValueFactory(new PropertyValueFactory<>("cfu"));
    }

    private void eseguiConfermaVoto(StateItem selectedItem){
        String matricola     = selectedItem.getMatricola();
        String numeroAppello = selectedItem.getNumeroAppello();

        Alert alert = ControllerAlert.mostraConferma("Quest azione è irreversibile", "Conferma voto", "Sei sicuro di voler inserire il voto?");
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                controllerSegreteria.segreteria.setCommand(new CommandConfermaVoto(controllerSegreteria.connection, matricola, numeroAppello));
                try {
                    controllerSegreteria.segreteria.eseguiAzione();

                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }


    public void setController(ControllerSegreteria controllerSegreteria) throws SQLException {
        this.controllerSegreteria = controllerSegreteria;
        confermaVoti();
    }

    public TableCell<StateItem, Void> creaBottoneConferma(){
        return new TableCell<>() {
            private final Button btn = new Button("Conferma");

            @Override
            protected void updateItem(java.lang.Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(btn);  // Mostriamo il bottone

                    btn.setOnAction(event -> {
                        StateItem selectedItem = getTableRow().getItem();
                        eseguiConfermaVoto(selectedItem);
                        try {
                            confermaVoti();
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    });
                }
            }
        };
    }

    public void confermaVoti() throws SQLException {

        System.out.println("TEST");
        configuraColonne();
        System.out.println("TEST 2");
        ObservableList<StateItem> listaStateItems = FXCollections.observableArrayList(getVotiDaConfermare());
        System.out.println("TEST 3");
        ColonnaConferma.setCellFactory(param -> creaBottoneConferma());
        System.out.println("TEST 4");
        tableConferme.setItems(listaStateItems);
    }

    public class StateItem {
        private String Matricola;
        private String NumeroAppello;
        private String Voto;
        private String Docente;
        private String DataAppello;
        private String NomeEsame;
        private String Cfu;

        public StateItem(String matricola, String NumeroAppello, String Voto, String docente, String dataAppello, String nomeEsame, String cfu){
            this.Matricola     = matricola;
            this.NumeroAppello = NumeroAppello;
            this.Voto          = Voto;
            this.Docente       = docente;
            this.DataAppello   = dataAppello;
            this.NomeEsame     = nomeEsame;
            this.Cfu           = cfu;
        }

        public String getMatricola(){
            return this.Matricola;
        }
        public String getNumeroAppello(){
            return this.NumeroAppello;
        }
        public String getVoto(){
            return this.Voto;
        }
        public String getDocente(){
            return this.Docente;
        }
        public String getDataAppello(){
            return this.DataAppello;
        }
        public String getNomeEsame(){
            return this.NomeEsame;
        }
        public String getCfu(){
            return this.Cfu;
        }
    }


}
