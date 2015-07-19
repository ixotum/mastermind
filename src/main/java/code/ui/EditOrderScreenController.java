package code.ui;

import code.Defines;
import code.bus.BusEvent;
import code.bus.BusEventManager;
import code.db.OrderDB;
import code.db.OrdersJDBCTemplate;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

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
        orderComponent.getTextAreaCustomer().setText(orderDB.getCustomer());
        orderComponent.getTextFieldVK().setText(orderDB.getVk());
    }

    public void onClickCancelButton() {
        stage.hide();
    }

    public void onClickNewOrderDoneButton() {
        updateOrderDB();
        final ApplicationContext applicationContext = new ClassPathXmlApplicationContext(Defines.BEANS_CONFIG);
        OrdersJDBCTemplate ordersJDBCTemplate = (OrdersJDBCTemplate) applicationContext.getBean("ordersJDBCTemplateId");
        ordersJDBCTemplate.updateExistedOrder(orderDB);

        BusEventManager.dispatch(BusEvent.ORDER_UPDATED);
        stage.hide();
    }

    private void updateOrderDB() {
        orderDB.setName(orderComponent.getTextFieldName().getText());
        orderDB.setStructure(orderComponent.getTextAreaStructure().getText());
        orderDB.setCustomer(orderComponent.getTextAreaCustomer().getText());
        orderDB.setVK(orderComponent.getTextFieldVK().getText());
    }
}
