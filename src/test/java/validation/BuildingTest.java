package validation;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.cscb525.entity.Building;
import org.cscb525.entity.Employee;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BuildingTest {
    private List<String> validate(Building building) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        return validator.validate(building)
                .stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());
    }

    @Test
    public void whenBlankAddress_thenAssertConstraintViolations() {
        Employee employee = Mockito.mock(Employee.class);
        Building building = new Building("", 8, BigDecimal.valueOf(10), BigDecimal.valueOf(10), BigDecimal.valueOf(0.01), employee);

        List<String> messages = validate(building);

        assertEquals(1, messages.size());
        assertTrue(messages.contains("Address cannot be blank"));
    }

    @Test
    public void whenNegativeFloors_thenAssertConstraintViolations() {
        Employee employee = Mockito.mock(Employee.class);
        Building building = new Building("ul. nqkoq 100", -8, BigDecimal.valueOf(10), BigDecimal.valueOf(10), BigDecimal.valueOf(0.01), employee);

        List<String> messages = validate(building);

        assertEquals(1, messages.size());
        assertTrue(messages.contains("must be greater than 0"));
    }

    @Test
    public void whenNegativePersonTax_thenAssertConstraintViolations() {
        Employee employee = Mockito.mock(Employee.class);
        Building building = new Building("ul. nqkoq 100", 8, BigDecimal.valueOf(-10), BigDecimal.valueOf(10), BigDecimal.valueOf(0.01), employee);

        List<String> messages = validate(building);

        assertEquals(1, messages.size());
        assertTrue(messages.contains("must be greater than 0"));
    }

    @Test
    public void whenNullPersonTax_thenAssertConstraintViolations() {
        Employee employee = Mockito.mock(Employee.class);
        Building building = new Building("ul. nqkoq 100", 8, null, BigDecimal.valueOf(10), BigDecimal.valueOf(0.01), employee);

        List<String> messages = validate(building);

        assertEquals(1, messages.size());
        assertTrue(messages.contains("must not be null"));
    }

    @Test
    public void whenNegativePetTax_thenAssertConstraintViolations() {
        Employee employee = Mockito.mock(Employee.class);
        Building building = new Building("ul. nqkoq 100", 8, BigDecimal.valueOf(10), BigDecimal.valueOf(-10), BigDecimal.valueOf(0.01), employee);

        List<String> messages = validate(building);

        assertEquals(1, messages.size());
        assertTrue(messages.contains("must be greater than 0"));
    }

    @Test
    public void whenNullPetTax_thenAssertConstraintViolations() {
        Employee employee = Mockito.mock(Employee.class);
        Building building = new Building("ul. nqkoq 100", 8, BigDecimal.valueOf(10), null, BigDecimal.valueOf(0.01), employee);

        List<String> messages = validate(building);

        assertEquals(1, messages.size());
        assertTrue(messages.contains("must not be null"));
    }

    @Test
    public void whenNegativeM2Tax_thenAssertConstraintViolations() {
        Employee employee = Mockito.mock(Employee.class);
        Building building = new Building("ul. nqkoq 100", 8, BigDecimal.valueOf(10), BigDecimal.valueOf(10), BigDecimal.valueOf(-0.01), employee);

        List<String> messages = validate(building);

        assertEquals(1, messages.size());
        assertTrue(messages.contains("must be greater than 0"));
    }

    @Test
    public void whenNullM2Tax_thenAssertConstraintViolations() {
        Employee employee = Mockito.mock(Employee.class);
        Building building = new Building("ul. nqkoq 100", 8, BigDecimal.valueOf(10), BigDecimal.valueOf(10), null, employee);

        List<String> messages = validate(building);

        assertEquals(1, messages.size());
        assertTrue(messages.contains("must not be null"));
    }

    @Test
    public void whenNullEmployee_thenAssertConstraintViolations() {
        Building building = new Building("ul. nqkoq 100", 8, BigDecimal.valueOf(10), BigDecimal.valueOf(10), BigDecimal.valueOf(0.01), null);

        List<String> messages = validate(building);

        assertEquals(1, messages.size());
        assertTrue(messages.contains("must not be null"));
    }
}
