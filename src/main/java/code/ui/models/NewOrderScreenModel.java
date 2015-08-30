package code.ui.models;

import code.db.OrderDB;
import code.db.order_structure_component.OrderStructureComponentDB;
import code.ui.NewOrderScreenController;
import code.ui.OrderComponentController;
import code.ui.order_structure_component.OrderStructureComponentController;

import java.sql.Date;
import java.time.LocalDate;

public class NewOrderScreenModel {
    private final NewOrderScreenController controller;

    public NewOrderScreenModel(NewOrderScreenController newOrderScreenController) {
        this.controller = newOrderScreenController;
    }

    public OrderDB createOrderDB(int orderId) {
        OrderComponentController orderComponent = controller.getOrderComponent();
        OrderDB orderDB = new OrderDB(orderId);
        orderDB.setName(orderComponent.getTextFieldName().getText());

        OrderStructureComponentController orderStructureComponentController = orderComponent.getOrderStructureComponentController();
        OrderStructureComponentDB orderStructureComponentDB = OrderComponentController.createOrderStructureComponentDB(orderStructureComponentController);
        orderDB.setOrderStructureComponentDB(orderStructureComponentDB);

        orderDB.setCustomer(orderComponent.getTextAreaCustomer().getText());
        orderDB.setAddress(orderComponent.getTextAreaAddress().getText());
        orderDB.setVK(orderComponent.getTextFieldVK().getText());
        LocalDate localDueDate = orderComponent.getDatePickerDueDate().getValue();
        orderDB.setDueDate(Date.valueOf(localDueDate));
        LocalDate localEventDate = orderComponent.getDatePickerEventDate().getValue();
        orderDB.setEventDate(Date.valueOf(localEventDate));
        orderDB.setDescription(orderComponent.getTextAreaDescription().getText());
        orderDB.setNotes(orderComponent.getTextAreaNotes().getText());

        return orderDB;
    }
}
