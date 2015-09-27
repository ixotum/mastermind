package code.ui.payment_component;

import javafx.beans.property.SimpleStringProperty;

public class PaymentRowData {
    private final SimpleStringProperty date;
    private final SimpleStringProperty payment;

    public PaymentRowData() {
        date = new SimpleStringProperty();
        payment = new SimpleStringProperty();
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

    public void setPayment(String payment) {
        this.payment.setValue(payment);
    }

    public String getPayment() {
        return payment.get();
    }

    public SimpleStringProperty paymentProperty() {
        return payment;
    }
}
