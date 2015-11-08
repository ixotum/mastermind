package code.ui;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.logging.Logger;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import jfxtras.scene.control.LocalDatePicker;

import code.bus.BusEvent;
import code.bus.BusEventType;
import code.bus.BusEventListener;
import code.bus.BusEventManager;
import code.utils.LoggerManager;
import code.ui.models.MainScreenModel;
import code.ui.expenses.ExpensesController;
import code.ui.payment_report.PaymentReportController;
import code.ui.orders_report.OrdersReportController;

public class MainScreenController implements Initializable, BusEventListener {
    private final static Logger logger = LoggerManager.getLoggerInstance();

    @FXML
    private TextField textFieldSearch;
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
        model.initCalendar();
        model.initGridOrders();
        BusEventManager.addListener(this, BusEventType.ORDER_UPDATED);
    }

    @FXML
    public void onClickNewOrder() {
        logger.info("Creating new order");
        showNewOrderScreen();
    }

    @FXML
    public void onClickResetDate() {
        calendar.setLocalDate(null);
    }

    @FXML
    public void onClickExpenses() {
        showExpensesScreen();
    }

    @FXML
    public void onClickPayments() {
        showPaymentsScreen();
    }

    @FXML
    public void onClickOrders() {
        showOrdersScreen();
    }

    @FXML
    public void onSearchButton() {
        model.search();
    }

    private void showOrdersScreen() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/orders_report_screen.fxml"));
        try {
            Pane pane = fxmlLoader.load();
            Scene scene = new Scene(pane);
            Stage stage = new Stage();
            stage.setTitle("Orders");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(parentStage);
            stage.setScene(scene);
            OrdersReportController ordersReportController = fxmlLoader.getController();
            ordersReportController.setStage(stage);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showPaymentsScreen() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/payments_report_screen.fxml"));
        try {
            Pane pane = fxmlLoader.load();
            Scene scene = new Scene(pane);
            Stage stage = new Stage();
            stage.setTitle("Payments");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(parentStage);
            stage.setScene(scene);
            PaymentReportController paymentReportController = fxmlLoader.getController();
            paymentReportController.setStage(stage);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showExpensesScreen() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/expenses/expenses_screen.fxml"));
        try {
            Pane pane = fxmlLoader.load();
            Scene scene = new Scene(pane);
            Stage stage = new Stage();
            stage.setTitle("Expenses");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(parentStage);
            stage.setScene(scene);
            ExpensesController expensesController = fxmlLoader.getController();
            expensesController.setStage(stage);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showNewOrderScreen() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/new_order_screen.fxml"));
        try {
            Pane pane = fxmlLoader.load();
            Scene scene = new Scene(pane);
            Stage stage = new Stage();
            stage.setTitle("Creating New Order");
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
    public void busEventDispatched(BusEvent busEvent) {
        if (busEvent.getType() == BusEventType.ORDER_UPDATED) {
            model.redrawGridOrders();
            MainScreenModel.redrawHighLights(calendar);
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

    public LocalDatePicker getCalendar() {
        return calendar;
    }

    public TextField getTextFieldSearch() {
        return textFieldSearch;
    }
}
