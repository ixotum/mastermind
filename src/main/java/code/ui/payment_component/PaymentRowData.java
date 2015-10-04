package code.ui.payment_component;

import java.math.BigDecimal;
import java.time.LocalDate;

public class PaymentRowData {
    private LocalDate localDate;
    private BigDecimal payment = BigDecimal.ZERO;

    public String getDate() {
        return localDate.toString();
    }

    public void setDate(LocalDate localDate) {
        this.localDate = localDate;
    }

    public void setPayment(BigDecimal payment) {
        this.payment = payment;
    }

    public String getPayment() {
        return payment.toString();
    }

    public LocalDate getLocalDate() {
        return localDate;
    }

    public BigDecimal getPaymentData() {
        return payment;
    }
}
