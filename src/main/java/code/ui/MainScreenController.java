package code.ui;

import code.bus.BusEvent;
import code.bus.BusEventType;
import code.bus.BusEventListener;
import code.bus.BusEventManager;
import code.ui.models.MainScreenModel;
import code.utils.LoggerManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import jfxtras.scene.control.LocalDatePicker;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.logging.Logger;

public class MainScreenController implements Initializable, BusEventListener {
    private final static Logger logger = LoggerManager.getLoggerInstance();

    @FXML
    private LocalDatePicker calendar;
    @FXML
    public GridPane gridOrders;
    @FXML
    public ScrollPane gridOrdersScrollPane;

    private Stage parentStage;
    private final MainScreenModel model;

    public MainScreenController() {
        model = new MainScreenModel(this);
    }

    public void initialize(URL location, ResourceBundle resources) {
        MainScreenModel.checkForUpdates();
        MainScreenModel.initCalendar(calendar);
        model.initGridOrders();
        BusEventManager.addListener(this, BusEventType.ORDER_UPDATED);
    }

    @FXML
    public void onClickNewOrder() {
        logger.info("Creating new order");
        createNewOrderScreen();
    }

    private void createNewOrderScreen() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/new_order_screen.fxml"));
        try {
            Pane pane = fxmlLoader.load();
            Scene scene = new Scene(pane);
            Stage stage = new Stage();
            stage.resizableProperty().setValue(Boolean.FALSE);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(parentStage);
            stage.setScene(scene);
            NewOrderScreenController newOrderScreenController = fxmlLoader.getController();
            newOrderScreenController.setStage(stage);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setParentStage(Stage stage) {
        parentStage = stage;
    }

    @Override
    public void busEventDispatch(BusEvent busEvent) {
        if (busEvent.getType() == BusEventType.ORDER_UPDATED) {
            model.redrawGridOrders();
        }
    }

    public Stage getParentStage() {
        return parentStage;
    }

    public GridPane getGridPane() {
        return gridOrders;
    }

    public ScrollPane getScrollPane() {
        return gridOrdersScrollPane;
    }
}
