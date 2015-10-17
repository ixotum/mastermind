package code.db.settings;

import code.db.settings.SettingsDB;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by ixotum on 7/6/15
 */
public class SettingsDBMapper implements RowMapper {
    @Override
    public Object mapRow(ResultSet rs, int i) throws SQLException {
        SettingsDB settingsDB = new SettingsDB();
        settingsDB.setLastOrderId(rs.getInt("LAST_ORDER_ID"));
        return settingsDB;
    }
}
