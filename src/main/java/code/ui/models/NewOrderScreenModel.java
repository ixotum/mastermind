package code.ui.models;

import code.db.OrderDB;
import code.db.order_structure_component.OrderStructureComponentDB;
import code.db.order_structure_component.OrderStructureComponentRowDB;
import code.ui.NewOrderScreenController;
import code.ui.OrderComponentController;
import code.ui.order_structure_component.OrderStructureComponentController;
import code.ui.order_structure_component.RowData;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class NewOrderScreenModel {
    private final NewOrderScreenController controller;

    public NewOrderScreenModel(NewOrderScreenController newOrderScreenController) {
        this.controller = newOrderScreenController;
    }

    public OrderDB createOrderDB(int orderId) {
        OrderComponentController orderComponent = controller.getOrderComponent();
        OrderDB orderDB = new OrderDB(orderId);
        orderDB.setName(orderComponent.getTextFieldName().getText());
        orderDB.setStructure(orderComponent.getTextAreaStructure().getText());
        orderDB.setPrice(orderComponent.getTextFieldPrice().getText());

        OrderStructureComponentController orderStructureComponentController = orderComponent.getOrderStructureComponentController();
        OrderStructureComponentDB orderStructureComponentDB = createOrderStructureComponentDB(orderStructureComponentController);
        orderDB.setOrderStructureComponentDB(orderStructureComponentDB);

        orderDB.setCustomer(orderComponent.getTextAreaCustomer().getText());
        orderDB.setVK(orderComponent.getTextFieldVK().getText());
        LocalDate localDueDate = orderComponent.getDatePickerDueDate().getValue();
        orderDB.setDueDate(Date.valueOf(localDueDate));
        LocalDate localEventDate = orderComponent.getDatePickerEventDate().getValue();
        orderDB.setEventDate(Date.valueOf(localEventDate));
        orderDB.setDescription(orderComponent.getTextAreaDescription().getText());

        return orderDB;
    }

    private static OrderStructureComponentDB createOrderStructureComponentDB(OrderStructureComponentController orderStructureComponentController) {
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
