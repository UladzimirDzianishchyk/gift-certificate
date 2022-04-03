package com.epam.esm.daoImpl;

import com.epam.esm.dao.GiftCertificateTagDAO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class GiftCertificateTagDAOImpl implements GiftCertificateTagDAO {

    private final JdbcTemplate jdbcTemplate;

    public GiftCertificateTagDAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addTagToCert(Long giftCertificateId, Long tagId) {
        String SQL="insert into gift_certificate_tag (gift_certificate_id,tag_id) values (?,?)";
        jdbcTemplate.update(SQL,giftCertificateId,tagId);
    }

    @Override
    public List<Long> showTagIdFromCert(Long certId) {
        String SQL = "select tag_id from gift_certificate_tag where gift_certificate_id=?";
        return jdbcTemplate.queryForList(SQL,Long.class,certId);
    }

    @Override
    public List<Long> findCertIdFromTag(Long tagId) {
        String SQL ="select gift_certificate_id from gift_certificate_tag where tag_id=?";
        return jdbcTemplate.queryForList(SQL,Long.class,tagId);
    }
}
