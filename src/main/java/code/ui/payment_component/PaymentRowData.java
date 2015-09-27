package code.ui.payment_component;

import javafx.beans.property.SimpleStringProperty;

public class PaymentRowData {
    private final SimpleStringProperty date;

    public PaymentRowData() {
        date = new SimpleStringProperty();
    }

    public String getDate() {
        return date.get();
    }

    public SimpleStringProperty dateProperty() {
        return date;
    }

    public void setDate(String value) {
        this.date.setValue(value);
    }
}
