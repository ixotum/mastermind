package code.ui;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;

import code.ui.models.NewOrderScreenModel;
import code.utils.LoggerManager;

/**
 * Created by ixotum on 7/5/15
 */
public class NewOrderScreenController implements Initializable{
    private final Logger logger = LoggerManager.getLoggerInstance();
    private Stage stage;
    private NewOrderScreenModel model;

    @FXML
    private OrderComponentController orderComponent;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        model.initListener();
        model.initOrderId();
        initComboStatus();
    }

    @FXML
    public void onClickNewOrderDoneButton() {
        model.saveOrderComponent();
        hide();
    }

    @FXML
    public void onClickCancelButton() {
        logger.info("Cancel saving order");
        hide();

    }

    public NewOrderScreenController() {
        model = new NewOrderScreenModel(this);
    }

    private void initComboStatus() {
        orderComponent.setOrderStatus(0);
    }

    public void setStage(Stage stage) {
        this.stage = stage;
        orderComponent.setStage(stage);
    }

    public OrderComponentController getOrderComponent() {
        return orderComponent;
    }

    public void hide() {
        stage.hide();
    }
}
