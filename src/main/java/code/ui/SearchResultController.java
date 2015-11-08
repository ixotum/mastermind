package code.ui;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import code.db.order.OrderDB;
import code.ui.search_result.SearchResultModel;
import code.ui.search_result.SearchResultRowData;

/**
 * Created by ixotum on 08.11.15
 */
public class SearchResultController implements Initializable {
    @FXML
    private TableView<SearchResultRowData> table;
    @FXML
    private TableColumn<SearchResultRowData, String> columnId;
    @FXML
    private TableColumn<SearchResultRowData, String> columnName;
    @FXML
    private TableColumn<SearchResultRowData, String> columnDueDate;

    private Stage stage;
    private SearchResultModel model;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        model = new SearchResultModel(this);
        model.initTable();
    }

    public void setFoundOrders(List<OrderDB> foundOrders) {
        model.updateContent(foundOrders);
    }

    public TableView<SearchResultRowData> getTable() {
        return table;
    }

    public TableColumn<SearchResultRowData, String> getColumnId() {
        return columnId;
    }

    public TableColumn<SearchResultRowData, String> getColumnName() {
        return columnName;
    }

    public TableColumn<SearchResultRowData, String> getColumnDueDate() {
        return columnDueDate;
    }

    public void close() {
        stage.close();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public Stage getStage() {
        return stage;
    }
}
