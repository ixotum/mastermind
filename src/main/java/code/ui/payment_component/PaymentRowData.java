package code.ui.payment_component;

import javafx.scene.control.DatePicker;
import javafx.scene.layout.AnchorPane;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class PaymentRowData {
    private LocalDate date = null;

    public AnchorPane getDate() {
        AnchorPane anchorPane = new AnchorPane();
        DatePicker datePicker = new DatePicker();

        if (date == null) {
            Date todayDate = new Date();
            date = todayDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        }

        datePicker.setValue(date);
        datePicker.setOnAction(event -> dateChanged(datePicker.getValue()));
        anchorPane.getChildren().add(datePicker);
        return anchorPane;
    }

    private void dateChanged(LocalDate newDate) {
        this.date = newDate;
    }
}
