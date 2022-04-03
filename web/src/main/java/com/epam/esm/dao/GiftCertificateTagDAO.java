package com.epam.esm.dao;

import java.util.List;


public interface GiftCertificateTagDAO {

    void addTagToCert(Long giftCertificateId, Long tagId);
    List<Long> showTagIdFromCert(Long certId);
    List<Long> findCertIdFromTag(Long tagId);
}
