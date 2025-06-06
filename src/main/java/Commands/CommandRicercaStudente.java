package Commands;

import Interfacce.ICommand;
import Interfacce.IRicercaStudenteStrategy;
import Interfacce.ISetCommand;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Comando per eseguire una ricerca di studenti utilizzando una strategia specifica.
 * <p>
 * Implementa l'interfaccia {@link ICommand} e utilizza una strategia {@link IRicercaStudenteStrategy}
 * per effettuare la ricerca nel database.
 * </p>
 * <p>
 * Restituisce una lista di {@link ISetCommand} come risultato della ricerca.
 * </p>
 *
 * @author Emanuele Pacilio
 * @version 1.0
 */
public class CommandRicercaStudente implements ICommand<List<ISetCommand>> {

    private IRicercaStudenteStrategy strategy;
    private Connection connection;

    /**
     * Costruisce un nuovo comando per la ricerca di studenti.
     *
     * @param strategy   Strategia di ricerca da utilizzare.
     * @param connection Connessione al database da utilizzare.
     */
    public CommandRicercaStudente(IRicercaStudenteStrategy strategy, Connection connection){
        this.strategy = strategy;
        this.connection = connection;
    }

    /**
     * Esegue la ricerca degli studenti utilizzando la strategia fornita.
     *
     * @return lista di {@link ISetCommand} risultante dalla ricerca.
     * @throws SQLException se si verifica un errore durante la ricerca nel database.
     */
    @Override
    public List<ISetCommand> execute() throws SQLException {
        return strategy.ricercaStudente(connection);
    }
}
