package code.ui.payment_component;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class PaymentComponentController extends AnchorPane implements Initializable {
    private final PaymentComponentModel model;

    @FXML
    private TableView<PaymentRowData> table;
    @FXML
    private TableColumn<PaymentRowData, String> columnDate;
    @FXML
    private TableColumn<PaymentRowData, String> columnPayment;
    @FXML
    private DatePicker datePicker;

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

    @FXML
    public void onButtonAdd() {
        model.addRow();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        model.initTable();
        model.initDatePicker();
    }

    public TableView<PaymentRowData> getTable() {
        return table;
    }

    public TableColumn<PaymentRowData, String> getColumnDate() {
        return columnDate;
    }

    public DatePicker getDatePicker() {
        return datePicker;
    }
}
