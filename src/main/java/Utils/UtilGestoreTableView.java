package Utils;

import javafx.scene.control.*;
import javafx.beans.property.ReadOnlyObjectWrapper;
import java.util.Map;
import java.util.function.Function;

/**
 * Classe di utilità per la configurazione dinamica di TableView in JavaFX.
 * Permette di configurare colonne basate su mappa di proprietà e di aggiungere colonne con bottoni dinamici.
 *
 * @author Emanuele
 * @version 1.0
 */
public class UtilGestoreTableView {

    /**
     * Configura automaticamente le colonne di una TableView a partire da una mappa.
     *
     * Ogni entry nella mappa rappresenta una colonna:
     * - la chiave è il nome della colonna,
     * - il valore è una funzione che, dato un elemento della tabella, restituisce il valore da visualizzare in quella colonna.
     *
     * @param <T>          tipo degli elementi contenuti nella TableView
     * @param tableView    la TableView da configurare
     * @param colonneMappa mappa che associa il nome della colonna alla funzione di estrazione del valore
     */
    public static <T> void configuraColonne(TableView<T> tableView, Map<String, Function<T, ?>> colonneMappa) {
        tableView.getColumns().clear();

        colonneMappa.forEach((colonnaNome, propertyGetter) -> {
            TableColumn<T, Object> column = new TableColumn<>(colonnaNome);
            column.setCellValueFactory(cellData ->
                    new ReadOnlyObjectWrapper<>(propertyGetter.apply(cellData.getValue()))
            );
            tableView.getColumns().add(column);
        });
    }

    /**
     * Aggiunge una colonna contenente bottoni alla TableView.
     *
     * La colonna visualizza un bottone per ogni riga che soddisfa la condizione data dalla funzione {@code crea}.
     * Il testo del bottone è dinamico e determinato da {@code buttonTextProvider}.
     * L'azione eseguita al click del bottone è definita da {@code actionProvider}.
     *
     * @param <T>               tipo degli elementi contenuti nella TableView
     * @param tableView         la TableView a cui aggiungere la colonna bottone
     * @param colonnaNome       il nome della colonna bottone
     * @param crea              funzione che, data una riga, restituisce true se il bottone deve essere mostrato
     * @param buttonTextProvider funzione che, data una riga, restituisce il testo da visualizzare sul bottone
     * @param actionProvider    funzione che, data una riga, restituisce un Runnable rappresentante l'azione da eseguire al click
     */
    public static <T> void aggiungiColonnaBottone(TableView<T> tableView, String colonnaNome, Function<T, Boolean> crea, Function<T, String> buttonTextProvider, Function<T, Runnable> actionProvider) {
        TableColumn<T, Void> colButton = new TableColumn<>(colonnaNome);

        colButton.setCellFactory(param -> new TableCell<>() {
            private final Button btn = new Button();

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    T rowData = getTableRow().getItem();
                    if (rowData != null) {
                        if (crea.apply(rowData)) {
                            btn.setText(buttonTextProvider.apply(rowData));
                            btn.setOnAction(event -> actionProvider.apply(rowData).run());
                            setGraphic(btn);
                        } else {
                            setGraphic(null);
                        }
                    } else {
                        setGraphic(null);
                    }
                }
            }
        });

        tableView.getColumns().add(colButton);
    }
}
