package Utils;

import javafx.scene.control.*;
import javafx.beans.property.ReadOnlyObjectWrapper;
import java.util.Map;
import java.util.function.Function;

public class UtilGestoreTableView {

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
                        // Usa `crea.apply(rowData)` per ottenere il valore booleano
                        if (crea.apply(rowData)) {
                            btn.setText(buttonTextProvider.apply(rowData));  // Testo dinamico
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
