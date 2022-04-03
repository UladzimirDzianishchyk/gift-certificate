package com.epam.esm.service;

import com.epam.esm.GiftCertificate;
import java.util.List;

public interface GiftCertificateService {
    GiftCertificate newCertificate(GiftCertificate giftCertificate);
    GiftCertificate update(GiftCertificate giftCertificate);
    GiftCertificate findById(Long id);
    List<GiftCertificate> find();
    void deleteById(Long id);
    List<GiftCertificate> getCertByTagName(String tagName);
    List<GiftCertificate> setTagsToCert(List<GiftCertificate> giftCertificates);
    List<GiftCertificate> getCertByPartName(String partName);
    List<GiftCertificate> getCertByPartDescription(String partDescription);
    List<GiftCertificate> findCertByPartNameSortByCreateDate(String partName);
    List<GiftCertificate> findCertByPartNameSortByName(String partName);

}
