package code.managers;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import code.Main;
import code.db.order.OrderDB;
import code.db.order.payment_component.PaymentComponentDB;
import code.db.order.payment_component.PaymentDB;
import code.utils.UITools;

/**
 * Created by ixotum on 08.11.15
 */
public class IncomeManager {
    public static BigDecimal getIncome(int year, int month) {
        List<OrderDB> orders = Main.getOrderManager().getOrders();
        List<PaymentDB> monthPayments = new ArrayList<>();

        for (OrderDB orderDB : orders) {
            PaymentComponentDB paymentComponentDB = orderDB.getPaymentComponentDB();
            if (paymentComponentDB == null) {
                continue;
            }

            List<PaymentDB> payments = paymentComponentDB.getPaymentDBList();
            monthPayments.addAll(payments.stream().filter(payment -> isInMonth(payment, year, month)).collect(Collectors.toList()));
        }

        return monthPayments.stream().map(PaymentDB::getPayment).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private static boolean isInMonth(PaymentDB payment, int year, int month) {
        Date date = payment.getDate();
        LocalDate localDate = UITools.convertToLocalDate(date);

        return localDate.getYear() == year && localDate.getMonthValue() == month;
    }

    public static List<Integer> findIncomeYears() {
        List<Integer> incomeYears = new ArrayList<>();
        List<OrderDB> orders = Main.getOrderManager().getOrders();

        for (OrderDB order : orders) {
            PaymentComponentDB paymentComponentDB = order.getPaymentComponentDB();
            if (paymentComponentDB == null) {
                continue;
            }

            List<PaymentDB> payments = paymentComponentDB.getPaymentDBList();
            payments.stream().forEach(payment -> updateIncomeYears(payment.getDate().toLocalDate().getYear(), incomeYears));
        }

        return incomeYears;
    }

    private static void updateIncomeYears(Integer year, List<Integer> incomeYears) {
        for (Integer existedYear: incomeYears) {
            if (Objects.equals(existedYear, year)) {
                return;
            }
        }

        incomeYears.add(year);
    }
}
