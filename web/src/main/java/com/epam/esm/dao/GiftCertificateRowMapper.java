package com.epam.esm.dao;

import com.epam.esm.GiftCertificate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

@Component
public class GiftCertificateRowMapper implements RowMapper<GiftCertificate> {



    @Override
    public GiftCertificate mapRow(ResultSet rs, int rowNum) throws SQLException {

        GiftCertificate giftCertificate = new GiftCertificate();


        giftCertificate.setId(rs.getLong("id"));
        giftCertificate.setName(rs.getString("name"));
        giftCertificate.setDescription(rs.getString("description"));
        giftCertificate.setPrice(rs.getDouble("price"));
        giftCertificate.setDuration(rs.getLong("duration"));
        giftCertificate.setCreateDate(rs.getObject("create_date", LocalDateTime.class));
        giftCertificate.setLastUpdateDate(rs.getObject("last_update_date",LocalDateTime.class));
        return giftCertificate;
    }
}
