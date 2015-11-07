package code.ui.payment_component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;

import code.bus.BusEvent;
import code.bus.BusEventType;
import code.bus.BusEventListener;
import code.bus.BusEventManager;
import code.db.order.payment_component.PaymentComponentDB;
import code.db.order.payment_component.PaymentDB;
import code.utils.UITools;

public class PaymentComponentModel implements BusEventListener {
    private final PaymentComponentController controller;

    public PaymentComponentModel(PaymentComponentController paymentComponentController) {
        this.controller = paymentComponentController;
    }

    protected void addRow() {
        TableView<PaymentComponentRowData> table = controller.getTable();
        LocalDate date = controller.getDatePicker().getValue();
        PaymentComponentRowData rowData = new PaymentComponentRowData();
        rowData.setDate(date);

        TextField textFieldPayment = controller.getTextFieldPayment();
        BigDecimal payment = new BigDecimal(textFieldPayment.getText());
        rowData.setPayment(payment);
        table.getItems().add(rowData);

        textFieldPayment.clear();

        updatePaymentBar();
    }

    private void addRow(PaymentDB paymentDB) {
        PaymentComponentRowData paymentComponentRowData = new PaymentComponentRowData();
        paymentComponentRowData.setDate(paymentDB.getDate().toLocalDate());
        paymentComponentRowData.setPayment(paymentDB.getPayment());
        TableView<PaymentComponentRowData> table = controller.getTable();
        table.getItems().add(paymentComponentRowData);

        updatePaymentBar();
    }

    private void updatePaymentBar() {
        TableView<PaymentComponentRowData> table = controller.getTable();
        ObservableList<PaymentComponentRowData> observableList = table.getItems();
        BigDecimal paid = calcPaid(observableList);
        controller.getLabelPaid().setText(paid.toString());

        BigDecimal total = new BigDecimal(controller.getLabelTotal().getText());
        BigDecimal due = total.subtract(paid);
        controller.getLabelDue().setText(due.toString());

        String status = UITools.getPaymentStatus(total, due);
        controller.getLabelStatus().setText(status);
    }

    private static BigDecimal calcPaid(ObservableList<PaymentComponentRowData> observableList) {
        return observableList.stream().map(paymentRowData -> new BigDecimal(paymentRowData.getPayment())).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    protected void initTable() {
        initTableHandlers();
        ObservableList<PaymentComponentRowData> observableList = FXCollections.observableArrayList();
        TableView<PaymentComponentRowData> table = controller.getTable();
        table.setItems(observableList);

        initColumns();
    }

    private void initTableHandlers() {
        TableView<PaymentComponentRowData> table = controller.getTable();
        table.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                BusEventManager.dispatch(new BusEvent(BusEventType.ESC_PRESSED, null));
            }
        });
    }

    private void initColumns() {
        TableColumn<PaymentComponentRowData, String> columnDate = controller.getColumnDate();
        columnDate.setCellValueFactory(new PropertyValueFactory<>("date"));

        TableColumn<PaymentComponentRowData, String> columnPayment = controller.getColumnPayment();
        columnPayment.setCellValueFactory(new PropertyValueFactory<>("payment"));
    }

    public void initDatePicker() {
        DatePicker datePicker = controller.getDatePicker();
        UITools.initDatePicker(datePicker);
        Date todayDate = new Date();
        LocalDate localDate = todayDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        datePicker.setValue(localDate);
    }

    public void initPaymentFieldHandlers() {
        TextField textFieldPayment = controller.getTextFieldPayment();
        Button buttonAdd = controller.getButtonAdd();
        textFieldPayment.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!textFieldPayment.getText().isEmpty()) {
                buttonAdd.setDisable(false);
                return;
            }

            buttonAdd.setDisable(true);
        });

        textFieldPayment.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER && !textFieldPayment.getText().isEmpty()) {
                addRow();
            }
        });
    }

    public void removeSelectedRow() {
        TableView<PaymentComponentRowData> table = controller.getTable();
        table.getItems().remove(table.getSelectionModel().getSelectedItem());

        updatePaymentBar();
    }

    public void initListeners() {
        BusEventManager.addListener(this, BusEventType.TOTAL_UPDATED);
    }

    @Override
    public void busEventDispatched(BusEvent busEvent) {
        BusEventType busEventType = busEvent.getType();
        if (busEventType != BusEventType.TOTAL_UPDATED) {
            return;
        }

        BigDecimal total = (BigDecimal) busEvent.getContent();
        controller.getLabelTotal().setText(total.toString());
        updatePaymentBar();
    }

    public void initComponent(PaymentComponentDB paymentComponentDB) {
        if (paymentComponentDB == null) {
            return;
        }

        List<PaymentDB> paymentDBList = paymentComponentDB.getPaymentDBList();
        paymentDBList.forEach(this::addRow);
    }
}
