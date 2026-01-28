package dao.owner;

import org.cscb525.config.SessionFactoryUtil;
import org.cscb525.dao.OwnerDao;
import org.cscb525.dto.owner.CreateOwnerDto;
import org.cscb525.entity.*;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OwnerCreateIT {

    @AfterEach
    void cleanDB() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.createMutationQuery("delete from Owner").executeUpdate();
            tx.commit();
        }
    }

    @Test
    public void createOwner_success() {
        CreateOwnerDto dto = new CreateOwnerDto("Misho");

        OwnerDao.createOwner(dto);

        Owner owner;
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            owner = session.createQuery("from Owner ", Owner.class)
                    .getSingleResult();
        }

        assertEquals("Misho", owner.getName());
    }
}
