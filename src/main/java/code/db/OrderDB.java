package code.db;

/**
 * Created by ixotum on 7/7/15
 */
public class OrderDB {
    private final int orderId;
    private String name;
    private String structure;

    public OrderDB(int orderId) {
        this.orderId = orderId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setStructure(String structure) {
        this.structure = structure;
    }

    public String getStructure() {
        return structure;
    }
}
