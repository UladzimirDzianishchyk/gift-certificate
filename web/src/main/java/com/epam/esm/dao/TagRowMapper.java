package com.epam.esm.dao;

import com.epam.esm.Tag;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class TagRowMapper implements RowMapper<Tag> {
    @Override
    public Tag mapRow(ResultSet rs, int rowNum) throws SQLException {

        Tag tag = new Tag();

        tag.setId(rs.getLong("id"));
        tag.setName(rs.getString("name"));
        return tag;
    }
}
