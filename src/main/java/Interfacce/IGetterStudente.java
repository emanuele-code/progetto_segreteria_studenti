package Interfacce;

import java.util.List;

/**
 * Interfaccia che definisce i metodi getter per le informazioni di uno studente.
 */
public interface IGetterStudente {

    /**
     * Restituisce il nome dello studente.
     *
     * @return nome dello studente
     */
    String getNome();

    /**
     * Restituisce il cognome dello studente.
     *
     * @return cognome dello studente
     */
    String getCognome();

    /**
     * Restituisce la matricola dello studente.
     *
     * @return matricola dello studente
     */
    String getMatricola();

    /**
     * Restituisce la residenza dello studente.
     *
     * @return residenza dello studente
     */
    String getResidenza();

    /**
     * Restituisce il nome del piano di studi dello studente.
     *
     * @return nome del piano di studi
     */
    String getNomePiano();

    /**
     * Restituisce la data di nascita dello studente.
     *
     * @return data di nascita dello studente
     */
    String getDataNascita();

    /**
     * Indica se lo studente ha pagato le tasse.
     *
     * @return stringa che indica lo stato del pagamento delle tasse (es. "true"/"false" o altro formato)
     */
    String isTassePagate();

    /**
     * Restituisce la lista dei nomi degli esami sostenuti dallo studente.
     *
     * @return lista dei nomi degli esami
     */
    List<String> getNomeEsami();

    /**
     * Restituisce la lista dei voti degli esami sostenuti dallo studente.
     *
     * @return lista dei voti degli esami
     */
    List<Integer> getVotiEsami();

    /**
     * Restituisce la lista dei CFU degli esami sostenuti dallo studente.
     *
     * @return lista dei CFU degli esami
     */
    List<Integer> getCfuEsami();
}
