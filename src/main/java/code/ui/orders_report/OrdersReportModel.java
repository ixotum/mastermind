package code.ui.orders_report;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.scene.control.DatePicker;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;

import code.Main;
import code.db.order.OrderDB;
import code.managers.OrderManager;
import code.utils.UITools;

/**
 * Created by ixotum on 01.11.15
 */
public class OrdersReportModel {
    private final OrdersReportController controller;

    public OrdersReportModel(OrdersReportController ordersReportController) {
        this.controller = ordersReportController;
    }

    public void initTable() {
        initTableKeyHandlers();
        initColumns();
        updateContent();
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
        controller.getTable().setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                controller.close();
            }
        });
    }
}
