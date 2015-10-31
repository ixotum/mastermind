package code.ui.models;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import code.Defines;
import code.db.order.order_structure_component.OrderStructureComponentDB;
import code.db.order.order_structure_component.OrderStructureComponentRowDB;
import code.db.order.payment_component.PaymentComponentDB;
import code.db.order.payment_component.PaymentDB;
import code.ui.order_structure_component.OrderStructureComponentController;
import code.ui.order_structure_component.RowData;
import code.ui.payment_component.PaymentComponentController;
import code.ui.payment_component.PaymentComponentRowData;

public class OrderComponentModel {
    public static OrderStructureComponentDB createOrderStructureComponentDB(OrderStructureComponentController orderStructureComponentController) {
        final int structureSize = orderStructureComponentController.getStructureSize();
        List<OrderStructureComponentRowDB> orderStructureComponentRowDBList = new ArrayList<>();

        for (int rowIndex = 0; rowIndex < structureSize; ++rowIndex) {
            RowData rowData = orderStructureComponentController.getRowData(rowIndex);
            OrderStructureComponentRowDB orderStructureComponentRowDB = createOrderStructureComponentRowDB(rowData);
            orderStructureComponentRowDBList.add(orderStructureComponentRowDB);
        }

        OrderStructureComponentDB orderStructureComponentDB = new OrderStructureComponentDB();
        orderStructureComponentDB.setComponentRowList(orderStructureComponentRowDBList);
        return orderStructureComponentDB;
    }

    private static OrderStructureComponentRowDB createOrderStructureComponentRowDB(RowData rowData) {
        OrderStructureComponentRowDB orderStructureComponentRowDB = new OrderStructureComponentRowDB();
        orderStructureComponentRowDB.setItem(rowData.getColumnItem());
        String priceString = rowData.getColumnPrice();
        BigDecimal price;

        try {
            price = new BigDecimal(priceString);
        } catch (NumberFormatException e) {
            price = BigDecimal.ZERO;
        }

        orderStructureComponentRowDB.setPrice(price);

        return orderStructureComponentRowDB;
    }

    public static PaymentComponentDB createPaymentComponentDB(PaymentComponentController paymentComponentController) {
        final int paymentsSize = paymentComponentController.getPaymentsSize();
        List<PaymentDB> paymentDBList = new ArrayList<>();

        for (int paymentIndex = 0; paymentIndex < paymentsSize; ++paymentIndex) {
            PaymentComponentRowData paymentComponentRowData = paymentComponentController.getPayment(paymentIndex);
            PaymentDB paymentDB = createPaymentDB(paymentComponentRowData);
            paymentDBList.add(paymentDB);
        }

        PaymentComponentDB paymentComponentDB = new PaymentComponentDB();
        paymentComponentDB.setPaymentDBList(paymentDBList);
        return paymentComponentDB;
    }

    private static PaymentDB createPaymentDB(PaymentComponentRowData paymentComponentRowData) {
        PaymentDB paymentDB = new PaymentDB();
        LocalDate localDate = paymentComponentRowData.getLocalDate();
        Date date = Date.valueOf(localDate);
        paymentDB.setDate(date);
        paymentDB.setPayment(paymentComponentRowData.getPaymentData());

        return paymentDB;
    }

    public static int getOrderStatus(String value) {
        return Defines.orderStatuses.indexOf(value);
    }
}
