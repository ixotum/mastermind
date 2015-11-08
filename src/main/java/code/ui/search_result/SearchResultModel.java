package code.ui.search_result;

import java.io.IOException;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import code.Main;
import code.db.order.OrderDB;
import code.managers.OrderManager;
import code.ui.EditOrderScreenController;
import code.ui.SearchResultController;

/**
 * Created by ixotum on 08.11.15
 */
public class SearchResultModel {
    private final SearchResultController controller;

    public SearchResultModel(SearchResultController controller) {
        this.controller = controller;
    }

    public void initTable() {
        initTableKeyHandlers();
        initColumns();
    }

    private void initTableKeyHandlers() {
        controller.getTable().setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                controller.close();
            }
        });

        controller.getTable().setOnMouseClicked(event -> {
            if (!event.getButton().equals(MouseButton.PRIMARY) ||
                    event.getClickCount() != 2) {
                return;
            }

            int row = getSelectedRow();
            int orderId = getSelectedOrder(row);
            showOrderScreen(orderId);
        });
    }

    private void showOrderScreen(int orderId) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/edit_order_screen.fxml"));
        try {
            Pane pane = fxmlLoader.load();
            Scene scene = new Scene(pane);
            Stage stage = new Stage();
            stage.setTitle("Editing Order");
            stage.resizableProperty().setValue(false);
            stage.initModality(Modality.WINDOW_MODAL);
            Stage parentStage = controller.getStage();
            stage.initOwner(parentStage);
            stage.setScene(scene);
            EditOrderScreenController editOrderScreenController = fxmlLoader.getController();
            editOrderScreenController.setStage(stage);
            OrderManager orderManager = Main.getOrderManager();
            OrderDB orderDB = orderManager.getOrder(orderId);
            editOrderScreenController.init(orderDB);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int getSelectedOrder(int row) {
        return controller.getTable().getItems().get(row).getIdNumeric();
    }

    private int getSelectedRow() {
        return controller.getTable().getSelectionModel().getSelectedCells().get(0).getRow();
    }

    private void initColumns() {
        controller.getColumnId().setCellValueFactory(new PropertyValueFactory<>("id"));
        controller.getColumnName().setCellValueFactory(new PropertyValueFactory<>("name"));
        controller.getColumnDueDate().setCellValueFactory(new PropertyValueFactory<>("dueDate"));
    }

    public void updateContent(List<OrderDB> orders) {
        ObservableList<SearchResultRowData> tableRows = FXCollections.observableArrayList();

        for (OrderDB orderDB : orders) {
            SearchResultRowData searchResultRowData = new SearchResultRowData();

            searchResultRowData.setId(orderDB.getOrderId());
            searchResultRowData.setName(orderDB.getName());
            searchResultRowData.setDueDate(orderDB.getDueDate());

            tableRows.add(searchResultRowData);
        }

        controller.getTable().getItems().setAll(tableRows);
    }
}
