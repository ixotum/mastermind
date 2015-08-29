package code.db.order_structure_component;

import code.ui.order_structure_component.OrderStructureComponentController;
import code.ui.order_structure_component.RowData;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class OrderComponentModel {
    public static OrderStructureComponentDB createOrderStructureComponentDB(OrderStructureComponentController orderStructureComponentController) {
        int structureSize = orderStructureComponentController.getStructureSize() - 1;
        List<OrderStructureComponentRowDB> orderStructureComponentRowDBList = new ArrayList<>();

        for (int rowIndex = 0; rowIndex < structureSize; ++rowIndex) {
            RowData rowData = orderStructureComponentController.getRowData(rowIndex);
            OrderStructureComponentRowDB orderStructureComponentRowDB = createOrderStructureComponentRowDB(rowData);
            orderStructureComponentRowDBList.add(orderStructureComponentRowDB);
        }

        OrderStructureComponentDB orderStructureComponentDB = new OrderStructureComponentDB();
        orderStructureComponentDB.setComponentRowList(orderStructureComponentRowDBList);
        return orderStructureComponentDB;
    }

    private static OrderStructureComponentRowDB createOrderStructureComponentRowDB(RowData rowData) {
        OrderStructureComponentRowDB orderStructureComponentRowDB = new OrderStructureComponentRowDB();
        orderStructureComponentRowDB.setItem(rowData.getColumnItem());

        String priceString = rowData.getColumnPrice();
        BigDecimal price;

        try {
            price = new BigDecimal(priceString);
        } catch (NumberFormatException e) {
            price = BigDecimal.ZERO;
        }

        orderStructureComponentRowDB.setPrice(price);

        return orderStructureComponentRowDB;
    }
}
