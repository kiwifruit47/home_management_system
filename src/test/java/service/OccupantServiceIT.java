package service;

import org.cscb525.config.SessionFactoryUtil;
import org.cscb525.dto.occupant.CreateOccupantDto;
import org.cscb525.dto.occupant.OccupantDto;
import org.cscb525.dto.occupant.UpdateOccupantDto;
import org.cscb525.exceptions.EmptyListException;
import org.cscb525.service.ApartmentService;
import org.cscb525.service.OccupantService;
import org.hibernate.Session;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class OccupantServiceIT {
    private final ApartmentService apartmentService = new ApartmentService();
    private final OccupantService occupantService = new OccupantService();

    @BeforeEach
    void setup() {
        TestDataSetup.setupData();
    }

    @AfterEach
    void cleanup () {
        TestDataSetup.clearDB();
    }

    private long createOccupantAndReturnId(String name, int age) {
        CreateOccupantDto dto = new CreateOccupantDto(age, name, true);
        apartmentService.addOccupantToApartment(1L, dto);

        long id;
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            id = session.createQuery(
                    "select o.id from Occupant o join o.apartment a where a.id = 1 and o.name = :name",
                    Long.class)
                    .setParameter("name", name)
                    .getSingleResult();
        }
        return id;
    }

    @Test
    public void updateOccupant_validData_updatesOccupant() {
        long occupantId = createOccupantAndReturnId("Petar", 30);

        UpdateOccupantDto updateDto =
                new UpdateOccupantDto(occupantId, 35, "Pesho", false);

        occupantService.updateOccupant(updateDto);

        OccupantDto updated = occupantService.getOccupantById(occupantId);

        assertEquals("Pesho", updated.getName());
        assertEquals(35, updated.getAge());
    }

    @Test
    public void getOccupantById_existingOccupant_returnsOccupant() {
        long occupantId = createOccupantAndReturnId("Anna", 28);

        OccupantDto occupant =
                occupantService.getOccupantById(occupantId);

        assertEquals("Anna", occupant.getName());
        assertEquals(28, occupant.getAge());
    }

    @Test
    public void getAllOccupants_existingOccupants_returnsList() {
        List<OccupantDto> occupants =
                occupantService.getAllOccupants();

        assertNotNull(occupants);
        assertEquals(90, occupants.size());
    }

    @Test
    public void getAllOccupants_noOccupants_throwsException() {
        cleanup();
        assertThrows(
                EmptyListException.class,
                occupantService::getAllOccupants
        );
    }

    @Test
    public void getAllOccupantsInBuildingOrderedAlphabetically_success() {
        List<OccupantDto> occupants =
                occupantService.getAllOccupantsInBuildingOrderedAlphabetically(1L);

        assertEquals("Hristiyan", occupants.get(0).getName());
        assertEquals("Mihail", occupants.get(1).getName());
        assertEquals("Parvan", occupants.get(2).getName());
        assertEquals("Raina", occupants.get(3).getName());
        assertEquals("Simona", occupants.get(4).getName());

    }

    @Test
    public void getAllOccupantsInBuildingOrderedAlphabetically_empty_throwsException() {
        assertThrows(
                EmptyListException.class,
                () -> occupantService.getAllOccupantsInBuildingOrderedAlphabetically(999L)
        );
    }

    @Test
    public void getAllOccupantsInBuildingOrderedByAge_ordersCorrectly() {
        List<OccupantDto> occupants =
                occupantService.getAllOccupantsInBuildingOrderedByAge(1L);

        for (int i = 1; i < occupants.size(); i++) {
            int previousAge = occupants.get(i - 1).getAge();
            int currentAge = occupants.get(i).getAge();

            assertTrue(previousAge >= currentAge);
        }
    }

    @Test
    public void getAllOccupantsInBuildingOrderedByAge_empty_throwsException() {
        assertThrows(
                EmptyListException.class,
                () -> occupantService.getAllOccupantsInBuildingOrderedByAge(999L)
        );
    }

    @Test
    public void getOccupantCountInBuilding_existingOccupants_returnsCount() {

        int count = occupantService.getOccupantCountInBuilding(1L);

        assertEquals(5, count);
    }

    @Test
    public void getOccupantCountInBuilding_noOccupants_returnsZero() {
        int count =
                occupantService.getOccupantCountInBuilding(999L);

        assertEquals(0, count);
    }
}
