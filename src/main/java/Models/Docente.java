package Models;

import Interfacce.ICommand;
import Interfacce.IGetterDocente;
import Interfacce.ISetCommand;

import java.sql.SQLException;

/**
 * Classe che rappresenta un docente con i relativi dati anagrafici e
 * che implementa i pattern command per l'esecuzione di azioni specifiche.
 *
 * @param <T> il tipo di risultato prodotto dall'esecuzione del comando
 */
public class Docente<T> implements ISetCommand<T>, IGetterDocente {
    private String cf;
    private String nome;
    private String cognome;
    private String email;
    private ICommand<T> command;

    /**
     * Costruisce un nuovo docente con le informazioni specificate.
     *
     * @param cf      codice fiscale del docente
     * @param nome    nome del docente
     * @param cognome cognome del docente
     * @param email   email del docente
     */
    public Docente(String cf, String nome, String cognome, String email) {
        this.cf = cf;
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
    }

    /**
     * Restituisce il codice fiscale del docente.
     *
     * @return codice fiscale
     */
    @Override
    public String getCf() {
        return this.cf;
    }

    /**
     * Restituisce il nome del docente.
     *
     * @return nome
     */
    public String getNome() {
        return this.nome;
    }

    /**
     * Restituisce il cognome del docente.
     *
     * @return cognome
     */
    public String getCognome() {
        return this.cognome;
    }

    /**
     * Restituisce l'email del docente.
     *
     * @return email
     */
    public String getEmail() {
        return this.email;
    }

    /**
     * Imposta il comando da eseguire per questa istanza di docente.
     *
     * @param command il comando da impostare
     */
    public void setCommand(ICommand command) {
        this.command = command;
    }

    /**
     * Esegue il comando associato e restituisce il risultato.
     *
     * @return risultato dell'esecuzione del comando
     * @throws SQLException in caso di errore durante l'esecuzione del comando
     */
    public T eseguiAzione() throws SQLException {
        return command.execute();
    }
}
