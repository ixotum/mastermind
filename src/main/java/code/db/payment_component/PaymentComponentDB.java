package code.db.payment_component;

import java.util.List;

/**
 * Created by ixotum on 02.10.15
 */
public class PaymentComponentDB {
    private List<PaymentDB> paymentDBList;

    public void setPaymentDBList(List<PaymentDB> paymentDBList) {
        this.paymentDBList = paymentDBList;
    }

    public int getPaymentsCount() {
        return paymentDBList.size();
    }

    public PaymentDB getPayment(int paymentIndex) {
        return paymentDBList.get(paymentIndex);
    }
}
