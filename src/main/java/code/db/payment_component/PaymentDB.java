package code.db.payment_component;

import java.math.BigDecimal;
import java.sql.Date;

/**
 * Created by ixotum on 02.10.15
 */
public class PaymentDB {
    private int orderId;
    private Date date;
    private BigDecimal payment;

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setPayment(BigDecimal payment) {
        this.payment = payment;
    }

    public Date getDate() {
        return date;
    }

    public BigDecimal getPayment() {
        return payment;
    }
}
