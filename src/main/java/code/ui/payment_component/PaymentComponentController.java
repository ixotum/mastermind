package code.ui.payment_component;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import code.db.order.payment_component.PaymentComponentDB;

public class PaymentComponentController extends AnchorPane implements Initializable {
    private final PaymentComponentModel model;

    @FXML
    private TableView<PaymentComponentRowData> table;
    @FXML
    private TableColumn<PaymentComponentRowData, String> columnDate;
    @FXML
    private TableColumn<PaymentComponentRowData, String> columnPayment;
    @FXML
    private DatePicker datePicker;
    @FXML
    private TextField textFieldPayment;
    @FXML
    private Button buttonAdd;
    @FXML
    private Label labelTotal;
    @FXML
    private Label labelPaid;
    @FXML
    private Label labelDue;
    @FXML
    private Label status;

    public PaymentComponentController() {
        model = new PaymentComponentModel(this);

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/payment_component.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        model.initTable();
        model.initDatePicker();
        model.initPaymentFieldHandlers();
        model.initListeners();
    }

    @FXML
    public void onButtonAdd() {
        model.addRow();
    }

    @FXML
    public void onButtonDelete() {
        model.removeSelectedRow();
    }

    public TableView<PaymentComponentRowData> getTable() {
        return table;
    }

    public TableColumn<PaymentComponentRowData, String> getColumnDate() {
        return columnDate;
    }

    public DatePicker getDatePicker() {
        return datePicker;
    }

    public TextField getTextFieldPayment() {
        return textFieldPayment;
    }

    public TableColumn<PaymentComponentRowData, String> getColumnPayment() {
        return columnPayment;
    }

    public Button getButtonAdd() {
        return buttonAdd;
    }

    public Label getLabelTotal() {
        return labelTotal;
    }

    public Label getLabelPaid() {
        return labelPaid;
    }

    public Label getLabelDue() {
        return labelDue;
    }

    public Label getLabelStatus() {
        return status;
    }

    public int getPaymentsSize() {
        return table.getItems().size();
    }

    public PaymentComponentRowData getPayment(int paymentIndex) {
        return table.getItems().get(paymentIndex);
    }

    public void setPaymentComponentDB(PaymentComponentDB paymentComponentDB) {
        model.initComponent(paymentComponentDB);
    }
}
