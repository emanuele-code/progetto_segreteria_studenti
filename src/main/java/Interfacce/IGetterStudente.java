package Interfacce;

import java.util.List;

public interface IGetterStudente {
    String getNome();
    String getCognome();
    String getMatricola();
    String getResidenza();
    String getNomePiano();
    String getDataNascita();
    String isTassePagate();
    List<String> getNomeEsami();
    List<Integer> getVotiEsami();
    List<Integer> getCfuEsami();
}
