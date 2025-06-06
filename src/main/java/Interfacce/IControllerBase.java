package Interfacce;

import java.sql.SQLException;

/**
 * Interfaccia base per i controller che possono essere collegati a un altro controller di tipo generico T.
 *
 * @param <T> tipo del controller a cui questo controller può essere associato
 */
public interface IControllerBase<T> {

    /**
     * Imposta il controller genitore o associato.
     *
     * @param controller il controller da associare
     * @throws SQLException se si verifica un errore di accesso al database durante l'impostazione
     */
    void setController(T controller) throws SQLException;
}
