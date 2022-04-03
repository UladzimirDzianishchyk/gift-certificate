package com.epam.esm.daoImpl;

import com.epam.esm.Tag;
import com.epam.esm.dao.TagDAO;
import com.epam.esm.dao.TagRowMapper;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public class TagDAOImpl implements TagDAO {


    private final JdbcTemplate jdbcTemplate;
    private final TagRowMapper tagRowMapper;

    public TagDAOImpl(JdbcTemplate jdbcTemplate, TagRowMapper tagRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.tagRowMapper = tagRowMapper;
    }

    @Override
    public Optional<Tag> save(Tag tag) {
        jdbcTemplate.update("INSERT INTO tag VALUES(default,?)", tag.getName());
        return findByName(tag.getName());
    }

    @Override
    public void delete(Long id) {

        jdbcTemplate.update("DELETE FROM tag WHERE id=?", id);

    }

    @Override
    public Optional<Tag> findById(Long id) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject("SELECT * FROM tag WHERE id=?",tagRowMapper, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Tag> findByName(String name) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject("SELECT * FROM tag WHERE name=?",tagRowMapper, name));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Tag> find() {
        return jdbcTemplate.query("SELECT * FROM tag",new TagRowMapper());
    }


}
