package code.ui.order_structure_component;

import code.bus.BusEvent;
import code.bus.BusEventType;
import code.bus.BusEventManager;
import code.db.order.order_structure_component.OrderStructureComponentDB;
import code.db.order.order_structure_component.OrderStructureComponentRowDB;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.math.BigDecimal;
import java.util.List;

public class OrderStructureComponentModel {
    private final OrderStructureComponentController controller;

    public OrderStructureComponentModel(OrderStructureComponentController controller) {
        this.controller = controller;
    }

    protected static void addEmptyRow(final TableView<RowData> table) {
        table.getItems().add(new RowData());
    }

    protected void initTableHandlers(final TableView<RowData> table) {
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
            KeyCode keyCode = event.getCode();
            if (keyCode == KeyCode.ENTER) {
                selectNextCell(table);
            }
            else if (keyCode == KeyCode.ESCAPE) {
                BusEvent busEvent = new BusEvent(BusEventType.ESC_PRESSED, null);
                BusEventManager.dispatch(busEvent);
            }
        });
    }

    private static void selectNextCell(final TableView<RowData> table) {
        TablePosition<RowData, String> focusedPosition = table.getFocusModel().getFocusedCell();
        final int currentRowIndex = focusedPosition.getRow();
        final int currentColumnIndex = focusedPosition.getColumn();
        final int lastRowIndex = table.getItems().size() - 1;
        final int lastColumnIndex = table.getColumns().size() - 1;

        if (currentRowIndex == lastRowIndex && currentColumnIndex == lastColumnIndex) {
            OrderStructureComponentModel.addEmptyRow(table);
        }

        final int nextRowIndex = calcNextRowIndex(currentRowIndex, currentColumnIndex, lastColumnIndex);
        final int nextColumnIndex = calcNextColumnIndex(currentColumnIndex, lastColumnIndex);
        selectNextCell(table, nextRowIndex, nextColumnIndex);
    }

    private static void selectNextCell(final TableView<RowData> table, final int rowIndex, final int columnIndex) {
        TableColumn<RowData, String> column = (TableColumn<RowData, String>) table.getColumns().get(columnIndex);
        table.getSelectionModel().clearAndSelect(rowIndex, column);
        table.scrollTo(rowIndex);
    }

    private static int calcNextColumnIndex(final int currentColumnIndex, final int lastColumnIndex) {
        return currentColumnIndex == lastColumnIndex ? 0 : currentColumnIndex + 1;
    }

    private static int calcNextRowIndex(final int currentRowIndex, final int currentColumnIndex, final int lastColumnIndex) {
        return currentColumnIndex == lastColumnIndex ? currentRowIndex + 1 : currentRowIndex;
    }

    protected void removeSelectedRow() {
        TableView<RowData> table = controller.getTable();

        if (table.getItems().size() != 1) {
            table.getItems().remove(table.getSelectionModel().getSelectedItem());
            table.getSelectionModel().clearSelection();

            updateTotal();
        }
    }

    private static BigDecimal calcTotal(ObservableList<RowData> items) {
        double total = items.stream().mapToDouble(rowData -> Double.parseDouble(rowData.getColumnPrice())).sum();
        return new BigDecimal(total).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    protected void priceCommitted(TableColumn.CellEditEvent<RowData, String> event) {
        String newValue = event.getNewValue();
        event.getTableView().getItems().get(event.getTablePosition().getRow()).setColumnPrice(newValue);
        updateTotal();
    }

    private void updateTotal() {
        BigDecimal total = calcTotal(controller.getTable().getItems());
        controller.getLabelTotal().setText(total.toString());
        BusEvent busEvent = new BusEvent(BusEventType.TOTAL_UPDATED, total);
        BusEventManager.dispatch(busEvent);
    }

    public void initComponent(OrderStructureComponentDB orderStructureComponentDB) {
        TableView<RowData> table = controller.getTable();
        table.getItems().clear();

        if (orderStructureComponentDB != null) {
            List<OrderStructureComponentRowDB> componentRowList = orderStructureComponentDB.getComponentRowList();
            for (OrderStructureComponentRowDB componentRowDB : componentRowList) {
                addRow(componentRowDB, table);
            }
        }

        updateTotal();
    }

    private static void addRow(OrderStructureComponentRowDB componentRowDB, TableView<RowData> table) {
        RowData rowData = new RowData();
        rowData.setColumnItem(componentRowDB.getItem());
        rowData.setColumnPrice(componentRowDB.getPrice().toString());
        table.getItems().add(rowData);
    }
}
