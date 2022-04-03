package com.epam.esm.serviceimpl;

import com.epam.esm.GiftCertificate;
import com.epam.esm.Tag;
import com.epam.esm.dao.GiftCertificateDAO;
import com.epam.esm.dao.GiftCertificateTagDAO;
import com.epam.esm.dao.TagDAO;
import com.epam.esm.exception.*;
import com.epam.esm.service.GiftCertificateService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Service
public class GiftCertificateServiceImpl implements GiftCertificateService {

    private final GiftCertificateTagDAO giftCertificateTagDAO;
    private final TagDAO tagDAO;
    private final GiftCertificateDAO giftCertificateDAO;

    public GiftCertificateServiceImpl(GiftCertificateTagDAO giftCertificateTagDAO, TagDAO tagDAO, GiftCertificateDAO giftCertificateDAO) {
        this.giftCertificateTagDAO = giftCertificateTagDAO;
        this.tagDAO = tagDAO;
        this.giftCertificateDAO = giftCertificateDAO;
    }

    @Transactional
    public GiftCertificate newCertificate(GiftCertificate giftCertificate) {
        if (!giftCertificateDAO.findById(giftCertificate.getId()).isPresent()) throw
                new EntityNotFoundByIdException(giftCertificate.getId());
        GiftCertificate giftCertificateReturn = giftCertificateDAO.save(giftCertificate).get();
        List<Tag> tagList = giftCertificate.getTags();
        List<Tag> tagsFromDb = new ArrayList<>();
        Long giftCertId = giftCertificateReturn.getId();

        /* attach tags to certificate */
        for (Tag tag : tagList) {
            String tagName = tag.getName();

            /* if tag doesn't exist create */
            if (!tagDAO.findByName(tagName).isPresent()) {
                tagDAO.save(tag);
            }

            Long tagId = tagDAO.findByName(tagName).orElseThrow(() -> new EntityNotFoundException(tagName)).getId();

            /* attach tag to certificate if not attached */
            if (!giftCertificateTagDAO.showTagIdFromCert(giftCertId).contains(tagId)) {
                giftCertificateTagDAO.addTagToCert(giftCertId,
                        tagDAO.findByName(tagName).orElseThrow(() -> new EntityNotFoundByIdException(tagId)).getId());
            }
        }
        List<Long> tagsId = giftCertificateTagDAO.showTagIdFromCert(giftCertificateReturn.getId());
        if (!tagsId.isEmpty()) {
            for (Long tagId : tagsId) {
                Tag tag = tagDAO.findById(tagId).orElseThrow(() -> new EntityNotFoundByIdException(tagId));
                tagsFromDb.add(tag);
            }
        }

        giftCertificateReturn.setTags(tagsFromDb);

        return giftCertificateReturn;
    }

    @Transactional
    public GiftCertificate update(GiftCertificate giftCertificate) {
        if (giftCertificate.getId() == null) throw new EntityNotFoundByIdException(giftCertificate.getId());
        Long id = giftCertificate.getId();
        List<Tag> tags = new ArrayList<>();
        giftCertificateDAO.findById(id).orElseThrow(() -> new EntityNotFoundByIdException(id));
        GiftCertificate cert = giftCertificateDAO.update(giftCertificate).get();
        if (giftCertificate.getTags() != null) {
            for (Tag tag : giftCertificate.getTags()) {
                String tagName = tag.getName();
                if (tagDAO.findByName(tag.getName()).isPresent()) {
                    long tagId = tagDAO.findByName(tagName).get().getId();
                    if (!giftCertificateTagDAO.showTagIdFromCert(id).contains(tagId))
                        giftCertificateTagDAO.addTagToCert(id, tagId);
                } else {
                    tagDAO.save(tag);
                    giftCertificateTagDAO.addTagToCert(id, tagDAO.findByName(tag.getName()).get().getId());
                }
            }

        }
        List<Long> tagsId = giftCertificateTagDAO.showTagIdFromCert(id);
        if (!tagsId.isEmpty()) {
            for (Long tagId : tagsId) {
                Tag tag = tagDAO.findById(tagId).get();
                tags.add(tag);
            }
        }
        cert.setTags(tags);

        return cert;
    }

    public GiftCertificate findById(Long id) {
        GiftCertificate giftCertificate = giftCertificateDAO.findById(id).orElseThrow(() -> new EntityNotFoundByIdException(id));
        List<Long> tagsId = giftCertificateTagDAO.showTagIdFromCert(id);
        List<Tag> tags = new ArrayList<>();
        if (tagsId.size() != 0) {
            for (Long tagId : tagsId) {
                tags.add(tagDAO.findById(tagId).get());
            }
        }
        giftCertificate.setTags(tags);


        return giftCertificate;
    }


