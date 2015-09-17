package code.ui.order_structure_component;

import code.db.order_structure_component.OrderStructureComponentDB;
import code.db.order_structure_component.OrderStructureComponentRowDB;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class OrderStructureComponentController extends AnchorPane implements Initializable {
    @FXML
    private TableView<RowData> table;
    @FXML
    private TableColumn<RowData, String> columnItem;
    @FXML
    private TableColumn<RowData, String> columnPrice;
    @FXML
    private Label labelTotal;

    private final OrderStructureComponentModel model;

    public OrderStructureComponentController() {
        model = new OrderStructureComponentModel(this);

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
        OrderStructureComponentModel.addEmptyRow(table);
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
        columnPrice.onEditCommitProperty().set(model::priceCommited);
    }

    private static Callback<TableColumn<RowData, String>, TableCell<RowData, String>> createStringCellFactory() {
        return TextFieldTableCell.forTableColumn();
    }

    @FXML
    public void onButtonDelete(ActionEvent actionEvent) {
        System.out.println("ComponentController.onButtonDelete");
        model.removeSelectedRow();
    }

    public TableView<RowData> getTable() {
        return table;
    }

    public Label getLabelTotal() {
        return labelTotal;
    }

    public int getStructureSize() {
        return table.getItems().size();
    }

    public RowData getRowData(int rowIndex) {
        assert rowIndex < table.getItems().size();
        return table.getItems().get(rowIndex);
    }

    public void setOrderStructureComponentDB(OrderStructureComponentDB orderStructureComponentDB) {
        model.initComponent(orderStructureComponentDB);
    }
}
