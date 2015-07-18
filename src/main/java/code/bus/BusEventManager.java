package code.bus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ixotum on 7/18/15
 */
public class BusEventManager {
    private static Map<BusEvent, List<BusEventListener>> mapListener = new HashMap<>();

    public static void addListener(BusEventListener listener, BusEvent event) {
        List<BusEventListener> listeners = mapListener.get(event);

        if (listeners == null) {
            listeners = new ArrayList<>();
        }

        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }

        mapListener.put(event, listeners);
    }

    public static void dispatch(BusEvent event) {
        List<BusEventListener> listeners = mapListener.get(event);

        if (listeners == null) {
            return;
        }

        for (BusEventListener listener : listeners) {
            listener.busEventDispatch(event);
        }
    }
}
