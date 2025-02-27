package Interfacce;

import java.sql.SQLException;

public interface ICambioScena {
    public void switchForm(javafx.event.ActionEvent actionEvent) throws SQLException;
    public void handleExit(javafx.event.ActionEvent event);
}
