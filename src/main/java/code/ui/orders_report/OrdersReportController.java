package code.ui.orders_report;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by ixotum on 01.11.15
 */
public class OrdersReportController implements Initializable {
    @FXML
    private TableView<OrdersReportRowData> table;

    private Stage stage;
    private OrdersReportModel model;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        model = new OrdersReportModel(this);

        model.initTable();
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
}
