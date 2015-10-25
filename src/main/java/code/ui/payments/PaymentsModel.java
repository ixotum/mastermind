package code.ui.payments;

import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;

/**
 * Created by ixotum on 25.10.15
 */
public class PaymentsModel {
    private final PaymentsController controller;

    public PaymentsModel(PaymentsController paymentsController) {
        this.controller = paymentsController;
    }

    public void initTable() {
        TableView table = controller.getTable();
        table.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                controller.close();
            }
        });
    }
}
