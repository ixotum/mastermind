package code.ui.models;

import code.ui.ExpensesController;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableView;

/**
 * Created by ixotum on 16.10.15
 */
public class ExpensesModel {
    private final ExpensesController controller;

    public ExpensesModel(ExpensesController expensesController) {
        this.controller = expensesController;
    }

    public void initTable() {
        ScrollPane scrollPane = controller.getScrollPane();
        scrollPane.heightProperty().addListener((observable, oldValue, newValue) -> scrollPaneHeightChanged(newValue));
        scrollPane.widthProperty().addListener((observable, oldValue, newValue) -> scrollPaneWidthChanged(newValue));
    }

    private void scrollPaneWidthChanged(Number newValue) {
        TableView table = controller.getTable();
        table.setPrefWidth(newValue.doubleValue());
    }

    private void scrollPaneHeightChanged(Number newValue) {
        TableView table = controller.getTable();
        table.setPrefHeight(newValue.doubleValue() - 2);
    }
}
