package code.ui;

import code.Defines;
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
    final Logger logger = LoggerManager.getLoggerInstance();

    @FXML
    public Label labelId;

    public void onClickSaveOrderButton(ActionEvent actionEvent) {
        logger.info("Saving order number: ");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext(Defines.BEANS_CONFIG);
        SettingsJDBCTemplate settingsJDBCTemplate = (SettingsJDBCTemplate) applicationContext.getBean("settingsJDBCTemplateId");
        int lastOrderId = settingsJDBCTemplate.readLastOrderId();
        ++lastOrderId;
        labelId.setText(labelId.getText() + lastOrderId);
    }
}
