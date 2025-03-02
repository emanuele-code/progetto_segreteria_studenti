package Controllers;

import javafx.collections.FXCollections;
import javafx.scene.control.*;
import javafx.beans.property.ReadOnlyObjectWrapper;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class ControllerGestoreTableView {

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

    public static <T> void aggiornaDati(TableView<T> tableView, List<T> items) {
        tableView.setItems(FXCollections.observableArrayList(items));
    }

    public static <T> void aggiungiColonnaBottone( TableView<T> tableView, String colonnaNome, Function<T, String> buttonTextProvider, Function<T, Runnable> actionProvider) {

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
                        btn.setText(buttonTextProvider.apply(rowData));  // Testo dinamico
                        btn.setOnAction(event -> actionProvider.apply(rowData).run());
                        setGraphic(btn);
                    } else {
                        setGraphic(null);
                    }
                }
            }
        });

        tableView.getColumns().add(colButton);
    }
}
