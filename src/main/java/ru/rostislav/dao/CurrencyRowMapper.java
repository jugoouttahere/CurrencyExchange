package ru.rostislav.dao;

import org.springframework.jdbc.core.RowMapper;
import ru.rostislav.model.Currency;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CurrencyRowMapper implements RowMapper<Currency> {

    @Override
    public Currency mapRow(ResultSet rs, int rowNum) throws SQLException {
        Currency currency = new Currency();

        currency.setId(rs.getInt("id"));
        currency.setCode(rs.getString("code"));
        currency.setFullName(rs.getString("full_name"));
        currency.setSign(rs.getString("sign"));

        return currency;
    }
}
