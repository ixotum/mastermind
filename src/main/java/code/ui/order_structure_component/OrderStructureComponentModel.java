package code.ui.order_structure_component;

import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class OrderStructureComponentModel {
    private final OrderStructureComponentController controller;

    public OrderStructureComponentModel(OrderStructureComponentController controller) {
        this.controller = controller;
    }

    protected static void addRow(final TableView<RowData> table) {
        table.getItems().add(new RowData());
    }

    protected static void initTableHandlers(final TableView<RowData> table) {
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

    private static void selectNextCell(final TableView<RowData> table) {
        TablePosition<RowData, String> focusedPosition = table.getFocusModel().getFocusedCell();
        final int currentRowIndex = focusedPosition.getRow();
        final int currentColumnIndex = focusedPosition.getColumn();
        final int lastRowIndex = table.getItems().size() - 1;
        final int lastColumnIndex = table.getColumns().size() - 1;

        if (currentRowIndex == lastRowIndex && currentColumnIndex == lastColumnIndex) {
            OrderStructureComponentModel.addRow(table);
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

            double total = calcTotal(table.getItems());
            controller.getLabelTotal().setText(String.valueOf(total));
        }
    }

    private static double calcTotal(ObservableList<RowData> items) {
        return items.stream().mapToDouble(rowData -> Double.parseDouble(rowData.getColumnPrice())).sum();
    }

    protected void priceCommited(TableColumn.CellEditEvent<RowData, String> event) {
        String newValue = event.getNewValue();
        event.getTableView().getItems().get(event.getTablePosition().getRow()).setColumnPrice(newValue);
        double total = calcTotal(controller.getTable().getItems());
        controller.getLabelTotal().setText(String.valueOf(total));
    }
}
