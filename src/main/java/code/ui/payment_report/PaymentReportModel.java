package code.ui.payment_report;

import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;

import code.Main;
import code.db.order.OrderDB;
import code.db.order.payment_component.PaymentComponentDB;
import code.db.order.payment_component.PaymentDB;
import code.managers.OrderManager;

/**
 * Created by ixotum on 25.10.15
 */
public class PaymentReportModel {
    private final PaymentReportController controller;

    public PaymentReportModel(PaymentReportController paymentReportController) {
        this.controller = paymentReportController;
    }

    public void initTable() {
        initKeyHandler();
        initColumns();
        updateContent();
    }

    private void initColumns() {
        controller.getColumnDate().setCellValueFactory(new PropertyValueFactory<>("date"));
        controller.getColumnId().setCellValueFactory(new PropertyValueFactory<>("id"));
        controller.getColumnName().setCellValueFactory(new PropertyValueFactory<>("name"));
        controller.getColumnPayment().setCellValueFactory(new PropertyValueFactory<>("payment"));
    }

    private void updateContent() {
        OrderManager orderManager = Main.getOrderManager();
        List<OrderDB> orders = orderManager.getOrders();
        List<PaymentReportRowData> paymentReportRows = new ArrayList<>();

        for (OrderDB orderDB : orders) {
            PaymentComponentDB paymentComponentDB = orderDB.getPaymentComponentDB();
            List<PaymentDB> paymentDBList = paymentComponentDB.getPaymentDBList();

            for (PaymentDB paymentDB : paymentDBList) {
                PaymentReportRowData paymentReportRowData = new PaymentReportRowData();
                paymentReportRowData.setDate(paymentDB.getDate());
                paymentReportRowData.setId(orderDB.getOrderId());
                paymentReportRowData.setName(orderDB.getName());
                paymentReportRowData.setPayment(paymentDB.getPayment());

                paymentReportRows.add(paymentReportRowData);
            }
        }

        controller.getTable().setItems(FXCollections.observableArrayList(paymentReportRows));

        applySortOrder();
    }

    private void applySortOrder() {
        TableView<PaymentReportRowData> tableView = controller.getTable();
        TableColumn<PaymentReportRowData, String> columnDate = controller.getColumnDate();
        columnDate.setSortType(TableColumn.SortType.DESCENDING);
        tableView.getSortOrder().add(columnDate);
    }

    private void initKeyHandler() {
        controller.getTable().setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                controller.close();
            }
        });
    }
}
