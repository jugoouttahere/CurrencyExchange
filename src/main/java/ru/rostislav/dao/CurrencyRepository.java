package ru.rostislav.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.rostislav.exception.CurrencyNotFoundException;
import ru.rostislav.model.Currency;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class CurrencyRepository {

    private static final RowMapper<Currency> CURRENCY_ROW_MAPPER =
            (rs, rowNum) -> new Currency(
                    rs.getInt("id"),
                    rs.getString("code"),
                    rs.getString("full_name"),
                    rs.getString("sign")
            );

    private final JdbcTemplate jdbcTemplate;

    public CurrencyRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Currency> findAll(int limit, int offset) {
        String sql = "SELECT id, code, full_name, sign FROM currencies LIMIT ? OFFSET ?";
        return jdbcTemplate.query(sql, CURRENCY_ROW_MAPPER, limit, offset);
    }

    public Currency findByCode(String code) {
        String sql = "SELECT id, code, full_name, sign FROM currencies WHERE code = ?";
        List<Currency> list = jdbcTemplate.query(sql, CURRENCY_ROW_MAPPER, code);
        if (list.isEmpty()) {
            throw new CurrencyNotFoundException("Currency not found: " + code);
        }
        return list.get(0);
    }

    public Currency findById(int id) {
        String sql = "SELECT id, code, full_name, sign FROM currencies WHERE id = ?";
        List<Currency> list = jdbcTemplate.query(sql, CURRENCY_ROW_MAPPER, id);
        if (list.isEmpty()) {
            throw new CurrencyNotFoundException("Currency not found by id: " + id);
        }
        return list.get(0);
    }

    public Currency insert(String code, String name, String sign) {
        String sql = "INSERT INTO currencies (code, full_name, sign) VALUES (?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    sql,
                    Statement.RETURN_GENERATED_KEYS
            );

            ps.setString(1, code);
            ps.setString(2, name);
            ps.setString(3, sign);
            return ps;
        }, keyHolder);

        int id = keyHolder.getKey().intValue();

        return new Currency(id, code, name, sign);
    }

}
