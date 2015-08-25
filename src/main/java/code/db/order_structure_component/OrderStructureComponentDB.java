package code.db.order_structure_component;

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
}
