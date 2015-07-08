package code.db;

/**
 * Created by ixotum on 7/7/15.
 */
public class OrderDB {
    private final int orderId;

    public OrderDB(int orderId) {
        this.orderId = orderId;
    }

    public int getOrderId() {
        return orderId;
    }
}
