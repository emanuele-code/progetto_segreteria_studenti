package Models;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.util.HashMap;
import java.util.Map;

public class StateItem {
    private final Map<String, ObjectProperty<?>> campi = new HashMap<>();

    public <T> void setCampo(String chiave, T valore){
        campi.put(chiave, new SimpleObjectProperty<>(valore));
    }

    public <T> ObjectProperty<T> getCampo(String chiave) {
        return (ObjectProperty<T>) campi.get(chiave);
    }

    public <T> T getValore(String chiave) {
        return (T) campi.get(chiave).get();
    }

}
