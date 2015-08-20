package code.ui.order_structure_component;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import java.util.ResourceBundle;

public class OrderStructureComponent extends AnchorPane implements Initializable {
    @FXML
    public TableView tableViewOrderStructure;
    @FXML
    public TableColumn columnButtonDelete;
    @FXML
    public TableColumn columnItem;
    @FXML
    public TableColumn columnPrice;

    public OrderStructureComponent() {
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
        initTableView();
    }

    private void initTableView() {
        Callback<TableColumn, TableCell> cellFactory = param -> new EditingCellFactory();

        columnButtonDelete.setCellValueFactory(new PropertyValueFactory<RowData, Button>("columnButtonDelete"));

        columnItem.setCellValueFactory(new PropertyValueFactory<RowData, String>("columnItem"));
        columnItem.setCellFactory(cellFactory);
        columnItem.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent>() {
            @Override
            public void handle(TableColumn.CellEditEvent event) {
                int currentRow = event.getTablePosition().getRow();
                ((RowData) event.getTableView().getItems().get(currentRow)).setColumnItem((String) event.getNewValue());
                int countRow = event.getTableView().getItems().size();

                if (currentRow == countRow - 1) {
                    addNewRow(tableViewOrderStructure);
                    recalculateTable(tableViewOrderStructure);
                }
            }
        });

        columnPrice.setCellValueFactory(new PropertyValueFactory<RowData, String>("columnPrice"));
        columnPrice.setCellFactory(cellFactory);
        columnPrice.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent>() {
            @Override
            public void handle(TableColumn.CellEditEvent event) {
                ((RowData) event.getTableView().getItems().get(event.getTablePosition().getRow())).setColumnPrice((String) event.getNewValue());
                recalculateTable(tableViewOrderStructure);
            }
        });

        tableViewOrderStructure.setEditable(true);
        ObservableList<RowData> observableList = FXCollections.observableArrayList();
        tableViewOrderStructure.setItems(observableList);
        addNewRow(tableViewOrderStructure);
    }

    private static void recalculateTable(TableView tableView) {
        int rowCount = tableView.getItems().size();
        BigDecimal totalPrice = BigDecimal.ZERO;

        for (int rowIndex = 0; rowIndex < rowCount - 1; ++rowIndex) {
            String priceString = ((RowData)tableView.getItems().get(rowIndex)).getColumnPrice();
            BigDecimal priceItem = BigDecimal.ZERO;

            if (priceString != null && !priceString.isEmpty()) {
                try {
                    priceItem = new BigDecimal(priceString);
                } catch (NumberFormatException e) {
                    System.out.println("Number format mismatch!");
                    priceItem = BigDecimal.ZERO;
                    priceString = priceString.replace("!", "");
                    ((RowData)tableView.getItems().get(rowIndex)).setColumnPrice("!" + priceString);
                }
            }

            totalPrice = totalPrice.add(priceItem);

            ((RowData)tableView.getItems().get(rowIndex)).setRowIndex(rowIndex);
            ((RowData)tableView.getItems().get(rowIndex)).getColumnButtonDelete().setVisible(true);
        }

        ((RowData) tableView.getItems().get(rowCount - 1)).setRowIndex(rowCount - 1);
        ((RowData) tableView.getItems().get(rowCount - 1)).getColumnButtonDelete().setVisible(false);
        ((RowData) tableView.getItems().get(rowCount - 1)).setColumnItem("total:");
        ((RowData) tableView.getItems().get(rowCount - 1)).setColumnPrice(totalPrice.toString());

        redrawTable(tableView);

        System.out.println("totalPrice = " + totalPrice);
    }

    private static void redrawTable(TableView tableView) {
        ((TableColumn) tableView.getColumns().get(0)).setVisible(false);
        ((TableColumn) tableView.getColumns().get(0)).setVisible(true);
    }

    private static void addNewRow(TableView tableView) {
        int lastRowIndex = tableView.getItems().size() - 1;
        System.out.println("lastRowIndex = " + lastRowIndex);

        if (lastRowIndex != -1) {
            ((RowData) tableView.getItems().get(lastRowIndex)).setColumnPrice("0");
        }

        int newRowIndex = lastRowIndex + 1;

        RowData rowData = new RowData(newRowIndex, event -> {
            int rowIndex = (int) event.getSource();
            System.out.println("handle delete event row index: " + rowIndex);
            removeRow(tableView, rowIndex);
        });
        ObservableList<RowData> observableList = tableView.getItems();
        observableList.add(rowData);

        if (newRowIndex == 0) {
            ((RowData) tableView.getItems().get(newRowIndex)).getColumnButtonDelete().setVisible(false);
        }
    }

    private static void removeRow(TableView tableView, int rowIndex) {
        tableView.getItems().remove(rowIndex);
        recalculateTable(tableView);
        redrawTable(tableView);
    }
}
