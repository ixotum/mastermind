package code.db.order_structure_component;

import code.ui.order_structure_component_old.OrderStructureComponentController_old;
import code.ui.order_structure_component_old.RowData_old;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class OrderComponentModel {
    public static OrderStructureComponentDB_old createOrderStructureComponentDB(OrderStructureComponentController_old orderStructureComponentControllerOld) {
        int structureSize = orderStructureComponentControllerOld.getStructureSize() - 1;
        List<OrderStructureComponentRowDB> orderStructureComponentRowDBList = new ArrayList<>();

        for (int rowIndex = 0; rowIndex < structureSize; ++rowIndex) {
            RowData_old rowDataOld = orderStructureComponentControllerOld.getRowData(rowIndex);
            OrderStructureComponentRowDB orderStructureComponentRowDB = createOrderStructureComponentRowDB(rowDataOld);
            orderStructureComponentRowDBList.add(orderStructureComponentRowDB);
        }

        OrderStructureComponentDB_old orderStructureComponentDBOld = new OrderStructureComponentDB_old();
        orderStructureComponentDBOld.setComponentRowList(orderStructureComponentRowDBList);
        return orderStructureComponentDBOld;
    }

    private static OrderStructureComponentRowDB createOrderStructureComponentRowDB(RowData_old rowDataOld) {
        OrderStructureComponentRowDB orderStructureComponentRowDB = new OrderStructureComponentRowDB();
        orderStructureComponentRowDB.setItem(rowDataOld.getColumnItem());

        String priceString = rowDataOld.getColumnPrice();
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
