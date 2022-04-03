package com.epam.esm.service;

import com.epam.esm.Tag;
import com.epam.esm.daoImpl.TagDAOImpl;
import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.exception.EntityNotFoundByIdException;
import com.epam.esm.serviceimpl.TagServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class TagServiceTest {
    TagDAOImpl tagDAOMoc = Mockito.mock(TagDAOImpl.class);
    Tag tag = new Tag("testTag");
    List<Tag> tags = new ArrayList<>();

    TagService tagService = new TagServiceImpl( tagDAOMoc);


    @Test
    void findById() {
        Mockito.when(tagDAOMoc.findById(1L)).thenReturn(Optional.of(tag));
        assertEquals(tag,tagService.findById(1L));

        Throwable thrown = assertThrows(EntityNotFoundByIdException.class, () -> tagService.findById(100L));
        assertNotNull(thrown.getMessage());

    }

    @Test
    void findByName() {
        Mockito.when(tagDAOMoc.findByName("testTag")).thenReturn(Optional.of(tag));
        assertEquals(tag,tagService.findByName("testTag"));
        Throwable thrown = assertThrows(EntityNotFoundException.class, () -> tagService.findByName(""));
        assertNotNull(thrown.getMessage());
    }

    @Test
    void showAll() {
        tags.add(tag);
        Mockito.when(tagDAOMoc.find()).thenReturn(tags);
        assertEquals(tags,tagService.find());

    }
}