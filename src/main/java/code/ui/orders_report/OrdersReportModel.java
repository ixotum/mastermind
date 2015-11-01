package code.ui.orders_report;


import javafx.scene.input.KeyCode;

/**
 * Created by ixotum on 01.11.15
 */
public class OrdersReportModel {
    private final OrdersReportController controller;

    public OrdersReportModel(OrdersReportController ordersReportController) {
        this.controller = ordersReportController;
    }

    public void initTable() {
        initTableKeyHandlers();
    }

    private void initTableKeyHandlers() {
        controller.getTable().setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                controller.close();
            }
        });
    }
}
