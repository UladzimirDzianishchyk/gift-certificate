package com.epam.esm.service;

import com.epam.esm.Tag;
import java.util.List;

public interface TagService {

    Tag save(Tag tag);
    Tag findById(Long id);
    Tag findByName(String name);
    List<Tag> find();
    void deleteById(Long id);
}
