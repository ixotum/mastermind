package code.db;

/**
 * Created by ixotum on 7/6/15.
 */
public class SettingsDB {
    private int last_order_id;

    public void setLastOrderId(int last_order_id) {
        this.last_order_id = last_order_id;
    }

    public int getLastOrderId() {
        return last_order_id;
    }
}
