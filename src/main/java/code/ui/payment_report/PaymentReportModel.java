package code.ui.payment_report;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;

import code.Main;
import code.db.order.OrderDB;
import code.db.order.payment_component.PaymentComponentDB;
import code.db.order.payment_component.PaymentDB;
import code.managers.OrderManager;
import code.utils.UITools;

/**
 * Created by ixotum on 25.10.15
 */
public class PaymentReportModel {
    private final PaymentReportController controller;
    private final List<String> periods = Arrays.asList("Month", "Year", "All Time");

    public PaymentReportModel(PaymentReportController paymentReportController) {
        this.controller = paymentReportController;
    }

    public void initTable() {
        initTableKeyHandler();
        initColumns();
        updateContent();
    }

    public void initFilterBar() {
        initRadio();
        initComboPeriod();
        initDatePickers();
    }

    private void initDatePickers() {
        DatePicker datePickerFrom = controller.getDatePickerFrom();
        UITools.initDatePicker(datePickerFrom);
        datePickerFrom.setValue(LocalDate.now().withDayOfMonth(1));
        datePickerFrom.valueProperty().addListener(observable -> updateContent());
        initDatePickerKeyHandler(datePickerFrom);

        DatePicker datePickerTo = controller.getDatePickerTo();
        UITools.initDatePicker(datePickerTo);
        datePickerTo.setValue(LocalDate.now());
        datePickerTo.valueProperty().addListener(observable -> updateContent());
        initDatePickerKeyHandler(datePickerTo);
    }

    private void initComboPeriod() {
        ComboBox<String> comboPeriod = controller.getComboPeriod();
        comboPeriod.setItems(FXCollections.observableArrayList(periods));
        comboPeriod.setValue(periods.get(0));
        comboPeriod.valueProperty().addListener(observable -> updateContent());
        comboPeriod.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                controller.close();
            }
        });
    }

    private void initRadio() {
        controller.getComboPeriod().setDisable(false);
        controller.getAnchorDates().setDisable(true);

        RadioButton radioPeriod = controller.getRadioPeriod();
        radioPeriod.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                controller.getComboPeriod().setDisable(false);
                controller.getAnchorDates().setDisable(true);
            } else {
                controller.getComboPeriod().setDisable(true);
                controller.getAnchorDates().setDisable(false);
            }

            updateContent();
        });

        initRadioButtonKeyHandler(radioPeriod);
        initRadioButtonKeyHandler(controller.getRadioDate());
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
            if (paymentComponentDB == null) {
                continue;
            }

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

        PaymentReportFilter paymentReportFilter = createFilter();
        List<PaymentReportRowData> filteredPaymentReportRows = paymentReportFilter.filter(paymentReportRows);

        controller.getTable().setItems(FXCollections.observableArrayList(filteredPaymentReportRows));

        applySortOrder();
        updateLabelAllPayments();
    }

    private void updateLabelAllPayments() {
        ObservableList<PaymentReportRowData> payments = controller.getTable().getItems();
        BigDecimal allPayments = payments.stream().map(paymentRowData -> new BigDecimal(paymentRowData.getPayment())).reduce(BigDecimal.ZERO, BigDecimal::add);
        controller.getLabelAllPayments().setText(allPayments.toString());
    }

    private PaymentReportFilter createFilter() {
        PaymentReportFilter paymentReportFilter = new PaymentReportFilter();

        if (controller.getRadioPeriod().isSelected()) {
            String period = controller.getComboPeriod().getValue();
            paymentReportFilter.initPeriod(period);
        } else {
            LocalDate localDateFrom = controller.getDatePickerFrom().getValue();
            LocalDate localDateTo = controller.getDatePickerTo().getValue();
            paymentReportFilter.setDates(localDateFrom, localDateTo);
        }

        return paymentReportFilter;
    }

    private void applySortOrder() {
        TableView<PaymentReportRowData> tableView = controller.getTable();
        TableColumn<PaymentReportRowData, String> columnDate = controller.getColumnDate();
        columnDate.setSortType(TableColumn.SortType.DESCENDING);
        tableView.getSortOrder().add(columnDate);
    }

    private void initTableKeyHandler() {
        controller.getTable().setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                controller.close();
            }
        });
    }

    private void initDatePickerKeyHandler(DatePicker datePicker) {
        datePicker.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                controller.close();
            }
        });
    }

    private void initRadioButtonKeyHandler(RadioButton radioPeriod) {
        radioPeriod.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                controller.close();
            }
        });
    }
}
