package code.db;

import code.db.order_structure_component.OrderStructureComponentDB;
import code.db.order_structure_component.OrderStructureComponentRowDB;
import code.db.order_structure_component.OrderStructureComponentRowDBMapper;
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

        saveNewOrderStructureComponent(orderDB.getOrderId(), orderDB.getOrderStructureComponentDB());
    }

    private void saveNewOrderStructureComponent_old(int orderId, OrderStructureComponentDB orderStructureComponentDBOld) {
        String sql = "INSERT INTO ORDER_STRUCTURE_COMPONENTS(ORDER_ID, POSITION, ITEM, PRICE) VALUES (?, ?, ?, ?)";
        int componentRowCount = orderStructureComponentDBOld.getComponentRowCount();

        for (int rowIndex = 0; rowIndex < componentRowCount; ++rowIndex) {
            OrderStructureComponentRowDB componentRowDB = orderStructureComponentDBOld.getComponentRow(rowIndex);
            jdbcTemplate.update(sql, orderId, rowIndex, componentRowDB.getItem(), componentRowDB.getPrice());
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
                    System.out.println("INFO: orderStructureComponentDBOld is empty!");
                }
            }
        }

        return orderDBList;
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

        deleteOrderStructureComponent(orderDB.getOrderId());
        saveNewOrderStructureComponent(orderDB.getOrderId(), orderDB.getOrderStructureComponentDB());
    }

    private void deleteOrderStructureComponent(int orderId) {
        String sql = "DELETE FROM ORDER_STRUCTURE_COMPONENTS WHERE ORDER_ID = ?";
        jdbcTemplate.update(sql, orderId);
    }
}
