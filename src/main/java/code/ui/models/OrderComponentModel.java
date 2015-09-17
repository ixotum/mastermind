package code.ui.models;

import code.db.order_structure_component.OrderStructureComponentDB;
import code.db.order_structure_component.OrderStructureComponentRowDB;
import code.ui.order_structure_component.OrderStructureComponentController;
import code.ui.order_structure_component.RowData;
import code.ui.order_structure_component_old.OrderStructureComponentController_old;
import code.ui.order_structure_component_old.RowData_old;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class OrderComponentModel {
    @Deprecated
    public static OrderStructureComponentDB createOrderStructureComponentDB_old(OrderStructureComponentController_old orderStructureComponentControllerOld) {
        int structureSize = orderStructureComponentControllerOld.getStructureSize() - 1;
        List<OrderStructureComponentRowDB> orderStructureComponentRowDBList = new ArrayList<>();

        for (int rowIndex = 0; rowIndex < structureSize; ++rowIndex) {
            RowData_old rowDataOld = orderStructureComponentControllerOld.getRowData(rowIndex);
            OrderStructureComponentRowDB orderStructureComponentRowDB = createOrderStructureComponentRowDB_old(rowDataOld);
            orderStructureComponentRowDBList.add(orderStructureComponentRowDB);
        }

        OrderStructureComponentDB orderStructureComponentDBOld = new OrderStructureComponentDB();
        orderStructureComponentDBOld.setComponentRowList(orderStructureComponentRowDBList);
        return orderStructureComponentDBOld;
    }

    @Deprecated
    private static OrderStructureComponentRowDB createOrderStructureComponentRowDB_old(RowData_old rowDataOld) {
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

    public static OrderStructureComponentDB createOrderStructureComponentDB(OrderStructureComponentController orderStructureComponentController) {
        final int structureSize = orderStructureComponentController.getStructureSize();
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
