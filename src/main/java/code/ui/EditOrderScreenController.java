package code.ui;

import code.Defines;
import code.bus.BusEvent;
import code.bus.BusEventManager;
import code.db.OrderDB;
import code.db.OrdersJDBCTemplate;
import code.db.order_structure_component.OrderStructureComponentDB;
import code.ui.order_structure_component.OrderStructureComponentController;
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
        orderComponent.getTextAreaStructure().setText(orderDB.getStructure());
        orderComponent.getTextFieldPrice().setText(orderDB.getPrice());
        orderComponent.getTextAreaCustomer().setText(orderDB.getCustomer());
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
        orderComponent.initOrderStructureComponentController(orderDB.getOrderStructureComponentDB());
    }

    public void onClickCancelButton() {
        stage.hide();
    }

    public void onClickEditOrderDoneButton() {
        updateOrderDB(orderComponent, orderDB);
        final ApplicationContext applicationContext = new ClassPathXmlApplicationContext(Defines.BEANS_CONFIG);
        OrdersJDBCTemplate ordersJDBCTemplate = (OrdersJDBCTemplate) applicationContext.getBean("ordersJDBCTemplateId");
        ordersJDBCTemplate.updateExistedOrder(orderDB);

        BusEventManager.dispatch(BusEvent.ORDER_UPDATED);
        stage.hide();
    }

    private static void updateOrderDB(OrderComponentController orderComponent, OrderDB orderDB) {
        orderDB.setName(orderComponent.getTextFieldName().getText());
        orderDB.setStructure(orderComponent.getTextAreaStructure().getText());
        orderDB.setPrice(orderComponent.getTextFieldPrice().getText());

        OrderStructureComponentController orderStructureComponentController = orderComponent.getOrderStructureComponentController();
        OrderStructureComponentDB orderStructureComponentDB = OrderComponentController.createOrderStructureComponentDB(orderStructureComponentController);
        orderDB.setOrderStructureComponentDB(orderStructureComponentDB);

        orderDB.setCustomer(orderComponent.getTextAreaCustomer().getText());
        orderDB.setVK(orderComponent.getTextFieldVK().getText());
        orderDB.setDueDate(Date.valueOf(orderComponent.getDatePickerDueDate().getValue()));
        orderDB.setEventDate(Date.valueOf(orderComponent.getDatePickerEventDate().getValue()));
        orderDB.setDescription(orderComponent.getTextAreaDescription().getText());
    }
}
