package code.db.order.order_structure_component;

import java.math.BigDecimal;
import java.util.List;

public class OrderStructureComponentDB {
    private List<OrderStructureComponentRowDB> componentRowList;

    public void setComponentRowList(List<OrderStructureComponentRowDB> orderStructureComponentRowDBList) {
        this.componentRowList = orderStructureComponentRowDBList;
    }

    public int getComponentRowCount() {
        return componentRowList.size();
    }

    public OrderStructureComponentRowDB getComponentRow(int rowIndex) {
        return componentRowList.get(rowIndex);
    }

    public int getOrderId() {
        return getComponentRow(0).getOrderId();
    }

    public List<OrderStructureComponentRowDB> getComponentRowList() {
        return componentRowList;
    }

    public BigDecimal getTotal() {
        return componentRowList.stream().map(OrderStructureComponentRowDB::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
