package service;

import jakarta.persistence.EntityNotFoundException;
import org.cscb525.config.SessionFactoryUtil;
import org.cscb525.dto.apartment.ApartmentDto;
import org.cscb525.dto.apartment.CreateApartmentDto;
import org.cscb525.dto.occupant.CreateOccupantDto;
import org.cscb525.dto.occupant.OccupantDto;
import org.cscb525.dto.owner.OwnerDto;
import org.cscb525.entity.Apartment;
import org.cscb525.entity.Occupant;
import org.cscb525.exceptions.NotFoundException;
import org.cscb525.service.ApartmentService;
import org.cscb525.service.util.PetUpdateType;
import org.hibernate.Session;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

public class ApartmentServiceIT {
    private final ApartmentService apartmentService = new ApartmentService();

    @BeforeEach
    void setup() {
        TestDataSetup.setupData();
    }

    @AfterEach
    void cleanup() {
        TestDataSetup.clearDB();
    }

    @Test
    public void createApartment_validData_createsApartment() {
        CreateApartmentDto dto = new CreateApartmentDto(
                2,
                202,
                BigDecimal.valueOf(75),
                1,
                1L
        );

        apartmentService.createApartment(dto);

        List<ApartmentDto> apartments = apartmentService.getAllApartments();
        assertTrue(
                apartments.stream()
                        .anyMatch(a -> a.getApartmentNumber() == 202)
        );
    }

    @Test
    public void updateApartmentPetCount_addPet_updatesCount() {
        ApartmentDto updated =
                apartmentService.updateApartmentPetCount(1L, PetUpdateType.ADD);

        assertEquals(1, updated.getPets());
    }

    @Test
    public void updateApartmentPetCount_removePet_belowZero_throwsException() {
        assertThrows(
                IllegalArgumentException.class,
                () -> apartmentService.updateApartmentPetCount(1L, PetUpdateType.REMOVE)
        );
    }

    @Test
    public void addOwnerToApartment_validOwner_addsOwner() {
        OwnerDto owner =
                apartmentService.addOwnerToApartment(1L, 1L);

        assertNotNull(owner);
        assertEquals("Georgi", owner.getName());
    }

    @Test
    public void addOwnerToApartment_invalidApartment_throwsException() {
        assertThrows(
                EntityNotFoundException.class,
                () -> apartmentService.addOwnerToApartment(999L, 1L)
        );
    }

    @Test
    public void removeOwnerFromApartment_existingRelation_removesOwner() {
        apartmentService.addOwnerToApartment(1L, 1L);

        OwnerDto removed =
                apartmentService.removeOwnerFromApartment(1L, 1L);

        assertNotNull(removed);
        assertEquals("Georgi", removed.getName());
    }

    @Test
    public void removeOwnerFromApartment_notAssociated_throwsException() {
        assertThrows(
                IllegalStateException.class,
                () -> apartmentService.removeOwnerFromApartment(1L, 2L)
        );
    }

    @Test
    public void addOccupantToApartment_validData_addsOccupant() {
        CreateOccupantDto dto = new CreateOccupantDto(
                30,
                "Grigor",
                true
        );

        apartmentService.addOccupantToApartment(1L, dto);

        Apartment apartment;
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            apartment = session.get(Apartment.class, 1L);
        }

        assertFalse(apartment.getOccupants().stream().anyMatch(o -> Objects.equals(o.getName(), "Grigor")));
    }

    @Test
    public void addOccupantToApartment_invalidApartment_throwsException() {
        CreateOccupantDto dto = new CreateOccupantDto(
                25,
                "Mihail",
                false
        );

        assertThrows(
                NotFoundException.class,
                () -> apartmentService.addOccupantToApartment(999L, dto)
        );
    }

    @Test
    public void removeOccupantFromApartment_existingOccupant_removesOccupant() {
        CreateOccupantDto dto = new CreateOccupantDto(
                28,
                "Anna",
                true
        );

        apartmentService.addOccupantToApartment(1L, dto);

        Occupant occupant;
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            occupant = session.createQuery(
                    "select o from Occupant o " +
                            "join o.apartment a" +
                            " where a.id=1" +
                            " and o.name='Anna'",
                    Occupant.class)
                    .getSingleResult();
        }

        OccupantDto removed =
                apartmentService.removeOccupantFromApartment(1L, occupant.getId());

        List<Occupant> occupants;
        try(Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            occupants = session.createQuery(
                            "select o from Occupant o " +
                                    "join o.apartment a" +
                                    " where a.id=1" +
                                    " and o.deleted=false",
                            Occupant.class)
                    .getResultList();
        }
        assertFalse(occupants.stream().anyMatch(o -> Objects.equals(o.getName(), "Anna")));


        assertEquals(occupant.getName(), removed.getName());
    }

    @Test
    public void removeOccupantFromApartment_invalidOccupant_throwsException() {
        assertThrows(
                NotFoundException.class,
                () -> apartmentService.removeOccupantFromApartment(1L, 999L)
        );
    }

    @Test
    public void getAllApartments_existingApartments_returnsList() {
        List<ApartmentDto> apartments =
                apartmentService.getAllApartments();

        assertNotNull(apartments);
        assertEquals(50, apartments.size());
    }

    @Test
    public void getApartmentCountByBuilding_existingBuilding_returnsCount() {
        int count =
                apartmentService.getApartmentCountByBuilding(1L);

        assertEquals(3, count);
    }

    @Test
    public void getApartmentCountByBuilding_nullBuilding_returnsZero() {
        int count =
                apartmentService.getApartmentCountByBuilding(999L);

        assertEquals(0, count);
    }

    @Test
    public void getAllApartmentsByBuilding_existingBuilding_returnsList() {
        List<ApartmentDto> apartments =
                apartmentService.getAllApartmentsByBuilding(1L);

        assertNotNull(apartments);
        assertEquals(3, apartments.size());
    }

    @Test
    public void getAllApartmentsByBuilding_nullBuilding_returnsEmptyList() {
        List<ApartmentDto> apartments =
                apartmentService.getAllApartmentsByBuilding(999L);

        assertTrue(apartments.isEmpty());
    }
}
