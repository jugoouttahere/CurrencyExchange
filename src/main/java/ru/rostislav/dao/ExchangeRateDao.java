package ru.rostislav.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.rostislav.dto.ExchangeRateDto;
import ru.rostislav.model.ExchangeRate;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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

    public List<ExchangeRate> findAll() {
        String sql = "SELECT id, base_currency_id, target_currency_id, rate FROM exchange_rates";

        return jdbcTemplate.query(sql,
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
                });
    }

    public ExchangeRate insert(int baseCurrencyId, int targetCurrencyId, double rate) {
        String sql = "INSERT INTO exchange_rates (base_currency_id, target_currency_id, rate) VALUES (?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    sql,
                    Statement.RETURN_GENERATED_KEYS
            );
            ps.setInt(1, baseCurrencyId);
            ps.setInt(2, targetCurrencyId);
            ps.setDouble(3, rate);
            return ps;
        }, keyHolder);

        int id = keyHolder.getKey().intValue();

        return new ExchangeRate(
                id,
                baseCurrencyId,
                targetCurrencyId,
                rate
        );
    }

}
