package code.ui.order_structure_component;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class OrderStructureComponentController extends AnchorPane implements Initializable {
    @FXML
    private TableView<RowData> table;
    @FXML
    private TableColumn<RowData, String> columnItem;
    @FXML
    private TableColumn<RowData, String> columnPrice;

    public OrderStructureComponentController() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/order_structure_component.fxml"));
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
        initTable();
        OrderStructureComponentModel.addRow(table);
    }

    private void initTable() {
        initColumns();

        ObservableList<RowData> observableList = FXCollections.observableArrayList();
        table.setItems(observableList);
        table.getSelectionModel().setCellSelectionEnabled(true);
        table.getSelectionModel().selectFirst();

        OrderStructureComponentModel.initTableHandlers(table);
    }

    private void initColumns() {
        columnItem.setCellValueFactory(cellData -> cellData.getValue().columnItemProperty());
        columnItem.setCellFactory(createStringCellFactory());

        columnPrice.setCellValueFactory(cellData -> cellData.getValue().columnPriceProperty());
        columnPrice.setCellFactory(createStringCellFactory());
        columnPrice.onEditCommitProperty().set(this::priceCommited);
    }

    private void priceCommited(TableColumn.CellEditEvent<RowData, String> event) {
        String newValue = event.getNewValue();
        event.getTableView().getItems().get(event.getTablePosition().getRow()).setColumnPrice(newValue);
        double total = calcTotal(table.getItems());
    }

    private static double calcTotal(ObservableList<RowData> items) {
        return items.stream().mapToDouble(rowData -> Double.parseDouble(rowData.getColumnPrice())).sum();
    }

    private static Callback<TableColumn<RowData, String>, TableCell<RowData, String>> createStringCellFactory() {
        return TextFieldTableCell.forTableColumn();
    }
}
