package code.db;

import code.db.order_structure_component.OrderStructureComponentDB;
import code.db.order_structure_component.OrderStructureComponentRowDB;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;

/**
 * Created by ixotum on 7/7/15
 */
public class OrdersJDBCTemplate {
    private JdbcTemplate jdbcTemplate;

    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void saveNewOrder(OrderDB orderDB) {
        String sql = "INSERT INTO ORDERS(ORDER_ID, NAME, STRUCTURE, PRICE, CUSTOMER, VK, DUE_DATE, EVENT_DATE, DESCRIPTION) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, orderDB.getOrderId(), orderDB.getName(), orderDB.getStructure(), orderDB.getPrice(), orderDB.getCustomer(),
                orderDB.getVk(), orderDB.getDueDate(), orderDB.getEventDate(), orderDB.getDescription());

        saveNewOrderStructureComponent(orderDB.getOrderId(), orderDB.getOrderStructureComponentDB());
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
        return jdbcTemplate.query(sql, new OrderDBMapper());
    }

    public void updateExistedOrder(OrderDB orderDB) {
        String sql = "UPDATE ORDERS SET NAME=?, STRUCTURE=?, PRICE=?, CUSTOMER=?, VK=?, DUE_DATE=?, EVENT_DATE=?, DESCRIPTION=? " +
                "WHERE ORDER_ID = ?";
        jdbcTemplate.update(sql, orderDB.getName(), orderDB.getStructure(), orderDB.getPrice(), orderDB.getCustomer(), orderDB.getVk(),
                orderDB.getDueDate(), orderDB.getEventDate(), orderDB.getDescription(), orderDB.getOrderId());
    }
}
