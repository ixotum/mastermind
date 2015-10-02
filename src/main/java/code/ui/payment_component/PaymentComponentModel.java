package code.ui.payment_component;

import code.bus.BusEvent;
import code.bus.BusEventType;
import code.bus.BusEventListener;
import code.bus.BusEventManager;
import code.utils.UITools;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class PaymentComponentModel implements BusEventListener {
    private final PaymentComponentController controller;

    public PaymentComponentModel(PaymentComponentController paymentComponentController) {
        this.controller = paymentComponentController;
    }

    protected void addRow() {
        TableView<PaymentRowData> table = controller.getTable();
        String date = controller.getDatePicker().getValue().toString();
        PaymentRowData rowData = new PaymentRowData();
        rowData.setDate(date);

        TextField textFieldPayment = controller.getTextFieldPayment();
        String payment = textFieldPayment.getText();
        rowData.setPayment(payment);
        table.getItems().add(rowData);

        textFieldPayment.clear();

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

        String status = getStatus(total, paid, due);
        controller.getLabelStatus().setText(status);
    }

    private static String getStatus(BigDecimal total, BigDecimal paid, BigDecimal due) {
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
        ObservableList<PaymentRowData> observableList = FXCollections.observableArrayList();
        TableView<PaymentRowData> table = controller.getTable();
        table.setItems(observableList);

        initColumns();
    }

    private void initColumns() {
        TableColumn<PaymentRowData, String> columnDate = controller.getColumnDate();
        columnDate.setCellValueFactory(cell -> cell.getValue().dateProperty());

        TableColumn<PaymentRowData, String> columnPayment = controller.getColumnPayment();
        columnPayment.setCellValueFactory(cell -> cell.getValue().paymentProperty());
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
    public void busEventDispatch(BusEvent busEvent) {
        BusEventType busEventType = busEvent.getType();
        if (busEventType != BusEventType.TOTAL_UPDATED) {
            return;
        }

        BigDecimal total = (BigDecimal) busEvent.getContent();
        controller.getLabelTotal().setText(total.toString());
        updatePaymentBar();
    }
}
