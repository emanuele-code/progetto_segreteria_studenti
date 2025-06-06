package Interfacce;

import java.sql.SQLException;

/**
 * Interfaccia generica che rappresenta un comando eseguibile che può restituire un risultato di tipo T.
 *
 * @param <T> tipo del risultato restituito dall'esecuzione del comando
 */
public interface ICommand<T> {

    /**
     * Esegue il comando.
     *
     * @return il risultato dell'esecuzione del comando di tipo T
     * @throws SQLException se si verifica un errore durante l'accesso al database
     */
    T execute() throws SQLException;
}
