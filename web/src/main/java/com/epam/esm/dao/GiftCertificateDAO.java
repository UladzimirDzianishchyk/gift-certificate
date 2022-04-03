package com.epam.esm.dao;

import com.epam.esm.GiftCertificate;
import java.util.List;
import java.util.Optional;

public interface GiftCertificateDAO {

    Optional<GiftCertificate> save(GiftCertificate giftCertificate);
    Optional<GiftCertificate> update(GiftCertificate giftCertificate);
    Optional<GiftCertificate> findById(Long id);
    Optional<GiftCertificate> findByName(String name);
    List<GiftCertificate> find();
    void delete(Long id);
    List<GiftCertificate> findByPartColumnName(String columnName,String value);
    List<GiftCertificate> findByValueOrderBySortValue(String columnName,String sortValue ,String value);
}
