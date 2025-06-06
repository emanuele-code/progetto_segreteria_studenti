package Models;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.util.HashMap;
import java.util.Map;

/**
 * Classe che rappresenta un contenitore generico di proprietà osservabili,
 * utile per costruire dinamicamente colonne in una {@code TableView} utilizzando JavaFX.
 * <p>
 * Ogni proprietà viene associata a una chiave di tipo {@code String} ed è gestita tramite {@code ObjectProperty<?>}.
 */
public class StateItem {

    /**
     * Mappa che associa una chiave stringa a una proprietà osservabile ({@code ObjectProperty}).
     */
    private final Map<String, ObjectProperty<?>> campi = new HashMap<>();

    /**
     * Imposta un campo nella mappa con una determinata chiave e valore.
     * Se la chiave esiste già, sovrascrive la proprietà esistente.
     *
     * @param <T>    il tipo del valore da memorizzare
     * @param chiave la chiave che identifica il campo
     * @param valore il valore da associare alla chiave come proprietà osservabile
     */
    public <T> void setCampo(String chiave, T valore) {
        campi.put(chiave, new SimpleObjectProperty<>(valore));
    }

    /**
     * Restituisce la {@code ObjectProperty} associata alla chiave specificata.
     *
     * @param <T>    il tipo della proprietà
     * @param chiave la chiave del campo da recuperare
     * @return la proprietà osservabile associata alla chiave, oppure {@code null} se non esiste
     */
    public <T> ObjectProperty<T> getCampo(String chiave) {
        return (ObjectProperty<T>) campi.get(chiave);
    }

    /**
     * Restituisce il valore attuale associato alla chiave specificata.
     *
     * @param <T>    il tipo del valore
     * @param chiave la chiave del campo da cui ottenere il valore
     * @return il valore contenuto nella proprietà associata, oppure {@code null} se la chiave non esiste
     */
    public <T> T getValore(String chiave) {
        return (T) campi.get(chiave).get();
    }
}
