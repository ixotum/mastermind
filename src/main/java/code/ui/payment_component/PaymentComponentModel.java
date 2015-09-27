package code.ui.payment_component;

import code.utils.UITools;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class PaymentComponentModel {
    private final PaymentComponentController controller;

    public PaymentComponentModel(PaymentComponentController paymentComponentController) {
        this.controller = paymentComponentController;
    }

    protected void addRow() {
        TableView<PaymentRowData> table = controller.getTable();
        String date = controller.getDatePicker().getValue().toString();
        PaymentRowData rowData = new PaymentRowData();
        rowData.setDate(date);
        table.getItems().add(rowData);
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
    }

    public void initDatePicker() {
        DatePicker datePicker = controller.getDatePicker();
        UITools.initDatePicker(datePicker);
        Date todayDate = new Date();
        LocalDate localDate = todayDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        datePicker.setValue(localDate);
    }
}
