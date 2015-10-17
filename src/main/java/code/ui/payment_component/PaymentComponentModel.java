package code.ui.payment_component;

import code.bus.BusEvent;
import code.bus.BusEventType;
import code.bus.BusEventListener;
import code.bus.BusEventManager;
import code.db.order.payment_component.PaymentComponentDB;
import code.db.order.payment_component.PaymentDB;
import code.utils.UITools;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

public class PaymentComponentModel implements BusEventListener {
    private final PaymentComponentController controller;

    public PaymentComponentModel(PaymentComponentController paymentComponentController) {
        this.controller = paymentComponentController;
    }

    protected void addRow() {
        TableView<PaymentRowData> table = controller.getTable();
        LocalDate date = controller.getDatePicker().getValue();
        PaymentRowData rowData = new PaymentRowData();
        rowData.setDate(date);

        TextField textFieldPayment = controller.getTextFieldPayment();
        BigDecimal payment = new BigDecimal(textFieldPayment.getText());
        rowData.setPayment(payment);
        table.getItems().add(rowData);

        textFieldPayment.clear();

        updatePaymentBar();
    }

    private void addRow(PaymentDB paymentDB) {
        PaymentRowData paymentRowData = new PaymentRowData();
        paymentRowData.setDate(paymentDB.getDate().toLocalDate());
        paymentRowData.setPayment(paymentDB.getPayment());
        TableView<PaymentRowData> table = controller.getTable();
        table.getItems().add(paymentRowData);

        updatePaymentBar();
    }

    private void updatePaymentBar() {
        TableView<PaymentRowData> table = controller.getTable();
        ObservableList<PaymentRowData> observableList = table.getItems();
        BigDecimal paid = calcPaid(observableList);
        controller.getLabelPaid().setText(paid.toString());

        BigDecimal total = new BigDecimal(controller.getLabelTotal().getText());
        BigDecimal due = total.subtract(paid);
        controller.getLabelDue().setText(due.toString());

        String status = getStatus(total, due);
        controller.getLabelStatus().setText(status);
    }

    private static String getStatus(BigDecimal total, BigDecimal due) {
        String status = "UNPAID";

        if (due.compareTo(total) < 0 && due.compareTo(BigDecimal.ZERO) > 0) {
            status = "PARTIALLY PAID";
        } else if (due.compareTo(BigDecimal.ZERO) == 0) {
            status = "PAID";
        } else if (due.compareTo(BigDecimal.ZERO) < 0) {
            status = "OVERPAID";
        }

        return status;
    }

    private static BigDecimal calcPaid(ObservableList<PaymentRowData> observableList) {
        return observableList.stream().map(paymentRowData -> new BigDecimal(paymentRowData.getPayment())).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    protected void initTable() {
        initTableHandlers();
        ObservableList<PaymentRowData> observableList = FXCollections.observableArrayList();
        TableView<PaymentRowData> table = controller.getTable();
        table.setItems(observableList);

        initColumns();
    }

    private void initTableHandlers() {
        TableView<PaymentRowData> table = controller.getTable();
        table.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                BusEventManager.dispatch(new BusEvent(BusEventType.ESC_PRESSED, null));
            }
        });
    }

    private void initColumns() {
        TableColumn<PaymentRowData, String> columnDate = controller.getColumnDate();
        columnDate.setCellValueFactory(new PropertyValueFactory<>("date"));

        TableColumn<PaymentRowData, String> columnPayment = controller.getColumnPayment();
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
    }

    public void removeSelectedRow() {
        TableView<PaymentRowData> table = controller.getTable();
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
