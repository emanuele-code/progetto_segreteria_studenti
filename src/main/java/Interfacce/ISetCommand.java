package Interfacce;

import java.sql.SQLException;

/**
 * Interfaccia generica per impostare un comando e eseguirlo.
 *
 * @param <T> il tipo di risultato prodotto dall'esecuzione del comando
 */
public interface ISetCommand<T> {

    /**
     * Imposta il comando da eseguire.
     *
     * @param command il comando da impostare
     */
    void setCommand(ICommand<T> command);

    /**
     * Esegue l'azione associata al comando impostato.
     *
     * @return il risultato dell'esecuzione del comando
     * @throws SQLException in caso di errori durante l'esecuzione
     */
    T eseguiAzione() throws SQLException;
}
