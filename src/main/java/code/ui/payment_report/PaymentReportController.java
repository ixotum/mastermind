package code.ui.payment_report;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

/**
 * Created by ixotum on 25.10.15
 */
public class PaymentReportController implements Initializable {
    @FXML
    private TableView<PaymentReportRowData> table;
    @FXML
    private TableColumn<PaymentReportRowData, String> columnDate;
    @FXML
    private TableColumn<PaymentReportRowData, String> columnId;
    @FXML
    private TableColumn<PaymentReportRowData, String> columnName;
    @FXML
    private TableColumn<PaymentReportRowData, String> columnPayment;

    private Stage stage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        PaymentReportModel model = new PaymentReportModel(this);
        model.initTable();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public TableView<PaymentReportRowData> getTable() {
        return table;
    }

    public void close() {
        stage.close();
    }

    public TableColumn<PaymentReportRowData, String> getColumnDate() {
        return columnDate;
    }

    public TableColumn<PaymentReportRowData, String> getColumnId() {
        return columnId;
    }

    public TableColumn<PaymentReportRowData, String> getColumnName() {
        return columnName;
    }

    public TableColumn<PaymentReportRowData, String> getColumnPayment() {
        return columnPayment;
    }
}
