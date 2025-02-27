package Interfacce;

import Controllers.ControllerDocente;

import java.sql.SQLException;

public interface ControllerBaseDocente {
    public void setController(ControllerDocente controller) throws SQLException;
    public void valorizzaTabellaAppelli() throws SQLException;
}
