package code.ui.models;

import code.db.OrderDB;
import code.ui.NewOrderScreenController;
import code.ui.OrderComponentController;

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
        orderDB.setStructure(orderComponent.getTextAreaStructure().getText());
        orderDB.setPrice(orderComponent.getTextFieldPrice().getText());
        orderDB.setCustomer(orderComponent.getTextAreaCustomer().getText());
        orderDB.setVK(orderComponent.getTextFieldVK().getText());
        LocalDate localDueDate = orderComponent.getDatePickerDueDate().getValue();
        orderDB.setDueDate(Date.valueOf(localDueDate));
        LocalDate localEventDate = orderComponent.getDatePickerEventDate().getValue();
        orderDB.setEventDate(Date.valueOf(localEventDate));
        orderDB.setDescription(orderComponent.getTextAreaDescription().getText());

        return orderDB;
    }
}
