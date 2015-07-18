package code.db;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by ixotum on 7/10/15
 */
public class OrderDBMapper implements RowMapper {
    @Override
    public Object mapRow(ResultSet rs, int rowNumber) throws SQLException {
        OrderDB orderDB = new OrderDB(rs.getInt("ORDER_ID"));
        orderDB.setName(rs.getString("NAME"));
        orderDB.setStructure(rs.getString("STRUCTURE"));
        return orderDB;
    }
}
