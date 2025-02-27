package Interfacce;

import Controllers.ControllerStudente;

import java.sql.SQLException;

public interface ControllerBaseStudente {
    public void setController(ControllerStudente controller) throws SQLException;
}
