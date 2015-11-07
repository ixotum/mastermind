package code.ui.orders_report;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import code.Main;
import code.db.order.OrderDB;
import code.managers.OrderManager;
import code.utils.UITools;
import code.bus.BusEvent;
import code.bus.BusEventListener;
import code.bus.BusEventManager;
import code.bus.BusEventType;
import code.ui.EditOrderScreenController;

/**
 * Created by ixotum on 01.11.15
 */
public class OrdersReportModel implements BusEventListener {
    private final OrdersReportController controller;

    public OrdersReportModel(OrdersReportController ordersReportController) {
        this.controller = ordersReportController;
    }

    public void initTable() {
        initTableKeyHandlers();
        initColumns();
        updateContent();
        initBusEventListener();
    }

    private void initBusEventListener() {
        BusEventManager.addListener(this, BusEventType.ORDER_UPDATED);
    }

    public void initFilterBar() {
        initDatePickers();
    }

    private void initDatePickers() {
        DatePicker datePickerFrom = controller.getDatePickerFrom();
        UITools.initDatePicker(datePickerFrom);
        datePickerFrom.setValue(LocalDate.now().withDayOfMonth(1));
        datePickerFrom.valueProperty().addListener(observable -> updateContent());

        DatePicker datePickerTo = controller.getDatePickerTo();
        UITools.initDatePicker(datePickerTo);
        datePickerTo.setValue(LocalDate.now());
        datePickerTo.valueProperty().addListener(observable -> updateContent());
    }

    private void initColumns() {
        controller.getColumnId().setCellValueFactory(new PropertyValueFactory<>("id"));
        controller.getColumnDueDate().setCellValueFactory(new PropertyValueFactory<>("dueDate"));
        controller.getColumnName().setCellValueFactory(new PropertyValueFactory<>("name"));
        controller.getColumnTotal().setCellValueFactory(new PropertyValueFactory<>("total"));
        controller.getColumnPaid().setCellValueFactory(new PropertyValueFactory<>("paid"));
        controller.getColumnDue().setCellValueFactory(new PropertyValueFactory<>("due"));
        controller.getColumnPaymentStatus().setCellValueFactory(new PropertyValueFactory<>("paymentStatus"));
    }

    private void updateContent() {
        OrderManager orderManager = Main.getOrderManager();
        List<OrderDB> orders = orderManager.getOrders();
        List<OrdersReportRowData> tableRows = new ArrayList<>();

        for (OrderDB orderDB : orders) {
            OrdersReportRowData ordersReportRowData = new OrdersReportRowData();

            ordersReportRowData.setId(orderDB.getOrderId());
            ordersReportRowData.setDueDate(orderDB.getDueDate());
            ordersReportRowData.setName(orderDB.getName());
            BigDecimal total = orderDB.getTotal();
            ordersReportRowData.setTotal(total);
            BigDecimal paid = orderDB.getPaid();
            ordersReportRowData.setPaid(paid);
            BigDecimal due = total.subtract(paid);
            ordersReportRowData.setDue(due);
            String paymentStatus = UITools.getPaymentStatus(total, due);
            ordersReportRowData.setPaymentStatus(paymentStatus);

            tableRows.add(ordersReportRowData);
        }

        OrdersReportFilter ordersReportFilter = createOrdersReportFilter();
        List<OrdersReportRowData> filteredTableRows = ordersReportFilter.filter(tableRows);

        controller.getTable().setItems(FXCollections.observableArrayList(filteredTableRows));
    }

    private OrdersReportFilter createOrdersReportFilter() {
        OrdersReportFilter ordersReportFilter = new OrdersReportFilter();

        LocalDate showFrom = controller.getDatePickerFrom().getValue();
        LocalDate showTo = controller.getDatePickerTo().getValue();
        ordersReportFilter.initDates(showFrom, showTo);

        return ordersReportFilter;
    }

    private void initTableKeyHandlers() {
        TableView<OrdersReportRowData> table = controller.getTable();
        table.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                controller.close();
            }
        });

        table.setOnMouseClicked(this::tableOnMouseClicked);
    }

    private void tableOnMouseClicked(MouseEvent mouseEvent) {
        if (!mouseEvent.getButton().equals(MouseButton.PRIMARY) ||
                mouseEvent.getClickCount() != 2) {
            return;
        }

        int row = getSelectedRow();
        int orderId = getSelectedOrder(row);
        showOrderScreen(orderId);
    }

    private void showOrderScreen(int orderId) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/edit_order_screen.fxml"));
        try {
            Pane pane = fxmlLoader.load();
            Scene scene = new Scene(pane);
            Stage stage = new Stage();
            stage.setTitle("Editing Order");
            stage.resizableProperty().setValue(false);
            stage.initModality(Modality.WINDOW_MODAL);
            Stage parentStage = controller.getStage();
            stage.initOwner(parentStage);
            stage.setScene(scene);
            EditOrderScreenController editOrderScreenController = fxmlLoader.getController();
            editOrderScreenController.setStage(stage);
            OrderManager orderManager = Main.getOrderManager();
            OrderDB orderDB = orderManager.getOrder(orderId);
            editOrderScreenController.init(orderDB);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int getSelectedOrder(int row) {
        return controller.getTable().getItems().get(row).getIdNumeric();
    }

    private int getSelectedRow() {
        return controller.getTable().getSelectionModel().getSelectedCells().get(0).getRow();
    }

    @Override
    public void busEventDispatched(BusEvent busEvent) {
        if (busEvent.getType() == BusEventType.ORDER_UPDATED) {
            updateContent();
        }
    }
}
