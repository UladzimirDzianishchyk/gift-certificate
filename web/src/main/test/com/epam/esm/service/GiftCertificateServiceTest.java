package com.epam.esm.service;

import com.epam.esm.GiftCertificate;
import com.epam.esm.Tag;
import com.epam.esm.dao.GiftCertificateDAO;
import com.epam.esm.dao.GiftCertificateTagDAO;
import com.epam.esm.dao.TagDAO;
import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.exception.EntityNotFoundByIdException;
import com.epam.esm.serviceimpl.GiftCertificateServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class GiftCertificateServiceTest {

    GiftCertificateDAO giftCertificateDAOMoc = Mockito.mock(GiftCertificateDAO.class);
    GiftCertificateTagDAO giftCertificateTagDAOMoc = Mockito.mock(GiftCertificateTagDAO.class);
    TagDAO tagDAOMoc = Mockito.mock(TagDAO.class);
    GiftCertificate giftCertificate = new GiftCertificate("cert", "desc", 1.2, 5L);
    GiftCertificate giftCertificateWithOutId = new GiftCertificate();
    Tag tag = new Tag("tag");
    List<GiftCertificate> giftCertificates = new LinkedList<>();
    List<Long> certsId = new ArrayList<>();
    List<Long> tagsId = new ArrayList<>();
    List<Tag> tags = new ArrayList<>();

    GiftCertificateService giftCertificateService = new GiftCertificateServiceImpl(giftCertificateTagDAOMoc, tagDAOMoc, giftCertificateDAOMoc);


    @BeforeEach
    void beforeClass() {
        tagsId.add(1L);
        tag.setId(1L);
        tags.add(tag);
        giftCertificate.setId(1L);
        giftCertificate.setTags(tags);
        giftCertificates.add(giftCertificate);
        certsId.add(1L);
        Mockito.when(tagDAOMoc.findByName(tag.getName())).thenReturn(Optional.of(tag));
        Mockito.when(giftCertificateTagDAOMoc.showTagIdFromCert(giftCertificate.getId())).thenReturn(tagsId);
        Mockito.when(tagDAOMoc.findById(tag.getId())).thenReturn(Optional.of(tag));
        Mockito.when(giftCertificateDAOMoc.findById(1L)).thenReturn(Optional.of(giftCertificate));

    }


    @Test
    void addGiftCertificate() {
        Mockito.when(giftCertificateDAOMoc.save(giftCertificate)).thenReturn(Optional.of(giftCertificate));
        assertEquals(giftCertificate, giftCertificateService.newCertificate(giftCertificate));
    }

    @Test
    void update() {
        Mockito.when(giftCertificateDAOMoc.update(giftCertificate)).thenReturn(Optional.of(giftCertificate));
        assertEquals(giftCertificate, giftCertificateService.update(giftCertificate));
        assertThrows(EntityNotFoundByIdException.class, () -> giftCertificateService.update(giftCertificateWithOutId));
    }

    @Test
    void showById() {
        assertEquals(giftCertificate.getId(), giftCertificateService.findById(1L).getId());
        assertThrows(EntityNotFoundByIdException.class, () -> giftCertificateService.findById(5L));
    }

    @Test
    void showAll() {
        Mockito.when(giftCertificateDAOMoc.find()).thenReturn(giftCertificates);
        assertEquals(giftCertificates, giftCertificateService.find());
        Mockito.when(giftCertificateDAOMoc.find()).thenThrow(EntityNotFoundException.class);
        assertThrows(EntityNotFoundException.class, () -> giftCertificateService.find());
    }

    @Test
    void getCertByTagName() {
        Mockito.when(giftCertificateTagDAOMoc.findCertIdFromTag(tag.getId())).thenReturn(tagsId);
        assertEquals(giftCertificates, giftCertificateService.getCertByTagName(tag.getName()));
        assertThrows(EntityNotFoundException.class, () -> giftCertificateService.getCertByTagName("noName"));
    }

    @Test
    void getCertByPartName() {
        Mockito.when(giftCertificateDAOMoc.findByPartColumnName("name", "cert")).thenReturn(giftCertificates);
        assertEquals(giftCertificates, giftCertificateService.getCertByPartName("cert"));
        assertThrows(EntityNotFoundException.class,() -> giftCertificateService.getCertByPartName("ce"));
    }

    @Test
    void getCertByPartDescription() {
        Mockito.when(giftCertificateDAOMoc.findByPartColumnName("description", "desc")).thenReturn(giftCertificates);
        assertEquals(giftCertificates, giftCertificateService.getCertByPartDescription("desc"));
        assertThrows(EntityNotFoundException.class,() -> giftCertificateService.getCertByPartName("other"));
    }

    @Test
    void getCertByPartNameSortByCreateDate() {
        Mockito.when(giftCertificateDAOMoc.findByValueOrderBySortValue("name","create_date","cert"))
                .thenReturn(giftCertificates);
        assertEquals(giftCertificates,giftCertificateService.findCertByPartNameSortByCreateDate("cert"));
        assertThrows(EntityNotFoundException.class,() -> giftCertificateService.findCertByPartNameSortByCreateDate("other"));
    }

    @Test
    void getCertByPartNameSortByName() {
        Mockito.when(giftCertificateDAOMoc.findByValueOrderBySortValue("name", "name","cert"))
                .thenReturn(giftCertificates);
        assertEquals(giftCertificates,giftCertificateService.findCertByPartNameSortByName("cert"));
        assertThrows(EntityNotFoundException.class,() -> giftCertificateService.findCertByPartNameSortByName("other"));
    }
}