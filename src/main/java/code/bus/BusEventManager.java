package code.bus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ixotum on 7/18/15
 */
public class BusEventManager {
    private static Map<BusEventType, List<BusEventListener>> mapListener = new HashMap<>();

    public static void addListener(BusEventListener listener, BusEventType eventType) {
        List<BusEventListener> listeners = mapListener.get(eventType);

        if (listeners == null) {
            listeners = new ArrayList<>();
        }

        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }

        mapListener.put(eventType, listeners);
    }

    public static void dispatch(BusEvent busEvent) {
        List<BusEventListener> listeners = mapListener.get(busEvent.getType());

        if (listeners == null) {
            return;
        }

        for (BusEventListener listener : listeners) {
            listener.busEventDispatched(busEvent);
        }
    }
}
