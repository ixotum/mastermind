package code.ui;

import code.Defines;
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
    private AnchorPane mainAnchor;
    @FXML
    private Label labelOrderId;
    @FXML
    private Label labelName;
    @FXML
    private Label labelStatus;

    private OrderDB orderDB;
    private MainScreenController mainScreenController;

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
            stage.setTitle("Editing Order");
            stage.resizableProperty().setValue(false);
            stage.initModality(Modality.WINDOW_MODAL);
            Stage parentStage = mainScreenController.getParentStage();
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
        initStatus(orderDB);
    }

    private void initStatus(OrderDB orderDB) {
        labelStatus.setText(Defines.orderStatuses.get(orderDB.getStatus()));
        labelStatus.getStyleClass().add("labelStatus");
    }


    public OrderDB getOrderDB() {
        return orderDB;
    }

    public void setMainScreenController(MainScreenController controller) {
        this.mainScreenController = controller;
    }
}
