package code.ui;

import code.ui.models.NewOrderScreenModel;
import code.utils.LoggerManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Logger;

/**
 * Created by ixotum on 7/5/15
 */
public class NewOrderScreenController implements Initializable{
    private final Logger logger = LoggerManager.getLoggerInstance();
    private Stage stage;
    private NewOrderScreenModel model;

    @FXML
    private OrderComponentController orderComponent;

    public NewOrderScreenController() {
        model = new NewOrderScreenModel(this);
    }

    public void onClickNewOrderDoneButton() {
        model.saveOrderComponent();
        hide();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        model.initListener();
        model.initOrderId();
        initComboStatus();
    }

    private void initComboStatus() {
        orderComponent.setOrderStatus(0);
    }

    @FXML
    public void onClickCancelButton() {
        logger.info("Cancel saving order");
        hide();

    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public OrderComponentController getOrderComponent() {
        return orderComponent;
    }

    public void hide() {
        stage.hide();
    }
}
