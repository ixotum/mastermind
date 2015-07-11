package code.ui;

import code.Defines;
import code.db.OrderDB;
import code.db.OrdersJDBCTemplate;
import code.db.SettingsJDBCTemplate;
import code.utils.LoggerManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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

    public Label labelOrderId;
    public TextField textFieldName;

    @FXML
    public OrderComponentController orderComponent;

    public void onClickNewOrderDoneButton() {
        int orderId = Integer.parseInt(labelOrderId.getText());
        logger.info("Saving order with number: " + orderId);
        settingsJDBCTemplate.saveLastOrderId(orderId);

        OrdersJDBCTemplate ordersJDBCTemplate = (OrdersJDBCTemplate) applicationContext.getBean("ordersJDBCTemplateId");
        OrderDB orderDB = createOrderDB(orderId);
        ordersJDBCTemplate.saveNewOrder(orderDB);

        stage.hide();
    }

    private OrderDB createOrderDB(int orderId) {
        OrderDB orderDB = new OrderDB(orderId);
        orderDB.setName(textFieldName.getText());
        return orderDB;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        labelOrderId = orderComponent.getLabelOrderId();
        textFieldName = orderComponent.getTextFieldName();

        settingsJDBCTemplate = (SettingsJDBCTemplate) applicationContext.getBean("settingsJDBCTemplateId");
        int lastOrderId = settingsJDBCTemplate.readLastOrderId();
        ++lastOrderId;
        labelOrderId.setText(String.valueOf(lastOrderId));
    }

    @FXML
    public void onClickCancelButton() {
        logger.info("Cancel saving order");
        stage.hide();

    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
