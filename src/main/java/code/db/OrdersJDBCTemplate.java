package code.db;

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
        String sql = "INSERT INTO ORDERS(ORDER_ID, NAME) " +
                "VALUES (?, ?)";
        jdbcTemplate.update(sql, orderDB.getOrderId(), orderDB.getName());
    }

    public List<OrderDB> readAllOrders() {
        String sql = "SELECT * FROM ORDERS";
        return jdbcTemplate.query(sql, new OrderDBMapper());
    }
}
