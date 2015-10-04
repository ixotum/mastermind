package code.ui;

import code.Defines;
import code.bus.BusEvent;
import code.bus.BusEventType;
import code.bus.BusEventManager;
import code.db.OrderDB;
import code.db.OrdersJDBCTemplate;
import code.db.order_structure_component.OrderStructureComponentDB;
import code.db.payment_component.PaymentComponentDB;
import code.ui.models.OrderComponentModel;
import code.ui.order_structure_component.OrderStructureComponentController;
import code.ui.payment_component.PaymentComponentController;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.sql.Date;

/**
 * Created by ixotum on 7/12/15
 */
public class EditOrderScreenController {
    @FXML
    public OrderComponentController orderComponent;
    private Stage stage;
    private OrderDB orderDB;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void init(OrderDB orderDB) {
        this.orderDB = orderDB;
        orderComponent.getLabelOrderId().setText(String.valueOf(orderDB.getOrderId()));
        orderComponent.getTextFieldName().setText(orderDB.getName());
        orderComponent.initOrderStructureComponentController(orderDB.getOrderStructureComponentDB());
        orderComponent.getTextAreaCustomer().setText(orderDB.getCustomer());
        orderComponent.getTextAreaAddress().setText(orderDB.getAddress());
        orderComponent.getTextFieldVK().setText(orderDB.getVk());

        Date dueDate = orderDB.getDueDate();
        if (dueDate != null) {
            orderComponent.getDatePickerDueDate().setValue(dueDate.toLocalDate());
        }

        Date eventDate = orderDB.getEventDate();
        if (eventDate != null) {
            orderComponent.getDatePickerEventDate().setValue(eventDate.toLocalDate());
        }

        orderComponent.getTextAreaDescription().setText(orderDB.getDescription());
        orderComponent.getTextAreaNotes().setText(orderDB.getNotes());
        orderComponent.initPaymentComponentController(orderDB.getPaymentComponentDB());
    }

    public void onClickCancelButton() {
        stage.hide();
    }

    public void onClickEditOrderDoneButton() {
        updateOrderDB(orderComponent, orderDB);
        final ApplicationContext applicationContext = new ClassPathXmlApplicationContext(Defines.BEANS_CONFIG);
        OrdersJDBCTemplate ordersJDBCTemplate = (OrdersJDBCTemplate) applicationContext.getBean("ordersJDBCTemplateId");
        ordersJDBCTemplate.updateExistedOrder(orderDB);

        BusEvent busEvent = new BusEvent(BusEventType.ORDER_UPDATED, null);
        BusEventManager.dispatch(busEvent);
        stage.hide();
    }

    private static void updateOrderDB(OrderComponentController orderComponent, OrderDB orderDB) {
        orderDB.setName(orderComponent.getTextFieldName().getText());

        OrderStructureComponentController orderStructureComponentController = orderComponent.getOrderStructureComponentController();
        OrderStructureComponentDB orderStructureComponentDB = OrderComponentModel.createOrderStructureComponentDB(orderStructureComponentController);
        orderDB.setOrderStructureComponentDB(orderStructureComponentDB);

        orderDB.setCustomer(orderComponent.getTextAreaCustomer().getText());
        orderDB.setAddress(orderComponent.getTextAreaAddress().getText());
        orderDB.setVK(orderComponent.getTextFieldVK().getText());
        orderDB.setDueDate(Date.valueOf(orderComponent.getDatePickerDueDate().getValue()));
        orderDB.setEventDate(Date.valueOf(orderComponent.getDatePickerEventDate().getValue()));
        orderDB.setDescription(orderComponent.getTextAreaDescription().getText());
        orderDB.setNotes(orderComponent.getTextAreaNotes().getText());

        PaymentComponentController paymentComponentController = orderComponent.getPaymentComponentController();
        PaymentComponentDB paymentComponentDB = OrderComponentModel.createPaymentComponentDB(paymentComponentController);
        orderDB.setPaymentComponentDB(paymentComponentDB);
    }
}
