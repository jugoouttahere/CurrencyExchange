package ru.rostislav.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.rostislav.model.Currency;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class CurrencyDao {
    private final JdbcTemplate jdbcTemplate;

    public CurrencyDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Currency> findAll() {
        String sql = "SELECT id, code, full_name, sign FROM currencies";
        return jdbcTemplate.query(sql, new CurrencyRowMapper());
    }

    public Currency findByCode(String code) {
        String sql = "SELECT id, code, full_name, sign FROM currencies WHERE code = ?";
        List<Currency> list = jdbcTemplate.query(sql, new CurrencyRowMapper(), code);
        return list.isEmpty() ? null : list.get(0);
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
