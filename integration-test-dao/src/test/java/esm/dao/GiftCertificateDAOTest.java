package esm.dao;

import com.epam.esm.GiftCertificate;
import com.epam.esm.config.DevConfig;
import com.epam.esm.dao.GiftCertificateDAO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = DevConfig.class)
@ActiveProfiles("dev")
@EnableTransactionManagement
@WebAppConfiguration
@Sql(executionPhase= Sql.ExecutionPhase.BEFORE_TEST_METHOD,scripts= {"classpath:testSchema.sql","classpath:testData.sql"})
@Sql(executionPhase= Sql.ExecutionPhase.AFTER_TEST_METHOD,scripts="classpath:testDrop.sql")
class GiftCertificateDAOTest {
    @Autowired
    GiftCertificateDAO giftCertificateDAO;

    GiftCertificate giftCertificate = new GiftCertificate("GiftCertificate1", "DescriptionOne", 45D, 2L);
    GiftCertificate giftCertificateFromDb = new GiftCertificate("GiftCertificateFromDb", "DescriptionOne", 45D, 2L);


    @Test
    void findById() {
        assertTrue(giftCertificateDAO.findById(1l).isPresent());
        assertEquals(giftCertificate.getName(), giftCertificateDAO.findById(1L).get().getName());
        assertFalse(giftCertificateDAO.findById(100L).isPresent());

    }

    @Test
    void save() {
        giftCertificateFromDb.setId(6L);
        assertEquals(giftCertificateFromDb.getName(), giftCertificateDAO.save(giftCertificateFromDb).get().getName());
    }

    @Test
    void delete() {
        giftCertificateDAO.delete(5L);
        assertFalse(giftCertificateDAO.findById(5L).isPresent());
    }

    @Test
    void update() {
        giftCertificateFromDb.setId(1L);
        assertEquals(giftCertificateFromDb.getName(), giftCertificateDAO.update(giftCertificateFromDb).get().getName());
    }


    @Test
    void findByName() {
        assertEquals(giftCertificate.getName(), giftCertificateDAO.findByName("GiftCertificate1").get().getName());
        assertFalse(giftCertificateDAO.findByName("").isPresent());
    }

    @Test
    void find() {
        assertFalse(giftCertificateDAO.find().isEmpty());
    }


    @Test
    void findByPartColumnName() {
        assertFalse(giftCertificateDAO.findByPartColumnName("name", "GiftCertificate1").isEmpty());
        assertTrue(giftCertificateDAO.findByPartColumnName("name", "dgdgd").isEmpty());
    }

    @Test
    void findByValueOrderBySortValue() {
        assertFalse(giftCertificateDAO.findByValueOrderBySortValue("name", "name", "GiftCertificate1").isEmpty());
        assertTrue(giftCertificateDAO.findByValueOrderBySortValue("name", "name", "sdfgsfdg").isEmpty());
    }
}