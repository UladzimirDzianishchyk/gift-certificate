package com.epam.esm.daoImpl;

import com.epam.esm.GiftCertificate;
import com.epam.esm.dao.GiftCertificateDAO;
import com.epam.esm.dao.GiftCertificateRowMapper;
import com.epam.esm.exception.EntityNotFoundByIdException;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static org.modelmapper.config.Configuration.AccessLevel.PRIVATE;

@Repository
public class GiftCertificateDAOImpl implements GiftCertificateDAO {

    private final JdbcTemplate jdbcTemplate;
    private final GiftCertificateRowMapper giftCertificateRowMapper;


    public GiftCertificateDAOImpl(JdbcTemplate jdbcTemplate, GiftCertificateRowMapper giftCertificateRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.giftCertificateRowMapper = giftCertificateRowMapper;
    }


    @Override
    public Optional<GiftCertificate> save(GiftCertificate giftCertificate) {

        jdbcTemplate.update("INSERT INTO gift_certificate VALUES(default,?,?,?,?,default,default)",
                giftCertificate.getName(),
                giftCertificate.getDescription(),
                giftCertificate.getPrice(),
                giftCertificate.getDuration());

        return findByName(giftCertificate.getName());
    }

    @Override
    public Optional<GiftCertificate> update(GiftCertificate giftCertificate) {

        Long id = giftCertificate.getId();
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setFieldMatchingEnabled(true)
                .setSkipNullEnabled(true)
                .setFieldAccessLevel(PRIVATE);
        TypeMap<GiftCertificate, GiftCertificate> propertyMap = mapper.createTypeMap(GiftCertificate.class, GiftCertificate.class);
        propertyMap.setProvider(p -> findById(id).orElseThrow(() -> new EntityNotFoundByIdException(id)));
        GiftCertificate update = mapper.map(giftCertificate, GiftCertificate.class);
        String SQL = "update gift_certificate set name=?,description=?,price=?,duration=?,last_update_date=default where id=" + id;
        jdbcTemplate.update(SQL,
                update.getName(),
                update.getDescription(),
                update.getPrice(),
                update.getDuration());

        return findById(id);
    }

    @Override
    public Optional<GiftCertificate> findById(Long id) {

        String SQL2 = "SELECT * FROM gift_certificate WHERE id=?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(SQL2, giftCertificateRowMapper, id));
        } catch (
                EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<GiftCertificate> findByName(String name) {
        String SQL = "SELECT * FROM gift_certificate WHERE name=?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(SQL, giftCertificateRowMapper, name));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }


    @Override
    public List<GiftCertificate> find() {
        String SQL1 = "SELECT * FROM gift_certificate";
        return jdbcTemplate.query(SQL1, giftCertificateRowMapper);
    }

    @Override
    public void delete(Long id) {
        jdbcTemplate.update("DELETE FROM gift_certificate WHERE id=?", id);
    }

    @Override
    public List<GiftCertificate> findByPartColumnName(String columnName, String value) {
        String SQL = "SELECT * FROM gift_certificate WHERE " + columnName + " like '%'||?||'%'";
        return jdbcTemplate.query(SQL, giftCertificateRowMapper, value);
    }

    @Override
    public List<GiftCertificate> findByValueOrderBySortValue(String columnName, String sortValue, String value) {
        String SQL = "SELECT * FROM gift_certificate WHERE " + columnName + " like '%'||?||'%' order by " + sortValue + " desc";
        return jdbcTemplate.query(SQL, giftCertificateRowMapper, value);
    }

}
