package code.ui;

import code.Defines;
import code.db.OrderDB;
import code.db.OrdersJDBCTemplate;
import code.db.SettingsJDBCTemplate;
import code.utils.LoggerManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Logger;

/**
 * Created by ixotum on 7/5/15.
 */
public class NewOrderScreenController implements Initializable {
    private final Logger logger = LoggerManager.getLoggerInstance();
    private final ApplicationContext applicationContext = new ClassPathXmlApplicationContext(Defines.BEANS_CONFIG);

    private SettingsJDBCTemplate settingsJDBCTemplate;

    @FXML
    public Label labelOrderId;

    public void onClickSaveOrderButton(ActionEvent actionEvent) {
        int orderId = Integer.parseInt(labelOrderId.getText());
        logger.info("Saving order with number: " + orderId);
        settingsJDBCTemplate.saveLastOrderId(orderId);

        OrdersJDBCTemplate ordersJDBCTemplate = (OrdersJDBCTemplate) applicationContext.getBean("ordersJDBCTemplateId");
        OrderDB orderDB = new OrderDB(orderId);
        ordersJDBCTemplate.saveNewOrder(orderDB);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        settingsJDBCTemplate = (SettingsJDBCTemplate) applicationContext.getBean("settingsJDBCTemplateId");
        int lastOrderId = settingsJDBCTemplate.readLastOrderId();
        ++lastOrderId;
        labelOrderId.setText(String.valueOf(lastOrderId));
    }
}
