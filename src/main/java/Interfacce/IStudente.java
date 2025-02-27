package Interfacce;

import java.sql.SQLException;
import java.util.List;

public interface IStudente<T> {
    public String getNome();
    public String getCognome();
    public String getMatricola();
    public String getResidenza();
    public String getNomePiano();
    public String getDataNascita();
    public String isTassePagate();
    public List<String> getNomeEsami();
    public List<Integer> getVotiEsami();
    public List<Integer> getCfuEsami();

    public void setCommand(ICommand<T> command);

    public T eseguiAzione() throws SQLException;
}
