package com.epam.esm.dao;

import com.epam.esm.Tag;
import java.util.List;
import java.util.Optional;

public interface TagDAO {

    Optional<Tag> save (Tag tag);
    void delete (Long id);
    Optional<Tag> findById(Long id);
    Optional<Tag> findByName(String name);
    List<Tag> find();

}
