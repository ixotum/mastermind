package code.db;

import code.db.order_structure_component.OrderStructureComponentDB;
import code.db.order_structure_component.OrderStructureComponentRowDB;
import code.db.order_structure_component.OrderStructureComponentRowDBMapper;
import code.db.payment_component.PaymentComponentDB;
import code.db.payment_component.PaymentDB;
import code.db.payment_component.PaymentDBMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

/**
 * Created by ixotum on 7/7/15
 */
public class OrdersJDBCTemplate {
    private JdbcTemplate jdbcTemplate;

    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void saveNewOrder(OrderDB orderDB) {
        String sql = "INSERT INTO ORDERS(ORDER_ID, NAME, CUSTOMER, ADDRESS, VK, DUE_DATE, EVENT_DATE, DESCRIPTION, NOTES) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, orderDB.getOrderId(), orderDB.getName(), orderDB.getCustomer(), orderDB.getAddress(),
                orderDB.getVk(), orderDB.getDueDate(), orderDB.getEventDate(), orderDB.getDescription(), orderDB.getNotes());

        int orderId = orderDB.getOrderId();
        saveNewOrderStructureComponent(orderId, orderDB.getOrderStructureComponentDB());
        saveNewPaymentComponent(orderId, orderDB.getPaymentComponentDB());
    }

    private void saveNewPaymentComponent(int orderId, PaymentComponentDB paymentComponentDB) {
        String sql = "INSERT INTO PAYMENT(ORDER_ID, DATE, PAYMENT) VALUES (?, ?, ?)";
        int paymentsCount = paymentComponentDB.getPaymentsCount();

        for (int paymentIndex = 0; paymentIndex < paymentsCount; ++paymentIndex) {
            PaymentDB paymentDB = paymentComponentDB.getPayment(paymentIndex);
            jdbcTemplate.update(sql, orderId, paymentDB.getDate(), paymentDB.getPayment());
        }
    }

    private void saveNewOrderStructureComponent(int orderId, OrderStructureComponentDB orderStructureComponentDB) {
        String sql = "INSERT INTO ORDER_STRUCTURE_COMPONENTS(ORDER_ID, POSITION, ITEM, PRICE) VALUES (?, ?, ?, ?)";
        int componentRowCount = orderStructureComponentDB.getComponentRowCount();

        for (int rowIndex = 0; rowIndex < componentRowCount; ++rowIndex) {
            OrderStructureComponentRowDB componentRowDB = orderStructureComponentDB.getComponentRow(rowIndex);
            jdbcTemplate.update(sql, orderId, rowIndex, componentRowDB.getItem(), componentRowDB.getPrice());
        }
    }

    public List<OrderDB> readAllOrders() {
        String sql = "SELECT * FROM ORDERS";
        List<OrderDB> orderDBList = jdbcTemplate.query(sql, new OrderDBMapper());
        List<Integer> orderIdList = extractOrderIdList(orderDBList);

        List<OrderStructureComponentDB> orderStructureComponentDBList = readAllOrderStructureComponents(orderIdList);
        if (!orderStructureComponentDBList.isEmpty()) {
            for (OrderDB orderDB : orderDBList) {
                try {
                    OrderStructureComponentDB orderStructureComponentDB = orderStructureComponentDBList.stream().filter(component -> isOrderIdEqual(orderDB.getOrderId(), component.getOrderId())).findFirst().get();
                    orderDB.setOrderStructureComponentDB(orderStructureComponentDB);
                } catch (NoSuchElementException e) {
                    System.out.println("INFO: orderStructureComponentDB is empty!");
                }
            }
        }

        List<PaymentComponentDB> paymentComponentDBList = readAllPaymentComponents(orderIdList);
        if (!paymentComponentDBList.isEmpty()) {
            for (OrderDB orderDB : orderDBList) {
                try {
                    PaymentComponentDB paymentComponentDB = paymentComponentDBList.stream().filter(component -> isOrderIdEqual(orderDB.getOrderId(), component.getOrderId())).findFirst().get();
                    orderDB.setPaymentComponentDB(paymentComponentDB);
                } catch (NoSuchElementException e) {
                    System.out.println("INFO: PaymentComponentDB is empty!");
                }
            }
        }

        return orderDBList;
    }

    private List<PaymentComponentDB> readAllPaymentComponents(List<Integer> orderIdList) {
        List<PaymentComponentDB> paymentComponentDBList = new ArrayList<>();
        String sql = "SELECT * FROM PAYMENT";
        List<PaymentDB> paymentDBList = jdbcTemplate.query(sql, new PaymentDBMapper());

        for (Integer orderId : orderIdList) {
            List<PaymentDB> filteredPaymentDBList = paymentDBList.stream().filter(row -> isOrderIdEqual(row.getOrderId(), orderId)).collect(Collectors.toList());

            if(!filteredPaymentDBList.isEmpty()) {
                PaymentComponentDB paymentComponentDB = new PaymentComponentDB();
                paymentComponentDB.setPaymentDBList(filteredPaymentDBList);
                paymentComponentDBList.add(paymentComponentDB);
            }
        }

        return paymentComponentDBList;
    }

    private List<OrderStructureComponentDB> readAllOrderStructureComponents(List<Integer> orderIdList) {
        List<OrderStructureComponentDB> orderStructureComponentDBList = new ArrayList<>();
        String sql = "SELECT * FROM ORDER_STRUCTURE_COMPONENTS";
        List<OrderStructureComponentRowDB> orderStructureComponentRowDBList = jdbcTemplate.query(sql, new OrderStructureComponentRowDBMapper());

        for (Integer orderId : orderIdList) {
            List<OrderStructureComponentRowDB> filteredRowDBList = orderStructureComponentRowDBList.stream().filter(row -> isOrderIdEqual(row.getOrderId(), orderId)).collect(Collectors.toList());

            if (!filteredRowDBList.isEmpty()) {
                OrderStructureComponentDB orderStructureComponentDB = new OrderStructureComponentDB();
                orderStructureComponentDB.setComponentRowList(filteredRowDBList);
                orderStructureComponentDBList.add(orderStructureComponentDB);
            }
        }

        return orderStructureComponentDBList;
    }

    private static boolean isOrderIdEqual(int id0, int id1) {
        return id0 == id1;
    }

    private static List<Integer> extractOrderIdList(List<OrderDB> orderDBList) {
        return orderDBList.stream().map(OrderDB::getOrderId).collect(Collectors.toList());
    }

    public void updateExistedOrder(OrderDB orderDB) {
        String sql = "UPDATE ORDERS SET NAME=?, CUSTOMER=?, ADDRESS=?, VK=?, DUE_DATE=?, EVENT_DATE=?, DESCRIPTION=?, NOTES=? " +
                "WHERE ORDER_ID = ?";
        jdbcTemplate.update(sql, orderDB.getName(), orderDB.getCustomer(), orderDB.getAddress(), orderDB.getVk(),
                orderDB.getDueDate(), orderDB.getEventDate(), orderDB.getDescription(), orderDB.getNotes(), orderDB.getOrderId());

        int orderId = orderDB.getOrderId();
        deleteOrderStructureComponent(orderId);
        saveNewOrderStructureComponent(orderId, orderDB.getOrderStructureComponentDB());

        deletePaymentComponent(orderId);
        saveNewPaymentComponent(orderId, orderDB.getPaymentComponentDB());
    }

    private void deletePaymentComponent(int orderId) {
        String sql = "DELETE FROM PAYMENT WHERE ORDER_ID = ?";
        jdbcTemplate.update(sql, orderId);
    }

    private void deleteOrderStructureComponent(int orderId) {
        String sql = "DELETE FROM ORDER_STRUCTURE_COMPONENTS WHERE ORDER_ID = ?";
        jdbcTemplate.update(sql, orderId);
    }
}
