package Models;

import Interfacce.ICommand;
import Interfacce.IGetterDocente;
import Interfacce.ISetCommand;

import java.sql.SQLException;

public class Docente<T> implements ISetCommand<T>, IGetterDocente {
    private String cf;
    private String nome;
    private String cognome;
    private String email;
    private ICommand<T> command;


    public Docente(String cf, String nome, String cognome, String email) {
        this.cf = cf;
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
    }

    @Override
    public String getCf(){ return this.cf; }
    public String getNome(){ return this.nome; }
    public String getCognome(){ return this.cognome; }
    public String getEmail(){ return this.email; }

    public void setCommand(ICommand command){
        this.command = command;
    }

    public T eseguiAzione() throws SQLException {
        return command.execute();
    }
}
