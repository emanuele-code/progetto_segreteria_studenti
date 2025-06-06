package Models;

import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import Interfacce.IGetterStudente;
import Interfacce.ISetCommand;
import Interfacce.ICommand;

/**
 * Classe che rappresenta un'entità Studente all'interno del sistema.
 * Implementa {@link IGetterStudente} per fornire accesso ai dati dello studente,
 * e {@link ISetCommand} per supportare l'esecuzione di comandi tramite il pattern Command.
 *
 * @param <T> il tipo di ritorno del comando eseguito tramite {@link ICommand}
 */
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

    /**
     * Costruttore della classe Studente.
     *
     * @param matricola    matricola dello studente
     * @param nome         nome dello studente
     * @param cognome      cognome dello studente
     * @param dataNascita  data di nascita dello studente
     * @param residenza    residenza dello studente
     * @param tassePagate  stato del pagamento delle tasse ("sì"/"no")
     * @param nomePiano    nome del piano di studi
     * @param voti         lista di mappe contenente informazioni sugli esami (nome, voto, cfu)
     */
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

    /**
     * @return nome dello studente
     */
    public String getNome() { return this.nome; }

    /**
     * @return cognome dello studente
     */
    public String getCognome() { return this.cognome; }

    /**
     * @return matricola dello studente
     */
    public String getMatricola() { return this.matricola; }

    /**
     * @return residenza dello studente
     */
    public String getResidenza() { return this.residenza; }

    /**
     * @return nome del piano di studi dello studente
     */
    public String getNomePiano() { return this.nomePiano; }

    /**
     * @return data di nascita dello studente
     */
    public String getDataNascita() { return this.dataNascita; }

    /**
     * @return elenco dei nomi degli esami sostenuti
     */
    public List<String> getNomeEsami() { return this.nomeEsami; }

    /**
     * @return elenco dei voti ottenuti negli esami
     */
    public List<Integer> getVotiEsami() { return this.votiEsami; }

    /**
     * @return elenco dei CFU associati agli esami sostenuti
     */
    public List<Integer> getCfuEsami() { return this.cfuEsami; }

    /**
     * @return stato del pagamento delle tasse ("sì"/"no")
     */
    public String isTassePagate() { return this.tassePagate; }

    /**
     * Imposta il comando da eseguire su questo oggetto.
     *
     * @param command comando da eseguire
     */
    public void setCommand(ICommand command) {
        this.command = command;
    }

    /**
     * Esegue l'azione associata al comando impostato.
     *
     * @return il risultato dell'esecuzione del comando
     * @throws SQLException se si verifica un errore durante l'esecuzione
     */
    public T eseguiAzione() throws SQLException {
        return command.execute();
    }
}
