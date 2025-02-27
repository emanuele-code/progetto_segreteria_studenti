package Controllers;

import java.sql.Connection;

public abstract class ControllerDB {
    protected Connection connection;

    public void setConnection(Connection connection) {
        this.connection = connection;
    }


}
