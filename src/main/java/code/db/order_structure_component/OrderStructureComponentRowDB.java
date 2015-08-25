package code.db.order_structure_component;

import java.math.BigDecimal;

public class OrderStructureComponentRowDB {
    private String item;
    private BigDecimal price;

    public void setItem(String item) {
        this.item = item;
    }

    public String getItem() {
        return item;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
