package service;

import org.cscb525.config.SessionFactoryUtil;
import org.cscb525.dto.apartment.CreateApartmentDto;
import org.cscb525.dto.company.CreateCompanyDto;
import org.cscb525.dto.employee.CreateEmployeeDto;
import org.cscb525.dto.occupant.CreateOccupantDto;
import org.cscb525.dto.owner.CreateOwnerDto;
import org.cscb525.service.*;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class TestDataSetup {
    public static void setupData() {
        CompanyService companyService = new CompanyService();

        CreateCompanyDto createCompanyDto1 = new CreateCompanyDto("Total Building Management");
        CreateCompanyDto createCompanyDto2 = new CreateCompanyDto("The Property Managers");
        CreateCompanyDto createCompanyDto3 = new CreateCompanyDto("Citywide Building Services");

        companyService.createCompany(createCompanyDto1);
        companyService.createCompany(createCompanyDto2);
        companyService.createCompany(createCompanyDto3);

        CreateEmployeeDto createEmployeeDto1 = new CreateEmployeeDto("Sofia", 1);
        CreateEmployeeDto createEmployeeDto2 = new CreateEmployeeDto("Alexander", 1);
        CreateEmployeeDto createEmployeeDto3 = new CreateEmployeeDto("Maria", 1);
        CreateEmployeeDto createEmployeeDto4 = new CreateEmployeeDto("Georgi", 2);
        CreateEmployeeDto createEmployeeDto5 = new CreateEmployeeDto("Victoria", 2);
        CreateEmployeeDto createEmployeeDto6 = new CreateEmployeeDto("Kaloyan", 3);

        EmployeeService employeeService = new EmployeeService();

        employeeService.createEmployee(createEmployeeDto1);
        employeeService.createEmployee(createEmployeeDto2);
        employeeService.createEmployee(createEmployeeDto3);
        employeeService.createEmployee(createEmployeeDto4);
        employeeService.createEmployee(createEmployeeDto5);
        employeeService.createEmployee(createEmployeeDto6);

        CreateBuildingRequest createBuildingRequest1 = new CreateBuildingRequest(
                "ul. \"Graf Ignatiev\" 12, 1000 Sofia Center",
                4,
                BigDecimal.valueOf(7),
                BigDecimal.valueOf(5),
                BigDecimal.valueOf(0.05)
        );

        CreateBuildingRequest createBuildingRequest2 = new CreateBuildingRequest(
                "ul. \"Tsar Ivan Shishman\" 24, 1000 Sofia Center",
                4,
                BigDecimal.valueOf(7),
                BigDecimal.valueOf(7),
                BigDecimal.valueOf(0.05)
        );

        CreateBuildingRequest createBuildingRequest3 = new CreateBuildingRequest(
                "ul. \"Georgi S. Rakovski\" 158, 1000 Sofia Center",
                5,
                BigDecimal.valueOf(7),
                BigDecimal.valueOf(5),
                BigDecimal.valueOf(0.05)
        );

        CreateBuildingRequest createBuildingRequest4 = new CreateBuildingRequest(
                "ul. \"Knyaz Boris I\" 88, 1000 Sofia Center",
                5,
                BigDecimal.valueOf(7),
                BigDecimal.valueOf(7),
                BigDecimal.valueOf(0.05)
        );

        CreateBuildingRequest createBuildingRequest5 = new CreateBuildingRequest(
                "ul. \"Aksakov\" 7, 1000 Sofia Center",
                4,
                BigDecimal.valueOf(10),
                BigDecimal.valueOf(7),
                BigDecimal.valueOf(0.05)
        );

        CreateBuildingRequest createBuildingRequest6 = new CreateBuildingRequest(
                "ul. \"Slavishte\" 8, 1421 Lozenets",
                6,
                BigDecimal.valueOf(10),
                BigDecimal.valueOf(7),
                BigDecimal.valueOf(0.05)
        );

        CreateBuildingRequest createBuildingRequest7 = new CreateBuildingRequest(
                "bul. \"James Bourchier\" 23, 1164 Lozenets",
                7,
                BigDecimal.valueOf(10),
                BigDecimal.valueOf(10),
                BigDecimal.valueOf(0.05)
        );

        CreateBuildingRequest createBuildingRequest8 = new CreateBuildingRequest(
                "ul. \"Krum Popov\" 50, 1421 Lozenets",
                10,
                BigDecimal.valueOf(10),
                BigDecimal.valueOf(10),
                BigDecimal.valueOf(0.01)
        );

        CreateBuildingRequest createBuildingRequest9 = new CreateBuildingRequest(
                "ul. \"Buntovnik\" 5, 1421 Lozenets",
                8,
                BigDecimal.valueOf(10),
                BigDecimal.valueOf(5),
                BigDecimal.valueOf(0.01)
        );

        CreateBuildingRequest createBuildingRequest10 = new CreateBuildingRequest(
                "ul. \"Buntovnik\" 10, 1426 Lozenets",
                8,
                BigDecimal.valueOf(10),
                BigDecimal.valueOf(5),
                BigDecimal.valueOf(0.01)
        );

        CreateBuildingRequest createBuildingRequest11 = new CreateBuildingRequest(
                "ul. \"Balsha\" 1, 1408 Ivan Vazov",
                6,
                BigDecimal.valueOf(10),
                BigDecimal.valueOf(5),
                BigDecimal.valueOf(0.01)
        );

        CreateBuildingRequest createBuildingRequest12 = new CreateBuildingRequest(
                "bul. \"Aleksandar Malinov\" 16, 1712 Mladost 3",
                12,
                BigDecimal.valueOf(7),
                BigDecimal.valueOf(5),
                BigDecimal.valueOf(0.01)
        );

        CreateBuildingRequest createBuildingRequest13 = new CreateBuildingRequest(
                "jk. Mladost 1, bl. 15, 1784 Mladost 1",
                8,
                BigDecimal.valueOf(10),
                BigDecimal.valueOf(5),
                BigDecimal.valueOf(0.01)
        );

        CreateBuildingRequest createBuildingRequest14 = new CreateBuildingRequest(
                "ul. \"Nikolay Haytov\" 2, 1172 Dianabad",
                10,
                BigDecimal.valueOf(7),
                BigDecimal.valueOf(5),
                BigDecimal.valueOf(0.05)
        );

        CreateBuildingRequest createBuildingRequest15 = new CreateBuildingRequest(
                "ul. \"Edison\" 33, 1111 Geo Milev",
                4,
                BigDecimal.valueOf(10),
                BigDecimal.valueOf(5),
                BigDecimal.valueOf(0.05)
        );

        CreateBuildingRequest createBuildingRequest16 = new CreateBuildingRequest(
                "ul. \"Lui Ayer\" bl. 113, 1404 Gotse Delchev",
                9,
                BigDecimal.valueOf(10),
                BigDecimal.valueOf(5),
                BigDecimal.valueOf(0.05)
        );

        CreateBuildingRequest createBuildingRequest17 = new CreateBuildingRequest(
                "ul. \"Kumata\" 12, 1616 Boyana",
                6,
                BigDecimal.valueOf(12),
                BigDecimal.valueOf(10),
                BigDecimal.valueOf(0.10)
        );

        CreateBuildingRequest createBuildingRequest18 = new CreateBuildingRequest(
                "ul. \"Zografski Manastir\" 15, 1415 Dragalevtsi",
                10,
                BigDecimal.valueOf(15),
                BigDecimal.valueOf(10),
                BigDecimal.valueOf(0.01)
        );

        CreateBuildingRequest createBuildingRequest19 = new CreateBuildingRequest(
                "ul. \"Ralevitca\" 98, 1618 Manastirski Livadi",
                8,
                BigDecimal.valueOf(10),
                BigDecimal.valueOf(10),
                BigDecimal.valueOf(0.05)
        );

        CreateBuildingRequest createBuildingRequest20 = new CreateBuildingRequest(
                "ul. \"Simeonovsko Shose\" 110, 1700 Vitosha District",
                8,
                BigDecimal.valueOf(12),
                BigDecimal.valueOf(5),
                BigDecimal.valueOf(0.01)
        );

        CreateBuildingRequest createBuildingRequest21 = new CreateBuildingRequest(
                "jk. Lyulin 3, bl. 320, 1336 Lyulin 3",
                14,
                BigDecimal.valueOf(6),
                BigDecimal.valueOf(5),
                BigDecimal.valueOf(0.01)
        );

        companyService.signNewContractForBuilding(1, createBuildingRequest1);
        companyService.signNewContractForBuilding(1, createBuildingRequest2);
        companyService.signNewContractForBuilding(1, createBuildingRequest3);
        companyService.signNewContractForBuilding(1, createBuildingRequest4);
        companyService.signNewContractForBuilding(1, createBuildingRequest5);
        companyService.signNewContractForBuilding(1, createBuildingRequest6);
        companyService.signNewContractForBuilding(1, createBuildingRequest7);
        companyService.signNewContractForBuilding(1, createBuildingRequest8);
        companyService.signNewContractForBuilding(1, createBuildingRequest9);
        companyService.signNewContractForBuilding(2, createBuildingRequest10);
        companyService.signNewContractForBuilding(2, createBuildingRequest11);
        companyService.signNewContractForBuilding(2, createBuildingRequest12);
        companyService.signNewContractForBuilding(2, createBuildingRequest13);
        companyService.signNewContractForBuilding(2, createBuildingRequest14);
        companyService.signNewContractForBuilding(2, createBuildingRequest15);
        companyService.signNewContractForBuilding(2, createBuildingRequest16);
        companyService.signNewContractForBuilding(3, createBuildingRequest17);
        companyService.signNewContractForBuilding(3, createBuildingRequest18);
        companyService.signNewContractForBuilding(3, createBuildingRequest19);
        companyService.signNewContractForBuilding(3, createBuildingRequest20);
        companyService.signNewContractForBuilding(3, createBuildingRequest21);


        List<CreateApartmentDto> apartments = new ArrayList<>();

        int apartmentNumber = 1;

        for (int i = 0; i < 50; i++) {
            int floor = (i % 10) + 1;
            BigDecimal area = BigDecimal.valueOf(60 + i);
            int pets = i % 3;
            long buildingId = (i % 21) + 1;

            apartments.add(
                    new CreateApartmentDto(
                            floor,
                            apartmentNumber++,
                            area,
                            pets,
                            buildingId
                    )
            );
        }

        ApartmentService apartmentService = new ApartmentService();

        for (CreateApartmentDto apartment : apartments) {
            apartmentService.createApartment(apartment);
        }

        OwnerService ownerService = new OwnerService();
        String[] names = {"Georgi", "Ivan", "Dimitar", "Nikolay", "Petar", "Aleksandar", "Hristo", "Stefan", "Yordan", "Vasil", "Todor", "Stoyan", "Atanas", "Angel", "Krasimir", "Plamen", "Ivaylo", "Martin", "Valentin", "Rumen", "Kiril", "Iliya", "Asen", "Boris", "Kaloyan", "Nikola", "Daniel", "Viktor", "Pavel", "Simeon", "Maria", "Elena", "Ivanka", "Yordanka", "Daniela", "Desislava", "Petya", "Rositsa", "Gergana", "Penka", "Margarita", "Violeta", "Nadezhda", "Silviya", "Emiliya", "Aleksandra", "Rumyana", "Radka", "Milena", "Viktoria", "Svetlana", "Nikol", "Sofia", "Anna", "Snezhana", "Tsvetelina", "Raya", "Katya", "Galina", "Teodora"};
        for (String name : names) {
            ownerService.createOwner(new CreateOwnerDto(name));
        }

        for (int i = 1; i < names.length; i++) {
            long apartmentId = (i - 1) % apartments.size() + 1;
            apartmentService.addOwnerToApartment(apartmentId, i);
        }

        List<CreateOccupantDto> occupants = new ArrayList<>();

        String[] occupantNames = {"Mihail", "Stanislav", "Miroslav", "Borislav", "Lyubomir", "Radoslav", "Boyan", "Tsvetan", "Konstantin", "Vladimir", "Marin", "Blagovest", "Nayden", "Samuil", "Bogdan", "Sava", "Grigor", "Kamen", "Iliyan", "Evgeni", "Grozdan", "Parvan", "Velyo", "Prodan", "Yavor", "Yasen", "Zlatin", "Kosta", "Damyan", "Neofit", "Zhelyazko", "Mihail", "Andrey", "Dragomir", "Asparuh", "Tervel", "Krum", "Zahari", "Zdravko", "Zhivko", "Mladen", "Ognyan", "Hristiyan", "Delyan", "Genadi", "Ekaterina", "Mariyka", "Galina", "Valentina", "Gabriela", "Simona", "Yoana", "Daria", "Tanya", "Iva", "Boryana", "Albena", "Svetla", "Aneliya", "Denitsa", "Kameliya", "Siana", "Stanka", "Hristina", "Zlatka", "Biliana", "Radka", "Milena", "Viktoriya", "Svetlana", "Nikolina", "Raina", "Boyka", "Iglika", "Iskra", "Elitsa", "Kalina", "Ralitsa", "Vessela", "Temenuzhka", "Tsveta", "Todorka", "Vasilka", "Zornitsa", "Ognyana", "Blaguna", "Fidanka", "Evdokiya", "Aneta", "Bisera"};
        for (int i = 0, j = 0; i < 90; i++, j+=5) {
            String name = occupantNames[i];
            boolean usesElevator = i % 10 != 0;

            occupants.add(
                    new CreateOccupantDto(j, name, usesElevator)
            );
        }

        long apartmentCount = 50;

        for (int i = 0; i < occupants.size(); i++) {
            long apartmentId = (i % apartmentCount) + 1;
            apartmentService.addOccupantToApartment(apartmentId, occupants.get(i));
        }

        MonthlyApartmentTaxService.generateMonthlyTaxesForCurrentMonth();
    }

    public static void clearDB() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.createMutationQuery("delete from MonthlyApartmentTax").executeUpdate();
            session.createMutationQuery("delete from Owner").executeUpdate();
            session.createMutationQuery("delete from Occupant").executeUpdate();
            session.createMutationQuery("delete from Apartment").executeUpdate();
            session.createMutationQuery("delete from Building").executeUpdate();
            session.createMutationQuery("delete from Employee ").executeUpdate();
            session.createMutationQuery("delete from Company").executeUpdate();
            transaction.commit();
        }
    }
}
