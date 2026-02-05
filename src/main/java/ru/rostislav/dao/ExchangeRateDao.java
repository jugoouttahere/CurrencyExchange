package ru.rostislav.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.rostislav.model.ExchangeRate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class ExchangeRateDao {
    private final JdbcTemplate jdbcTemplate;

    public ExchangeRateDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public ExchangeRate findByCurrencyIds(int baseId, int targetId) {
        String sql = "SELECT id, base_currency_id, target_currency_id, rate FROM exchange_rates WHERE base_currency_id = ? AND target_currency_id = ?";

        List<ExchangeRate> list = jdbcTemplate.query(
                sql,
                new RowMapper<ExchangeRate>() {
                    @Override
                    public ExchangeRate mapRow(ResultSet rs, int rowNum) throws SQLException {
                        return new ExchangeRate(
                                rs.getInt("id"),
                                rs.getInt("base_currency_id"),
                                rs.getInt("target_currency_id"),
                                rs.getDouble("rate")
                        );
                    }
                },
                baseId, targetId);

        return list.isEmpty() ? null : list.get(0);
    }
}
