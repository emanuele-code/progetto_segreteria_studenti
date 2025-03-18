package Models;

import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import Interfacce.IGetterStudente;
import Interfacce.ISetCommand;
import Interfacce.ICommand;

public class Studente<T> implements ISetCommand<T>, IGetterStudente {
    private String nome;
    private String cognome;
    private String matricola;
    private String dataNascita;
    private String residenza;
    private String nomePiano;
    private String tassePagate;
    private List<String> nomeEsami;
    private List<Integer> votiEsami;
    private List<Integer> cfuEsami;
    private ICommand<T> command;


    public Studente(String matricola, String nome, String cognome, String dataNascita, String residenza, String tassePagate,
                    String nomePiano, List<Map<String, Object>> voti) {
        this.matricola   = matricola;
        this.nome        = nome;
        this.cognome     = cognome;
        this.dataNascita = dataNascita;
        this.residenza   = residenza;
        this.tassePagate = tassePagate;
        this.nomePiano   = nomePiano;
        this.nomeEsami   = new ArrayList<>();
        this.votiEsami   = new ArrayList<>();
        this.cfuEsami    = new ArrayList<>();

        for (Map<String, Object> voto : voti) {
            nomeEsami.add((String) voto.get("nome_esame"));
            votiEsami.add((Integer) voto.get("voto"));
            cfuEsami.add((Integer) voto.get("cfu"));
        }
    }

    public String getNome(){ return this.nome; }
    public String getCognome(){ return this.cognome; }
    public String getMatricola(){ return this.matricola; }
    public String getResidenza(){ return this.residenza; }
    public String getNomePiano(){ return this.nomePiano; }
    public String getDataNascita(){ return this.dataNascita; }
    public List<String> getNomeEsami(){ return this.nomeEsami; }
    public List<Integer> getVotiEsami(){ return this.votiEsami; }
    public List<Integer> getCfuEsami(){ return this.cfuEsami; }
    public String isTassePagate(){ return this.tassePagate; }


    public void setCommand(ICommand command){
        this.command = command;
    }

    public T eseguiAzione() throws SQLException {
        return command.execute();
    }
}
