package code.db.order.payment_component;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by ixotum on 02.10.15
 */
public class PaymentComponentDB {
    private List<PaymentDB> paymentDBList;

    public void setPaymentDBList(List<PaymentDB> paymentDBList) {
        this.paymentDBList = paymentDBList;
    }

    public List<PaymentDB> getPaymentDBList() {
        return paymentDBList;
    }

    public int getPaymentsCount() {
        return paymentDBList.size();
    }

    public PaymentDB getPayment(int paymentIndex) {
        return paymentDBList.get(paymentIndex);
    }

    public int getOrderId() {
        return paymentDBList.get(0).getOrderId();
    }

    public BigDecimal getPaid() {
        return paymentDBList.stream().map(PaymentDB::getPayment).reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
