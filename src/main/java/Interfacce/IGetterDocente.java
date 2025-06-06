package Interfacce;

/**
 * Interfaccia che definisce i metodi getter per le informazioni di un docente.
 */
public interface IGetterDocente {

    /**
     * Restituisce il codice fiscale del docente.
     *
     * @return codice fiscale (CF) del docente
     */
    String getCf();

    /**
     * Restituisce il nome del docente.
     *
     * @return nome del docente
     */
    String getNome();

    /**
     * Restituisce il cognome del docente.
     *
     * @return cognome del docente
     */
    String getCognome();

    /**
     * Restituisce l'email del docente.
     *
     * @return email del docente
     */
    String getEmail();
}
