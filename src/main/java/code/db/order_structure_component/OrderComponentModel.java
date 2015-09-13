package code.db.order_structure_component;

import code.ui.order_structure_component.OrderStructureComponentController_old;
import code.ui.order_structure_component.RowData;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class OrderComponentModel {
    public static OrderStructureComponentDB_old createOrderStructureComponentDB(OrderStructureComponentController_old orderStructureComponentControllerOld) {
        int structureSize = orderStructureComponentControllerOld.getStructureSize() - 1;
        List<OrderStructureComponentRowDB> orderStructureComponentRowDBList = new ArrayList<>();

        for (int rowIndex = 0; rowIndex < structureSize; ++rowIndex) {
            RowData rowData = orderStructureComponentControllerOld.getRowData(rowIndex);
            OrderStructureComponentRowDB orderStructureComponentRowDB = createOrderStructureComponentRowDB(rowData);
            orderStructureComponentRowDBList.add(orderStructureComponentRowDB);
        }

        OrderStructureComponentDB_old orderStructureComponentDBOld = new OrderStructureComponentDB_old();
        orderStructureComponentDBOld.setComponentRowList(orderStructureComponentRowDBList);
        return orderStructureComponentDBOld;
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
