package code.ui.orders_report;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;

import code.Main;
import code.db.order.OrderDB;
import code.managers.OrderManager;

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

    private void initColumns() {
        controller.getColumnId().setCellValueFactory(new PropertyValueFactory<>("id"));
        controller.getColumnName().setCellValueFactory(new PropertyValueFactory<>("name"));
        controller.getColumnTotal().setCellValueFactory(new PropertyValueFactory<>("total"));
        controller.getColumnPaid().setCellValueFactory(new PropertyValueFactory<>("paid"));
        controller.getColumnDue().setCellValueFactory(new PropertyValueFactory<>("due"));
    }

    private void updateContent() {
        OrderManager orderManager = Main.getOrderManager();
        List<OrderDB> orders = orderManager.getOrders();
        List<OrdersReportRowData> tableRows = new ArrayList<>();

        for (OrderDB orderDB : orders) {
            OrdersReportRowData ordersReportRowData = new OrdersReportRowData();

            ordersReportRowData.setId(orderDB.getOrderId());
            ordersReportRowData.setName(orderDB.getName());
            BigDecimal total = orderDB.getTotal();
            ordersReportRowData.setTotal(total);
            BigDecimal paid = orderDB.getPaid();
            ordersReportRowData.setPaid(paid);
            BigDecimal due = total.subtract(paid);
            ordersReportRowData.setDue(due);

            tableRows.add(ordersReportRowData);
        }

        controller.getTable().setItems(FXCollections.observableArrayList(tableRows));
    }

    private void initTableKeyHandlers() {
        controller.getTable().setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                controller.close();
            }
        });
    }
}
