package code.ui.orders_report;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

/**
 * Created by ixotum on 01.11.15
 */
public class OrdersReportController implements Initializable {
    @FXML
    private TableView<OrdersReportRowData> table;
    @FXML
    private TableColumn<OrdersReportRowData, String> columnId;
    @FXML
    private TableColumn<OrdersReportRowData, String> columnDueDate;
    @FXML
    private TableColumn<OrdersReportRowData, String> columnName;
    @FXML
    private TableColumn<OrdersReportRowData, String> columnTotal;
    @FXML
    private TableColumn<OrdersReportRowData, String> columnPaid;
    @FXML
    private TableColumn<OrdersReportRowData, String> columnDue;
    @FXML
    private DatePicker datePickerFrom;
    @FXML
    private DatePicker datePickerTo;

    private Stage stage;
    private OrdersReportModel model;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        model = new OrdersReportModel(this);

        model.initTable();
        model.initFilterBar();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public TableView<OrdersReportRowData> getTable() {
        return table;
    }

    public void close() {
        stage.close();
    }

    public TableColumn<OrdersReportRowData, String> getColumnId() {
        return columnId;
    }

    public TableColumn<OrdersReportRowData, String> getColumnDueDate() {
        return columnDueDate;
    }

    public TableColumn<OrdersReportRowData, String> getColumnName() {
        return columnName;
    }

    public TableColumn<OrdersReportRowData, String> getColumnTotal() {
        return columnTotal;
    }

    public TableColumn<OrdersReportRowData, String> getColumnPaid() {
        return columnPaid;
    }

    public TableColumn<OrdersReportRowData, String> getColumnDue() {
        return columnDue;
    }

    public DatePicker getDatePickerFrom() {
        return datePickerFrom;
    }

    public DatePicker getDatePickerTo() {
        return datePickerTo;
    }
}
