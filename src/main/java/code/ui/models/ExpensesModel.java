package code.ui.models;

import code.ui.ExpensesController;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;

/**
 * Created by ixotum on 16.10.15
 */
public class ExpensesModel {
    private final ExpensesController controller;

    public ExpensesModel(ExpensesController expensesController) {
        this.controller = expensesController;
    }

    public void initTable() {
        initSizeHandlers();
        initKeyHandlers();
    }

    private void initKeyHandlers() {
        TableView table = controller.getTable();
        table.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                controller.close();
            }
        });
    }

    private void initSizeHandlers() {
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
