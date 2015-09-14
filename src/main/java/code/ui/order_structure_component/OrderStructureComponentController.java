package code.ui.order_structure_component;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class OrderStructureComponentController extends AnchorPane implements Initializable {
    @FXML
    public TableView<RowData> table;
    @FXML
    public TableColumn<RowData, String> columnItem;
    @FXML
    public TableColumn<RowData, String> columnPrice;

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
    }

    private void initTable() {
        initColumns();

        ObservableList<RowData> observableList = FXCollections.observableArrayList();
        table.setItems(observableList);
        table.getSelectionModel().setCellSelectionEnabled(true);
        table.getSelectionModel().selectFirst();

        initTableHandlers(table);
        addRow(table);
    }

    private static void initTableHandlers(final TableView<RowData> table) {
        table.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (table.getEditingCell() == null) {
                KeyCode keyCode = event.getCode();

                if (keyCode.isLetterKey() || keyCode.isDigitKey()) {
                    TablePosition<RowData, String> focusedPosition = table.getFocusModel().getFocusedCell();
                    table.edit(focusedPosition.getRow(), focusedPosition.getTableColumn());
                }
            }
        });

        table.addEventFilter(KeyEvent.KEY_RELEASED, event -> {
            if (event.getCode() == KeyCode.ENTER) {
                selectNextCell(table);
            }
        });
    }

    private static void selectNextCell(TableView<RowData> table) {
        TablePosition<RowData, String> focusedPosition = table.getFocusModel().getFocusedCell();
        int currentRowIndex = focusedPosition.getRow();
        int currentColumnIndex = focusedPosition.getColumn();
        int lastRowIndex = table.getItems().size() - 1;
        int lastColumnIndex = table.getColumns().size() - 1;

        if (currentRowIndex == lastRowIndex && currentColumnIndex == lastColumnIndex) {
            addRow(table);
        }

        int nextRowIndex = calcNextRowIndex(currentRowIndex, currentColumnIndex, lastColumnIndex);
        int nextColumnIndex = calcNextColumnIndex(currentColumnIndex, lastColumnIndex);
        selectNextCell(table, nextRowIndex, nextColumnIndex);
    }

    private static void selectNextCell(TableView<RowData> table, int rowIndex, int columnIndex) {
        TableColumn<RowData, String> column = (TableColumn<RowData, String>) table.getColumns().get(columnIndex);
        table.getSelectionModel().clearAndSelect(rowIndex, column);
        table.scrollTo(rowIndex);
    }

    private static int calcNextColumnIndex(int currentColumnIndex, int lastColumnIndex) {
        return currentColumnIndex == lastColumnIndex ? 0 : currentColumnIndex + 1;
    }

    private static int calcNextRowIndex(int currentRowIndex, int currentColumnIndex, int lastColumnIndex) {
        return currentColumnIndex == lastColumnIndex ? currentRowIndex + 1 : currentRowIndex;
    }

    private static void addRow(TableView<RowData> table) {
        table.getItems().add(new RowData());
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
