package code.db;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

/**
 * Created by ixotum on 7/5/15.
 */
public class SettingsJDBCTemplate {
    private JdbcTemplate jdbcTemplate;

    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public int readLastOrderId() {
        String sql = "SELECT LAST_ORDER_ID FROM SETTINGS";
        try {
            SettingsDB settingsDB = (SettingsDB) jdbcTemplate.queryForObject(sql, new SettingsDBMapper());
            return settingsDB.getLastOrderId();
        } catch (EmptyResultDataAccessException e) {
            return 0;
        }
    }

    public DataSource getDataSource() {
        return jdbcTemplate.getDataSource();
    }

    public void saveLastOrderId(int lastOrderId) {
        String sql = (lastOrderId == 1) ? "INSERT INTO SETTINGS(LAST_ORDER_ID) VALUES (?)" : "UPDATE SETTINGS SET LAST_ORDER_ID=?";
        jdbcTemplate.update(sql, lastOrderId);
    }
}
