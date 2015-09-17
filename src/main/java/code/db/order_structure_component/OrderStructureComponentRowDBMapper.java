package code.db.order_structure_component;

import code.db.order_structure_component.OrderStructureComponentRowDB;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderStructureComponentRowDBMapper implements RowMapper {
    @Override
    public Object mapRow(ResultSet rs, int rowNumber) throws SQLException {
        OrderStructureComponentRowDB orderStructureComponentRowDB = new OrderStructureComponentRowDB();
        orderStructureComponentRowDB.setOrderId(rs.getInt("ORDER_ID"));
        orderStructureComponentRowDB.setPosition(rs.getInt("POSITION"));
        orderStructureComponentRowDB.setItem(rs.getString("ITEM"));
        orderStructureComponentRowDB.setPrice(rs.getBigDecimal("PRICE"));
        return orderStructureComponentRowDB;
    }
}
