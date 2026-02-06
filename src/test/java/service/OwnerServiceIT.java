package service;

import org.cscb525.config.SessionFactoryUtil;
import org.cscb525.dto.owner.CreateOwnerDto;
import org.cscb525.dto.owner.OwnerDto;
import org.cscb525.exceptions.EmptyListException;
import org.cscb525.service.OwnerService;
import org.hibernate.Session;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class OwnerServiceIT {
    private final OwnerService ownerService = new OwnerService();

    @BeforeEach
    void setup() {
        TestDataSetup.setupData();
    }

    @AfterEach
    void cleanup() {
        TestDataSetup.clearDB();
    }

    private long createOwnerAndReturnId(String name) {
        ownerService.createOwner(new CreateOwnerDto(name));

        long id;
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            id = session.createQuery(
                            "select o.id from Owner o where o.name = :name",
                            Long.class)
                    .setParameter("name", name)
                    .getSingleResult();
        }
        return id;
    }

    @Test
    public void createOwner_validOwner_createsOwner() {
        ownerService.createOwner(new CreateOwnerDto("Test Owner"));

        List<OwnerDto> owners = ownerService.getAllOwners();

        assertTrue(
                owners.stream().anyMatch(o -> o.getName().equals("Test Owner"))
        );
    }

    @Test
    public void updateOwnerName_existingOwner_updatesName() {
        long ownerId = createOwnerAndReturnId("Old Name");

        OwnerDto updated =
                ownerService.updateOwnerName(ownerId, "New Name");

        assertEquals("New Name", updated.getName());
    }

    @Test
    public void getOwnerById_existingOwner_returnsOwner() {
        long ownerId = createOwnerAndReturnId("Test Owner");

        OwnerDto owner =
                ownerService.getOwnerById(ownerId);

        assertEquals("Test Owner", owner.getName());
    }

    @Test
    public void getAllOwners_existingOwners_returnsList() {
        List<OwnerDto> owners =
                ownerService.getAllOwners();

        assertNotNull(owners);
        assertEquals(60, owners.size());
    }

    @Test
    public void getAllOwners_noOwners_throwsException() {
        cleanup();
        assertThrows(
                EmptyListException.class,
                ownerService::getAllOwners
        );
    }

    @Test
    public void getAllApartmentOwners_existingOwners_returnsOwners() {
        List<OwnerDto> owners =
                ownerService.getAllApartmentOwners(1L);

        assertEquals(2, owners.size());
    }

    @Test
    public void getAllApartmentOwners_noOwners_throwsException() {
        assertThrows(
                EmptyListException.class,
                () -> ownerService.getAllApartmentOwners(999L)
        );
    }
}
