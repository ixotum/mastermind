package code.ui;

import code.db.OrderDB;
import code.utils.LoggerManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * Created by ixotum on 7/8/15
 */
public class OrderCardController extends VBox {
    private final static Logger logger = LoggerManager.getLoggerInstance();

    @FXML
    public AnchorPane mainAnchor;
    @FXML
    public Label labelOrderId;
    @FXML
    public Label labelName;
    private OrderDB orderDB;
    private Stage parentStage;

    public OrderCardController() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/order_card.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onOrderCardClick() {
        logger.info("Load edit order screen. orderId: " + orderDB.getOrderId());
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/edit_order_screen.fxml"));
        try {
            Pane pane = fxmlLoader.load();
            Scene scene = new Scene(pane);
            Stage stage = new Stage();
            stage.resizableProperty().setValue(false);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(parentStage);
            stage.setScene(scene);
            EditOrderScreenController editOrderScreenController = fxmlLoader.getController();
            editOrderScreenController.setStage(stage);
            editOrderScreenController.init(orderDB);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public double getCardWidth() {
        return mainAnchor.getPrefWidth();
    }

    public double getCardHeight() {
        return mainAnchor.getPrefHeight();
    }

    public void init(OrderDB orderDB) {
        this.orderDB = orderDB;
        labelOrderId.setText(String.valueOf(orderDB.getOrderId()));
        labelName.setText(orderDB.getName());
    }

    public void setParentStage(Stage parentStage) {
        this.parentStage = parentStage;
    }
}
