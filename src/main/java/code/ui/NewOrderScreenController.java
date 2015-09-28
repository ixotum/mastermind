package code.ui;

import code.Defines;
import code.bus.BusEvent;
import code.bus.BusEventType;
import code.bus.BusEventManager;
import code.db.OrderDB;
import code.db.OrdersJDBCTemplate;
import code.db.SettingsJDBCTemplate;
import code.ui.models.NewOrderScreenModel;
import code.utils.LoggerManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Logger;

/**
 * Created by ixotum on 7/5/15
 */
public class NewOrderScreenController implements Initializable {
    private final Logger logger = LoggerManager.getLoggerInstance();
    private final ApplicationContext applicationContext = new ClassPathXmlApplicationContext(Defines.BEANS_CONFIG);

    private SettingsJDBCTemplate settingsJDBCTemplate;
    private Stage stage;
    private NewOrderScreenModel model;

    @FXML
    private OrderComponentController orderComponent;

    public NewOrderScreenController() {
        model = new NewOrderScreenModel(this);
    }

    public void onClickNewOrderDoneButton() {
        int orderId = Integer.parseInt(orderComponent.getLabelOrderId().getText());
        logger.info("Saving order with number: " + orderId);
        settingsJDBCTemplate.saveLastOrderId(orderId);//TODO db operations move to model

        OrdersJDBCTemplate ordersJDBCTemplate = (OrdersJDBCTemplate) applicationContext.getBean("ordersJDBCTemplateId");
        OrderDB orderDB = model.createOrderDB(orderId);
        ordersJDBCTemplate.saveNewOrder(orderDB);

        BusEvent busEvent = new BusEvent(BusEventType.ORDER_UPDATED, null);
        BusEventManager.dispatch(busEvent);
        stage.hide();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        settingsJDBCTemplate = (SettingsJDBCTemplate) applicationContext.getBean("settingsJDBCTemplateId");
        int lastOrderId = settingsJDBCTemplate.readLastOrderId();
        ++lastOrderId;
        orderComponent.getLabelOrderId().setText(String.valueOf(lastOrderId));
    }

    @FXML
    public void onClickCancelButton() {
        logger.info("Cancel saving order");
        stage.hide();

    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public OrderComponentController getOrderComponent() {
        return orderComponent;
    }
}
