package ru.rostislav.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.rostislav.exception.ExchangeRateNotFoundException;
import ru.rostislav.model.ExchangeRate;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class ExchangeRateRepository {
    private static final RowMapper<ExchangeRate> EXCHANGE_RATE_ROW_MAPPER = (rs, rowNum) -> new ExchangeRate(
            rs.getInt("id"),
            rs.getInt("base_currency_id"),
            rs.getInt("target_currency_id"),
            rs.getDouble("rate")
    );

    private final JdbcTemplate jdbcTemplate;

    public ExchangeRateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public ExchangeRate findByCurrencyIds(int baseId, int targetId) {
        String sql = "SELECT id, base_currency_id, target_currency_id, rate FROM exchange_rates WHERE base_currency_id = ? AND target_currency_id = ?";

        List<ExchangeRate> list = jdbcTemplate.query(sql, EXCHANGE_RATE_ROW_MAPPER, baseId, targetId);

        return list.isEmpty() ? null : list.get(0);
    }

    public List<ExchangeRate> findAll() {
        String sql = "SELECT id, base_currency_id, target_currency_id, rate FROM exchange_rates";

        return jdbcTemplate.query(sql, EXCHANGE_RATE_ROW_MAPPER);
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

    public void updateRate(int baseId, int targetId, Double rate) {
        String sql = "UPDATE exchange_rates SET rate = ? WHERE base_currency_id = ? AND target_currency_id = ?";

        int updated = jdbcTemplate.update(sql, rate, baseId, targetId);

        if (updated == 0) {
            throw new ExchangeRateNotFoundException("Exchange rate not found");
        }
    }

}
