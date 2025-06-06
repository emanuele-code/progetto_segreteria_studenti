package Models;

import Interfacce.ICommand;
import Interfacce.ISetCommand;

import java.sql.SQLException;

/**
 * Classe che rappresenta un'entità Segreteria in grado di eseguire comandi
 * utilizzando il pattern Command. Implementa l'interfaccia {@link ISetCommand}.
 *
 * @param <T> il tipo di risultato restituito dal comando
 */
public class Segreteria<T> implements ISetCommand<T> {
    private ICommand<T> command;

    /**
     * Imposta il comando da eseguire per questa istanza di Segreteria.
     *
     * @param command il comando da associare
     */
    public void setCommand(ICommand command) {
        this.command = command;
    }

    /**
     * Esegue il comando precedentemente impostato.
     *
     * @return il risultato dell'esecuzione del comando
     * @throws SQLException se si verifica un errore durante l'esecuzione
     */
    public T eseguiAzione() throws SQLException {
        return command.execute();
    }
}
