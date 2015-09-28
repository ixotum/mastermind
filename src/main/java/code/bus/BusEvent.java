package code.bus;

/**
 * Created by ixotum on 28.09.15.
 */
public class BusEvent {
    private final BusEventType type;
    private final Object content;

    public BusEvent(BusEventType type, Object content) {
        this.type = type;
        this.content = content;
    }

    public BusEventType getType() {
        return type;
    }

    public Object getContent() {
        return content;
    }
}
