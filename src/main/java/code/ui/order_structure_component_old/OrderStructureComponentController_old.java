package code.ui.order_structure_component_old;

import code.db.order_structure_component.OrderStructureComponentDB_old;
import code.db.order_structure_component.OrderStructureComponentRowDB;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

@Deprecated
public class OrderStructureComponentController_old extends AnchorPane implements Initializable {
    @FXML
    public TableView<RowData_old> tableViewOrderStructure;
    @FXML
    public TableColumn columnButtonDelete;
    @FXML
    public TableColumn columnItem;
    @FXML
    public TableColumn columnPrice;

    public OrderStructureComponentController_old() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/order_structure_component_old.fxml"));
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
        initTableView();
    }

    private void initTableView() {
        Callback<TableColumn, TableCell> cellFactory = param -> new EditingCellFactory();

        columnButtonDelete.setCellValueFactory(new PropertyValueFactory<RowData_old, Button>("columnButtonDelete"));

        columnItem.setCellValueFactory(new PropertyValueFactory<RowData_old, String>("columnItem"));
        columnItem.setCellFactory(cellFactory);
        columnItem.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent>() {
            @Override
            public void handle(TableColumn.CellEditEvent event) {
                int currentRow = event.getTablePosition().getRow();
                ((RowData_old) event.getTableView().getItems().get(currentRow)).setColumnItem((String) event.getNewValue());
                int countRow = event.getTableView().getItems().size();

                if (currentRow == countRow - 1) {
                    addEmptyRow(tableViewOrderStructure, true);
                    recalculateTable(tableViewOrderStructure);
                }
            }
        });

        columnPrice.setCellValueFactory(new PropertyValueFactory<RowData_old, String>("columnPrice"));
        columnPrice.setCellFactory(cellFactory);
        columnPrice.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent>() {
            @Override
            public void handle(TableColumn.CellEditEvent event) {
                ((RowData_old) event.getTableView().getItems().get(event.getTablePosition().getRow())).setColumnPrice((String) event.getNewValue());
                recalculateTable(tableViewOrderStructure);
            }
        });

        tableViewOrderStructure.setEditable(true);
        ObservableList<RowData_old> observableList = FXCollections.observableArrayList();
        tableViewOrderStructure.setItems(observableList);
        addEmptyRow(tableViewOrderStructure, true);
    }

    private static void recalculateTable(TableView tableView) {
        int rowCount = tableView.getItems().size();
        BigDecimal totalPrice = BigDecimal.ZERO;

        for (int rowIndex = 0; rowIndex < rowCount - 1; ++rowIndex) {
            String priceString = ((RowData_old)tableView.getItems().get(rowIndex)).getColumnPrice();
            BigDecimal priceItem = BigDecimal.ZERO;

            if (priceString != null && !priceString.isEmpty()) {
                try {
                    priceItem = new BigDecimal(priceString);
                } catch (NumberFormatException e) {
                    System.out.println("Number format mismatch!");
                    priceItem = BigDecimal.ZERO;
                    priceString = priceString.replace("!", "");
                    ((RowData_old)tableView.getItems().get(rowIndex)).setColumnPrice("!" + priceString);
                }
            }

            totalPrice = totalPrice.add(priceItem);

            ((RowData_old)tableView.getItems().get(rowIndex)).setRowIndex(rowIndex);
            ((RowData_old)tableView.getItems().get(rowIndex)).getColumnButtonDelete().setVisible(true);
        }

        ((RowData_old) tableView.getItems().get(rowCount - 1)).setRowIndex(rowCount - 1);
        ((RowData_old) tableView.getItems().get(rowCount - 1)).getColumnButtonDelete().setVisible(false);
        ((RowData_old) tableView.getItems().get(rowCount - 1)).setColumnItem("total:");
        ((RowData_old) tableView.getItems().get(rowCount - 1)).setColumnPrice(totalPrice.toString());

        redrawTable(tableView);

        System.out.println("totalPrice = " + totalPrice);
    }

    private static void redrawTable(TableView tableView) {
        ((TableColumn) tableView.getColumns().get(0)).setVisible(false);
        ((TableColumn) tableView.getColumns().get(0)).setVisible(true);
    }

    private static void addEmptyRow(TableView<RowData_old> tableView, boolean zeroFlag) {
        int lastRowIndex = tableView.getItems().size() - 1;
        System.out.println("lastRowIndex = " + lastRowIndex);

        if (lastRowIndex != -1 && zeroFlag) {
            tableView.getItems().get(lastRowIndex).setColumnPrice("0");
        }

        int newRowIndex = lastRowIndex + 1;

        RowData_old rowDataOld = new RowData_old(newRowIndex, event -> deleteButtonHandler(tableView, event));
        ObservableList<RowData_old> observableList = tableView.getItems();
        observableList.add(rowDataOld);

        if (newRowIndex == 0) {
            tableView.getItems().get(newRowIndex).getColumnButtonDelete().setVisible(false);
        }
    }

    private static void deleteButtonHandler(TableView tableView, Event event) {
        int rowIndex = (int) event.getSource();
        System.out.println("handle delete event row index: " + rowIndex);
        removeRow(tableView, rowIndex);
    }

    private static void removeRow(TableView tableView, int rowIndex) {
        tableView.getItems().remove(rowIndex);
        recalculateTable(tableView);
        redrawTable(tableView);
    }

    public int getStructureSize() {
        return tableViewOrderStructure.getItems().size();
    }

    public RowData_old getRowData(int rowIndex) {
        return tableViewOrderStructure.getItems().get(rowIndex);
    }

    public void setOrderStructureComponentDB(OrderStructureComponentDB_old structureComponentDB) {
        tableViewOrderStructure.getItems().clear();

        if (structureComponentDB != null) {
            List<OrderStructureComponentRowDB> componentRowList = structureComponentDB.getComponentRowList();
            for (OrderStructureComponentRowDB componentRowDB : componentRowList) {
                addNewRow(componentRowDB, tableViewOrderStructure);
            }
        }

        addEmptyRow(tableViewOrderStructure, false);
        recalculateTable(tableViewOrderStructure);
    }

    private static void addNewRow(OrderStructureComponentRowDB componentRowDB, TableView<RowData_old> table) {
        int rowIndex = table.getItems().size();
        RowData_old rowDataOld = new RowData_old(rowIndex, event -> deleteButtonHandler(table, event));
        rowDataOld.setColumnItem(componentRowDB.getItem());
        rowDataOld.setColumnPrice(componentRowDB.getPrice().toString());

        ObservableList<RowData_old> observableList = table.getItems();
        observableList.add(rowDataOld);
    }
}