    @Transactional
    public List<GiftCertificate> find() {
        List<GiftCertificate> giftCertificates = giftCertificateDAO.find();
        if (giftCertificates.isEmpty()) throw new EntityNotFoundException("");
        for (GiftCertificate cert : giftCertificates) {
            List<Long> tagsId = giftCertificateTagDAO.showTagIdFromCert(cert.getId());
            List<Tag> tags = new ArrayList<>();
            if (tagsId.size() != 0) {
                for (long tagId : tagsId) {
                    tags.add(tagDAO.findById(tagId).get());
                }
                cert.setTags(tags);
            }

        }

        return giftCertificates;
    }

    @Transactional
    public void deleteById(Long id) {
        giftCertificateDAO.findById(id).orElseThrow(() -> new EntityNotFoundByIdException(id));
        List<Long> tagIds = giftCertificateTagDAO.showTagIdFromCert(id);
        giftCertificateDAO.delete(id);
        /*  delete tags without certificate  */
        for (long tagId : tagIds) {
            if (giftCertificateTagDAO.findCertIdFromTag(tagId).isEmpty()) {
                tagDAO.delete(tagId);
            }
        }
    }

    @Transactional
    public List<GiftCertificate> getCertByTagName(String tagName) throws EntityNotFoundException {
        /*check if tag is empty throw exception*/
        Tag tag = tagDAO.findByName(tagName).orElseThrow(() -> new EntityNotFoundException(tagName));
        /* get id certificates by tag */
        List<Long> certificatesId;
        List<GiftCertificate> certificates = new ArrayList<>();

        certificatesId = giftCertificateTagDAO.findCertIdFromTag(tag.getId());
        for (long certId : certificatesId) {
            /* get certificate by id */
            GiftCertificate giftCertificate = giftCertificateDAO.findById(certId).get();
            /* get id attached tags */
            List<Long> tagsId = giftCertificateTagDAO.showTagIdFromCert(certId);
            List<Tag> tagList = new ArrayList<>();
            /* add tags to list */
            for (long tagId : tagsId) {
                tagList.add(tagDAO.findById(tagId).get());
            }

            giftCertificate.setTags(tagList);

            certificates.add(giftCertificate);
        }

        return certificates;
    }


    public List<GiftCertificate> setTagsToCert(List<GiftCertificate> giftCertificates) {

        /* add tags to certificate */
        for (GiftCertificate cert : giftCertificates) {
            List<Long> tagsId = giftCertificateTagDAO.showTagIdFromCert(cert.getId());
            List<Tag> tagList = new ArrayList<>();
            for (long tagId : tagsId) {
                Tag tag = tagDAO.findById(tagId).get();
                tagList.add(tag);
            }
            cert.setTags(tagList);
        }

        return giftCertificates;
    }


    public List<GiftCertificate> getCertByPartName(String partName) throws EntityNotFoundException {
        /* get certificate for part of name */
        List<GiftCertificate> giftCertificates = giftCertificateDAO.findByPartColumnName("name", partName);
        if (giftCertificates.isEmpty()) throw new EntityNotFoundException(partName);
        return setTagsToCert(giftCertificates);
    }

    @Transactional
    public List<GiftCertificate> getCertByPartDescription(String partDescription) throws EntityNotFoundException {
        /* get certificate for part of description */
        List<GiftCertificate> giftCertificates = giftCertificateDAO.findByPartColumnName("description", partDescription);
        if (giftCertificates.isEmpty()) throw new EntityNotFoundException(partDescription);

        return setTagsToCert(giftCertificates);
    }

    @Transactional
    public List<GiftCertificate> findCertByPartNameSortByCreateDate(String partName) throws EntityNotFoundException {
        /* get certificate for part of name, sort by date */
        List<GiftCertificate> giftCertificates = new LinkedList<>();
        giftCertificates = giftCertificateDAO.findByValueOrderBySortValue("name", "create_date", partName);
        if (giftCertificates.isEmpty()) throw new EntityNotFoundException(partName);

        return setTagsToCert(giftCertificates);
    }

    @Transactional
    public List<GiftCertificate> findCertByPartNameSortByName(String partName) throws EntityNotFoundException {
        /* get certificate for part of name, sort by name */
        List<GiftCertificate> giftCertificates = new LinkedList<>();
        giftCertificates = giftCertificateDAO.findByValueOrderBySortValue("name", "name", partName);

        if (giftCertificates.isEmpty()) throw new EntityNotFoundException(partName);

        return setTagsToCert(giftCertificates);
    }


}
