package validation;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.cscb525.entity.Apartment;
import org.cscb525.entity.Building;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ApartmentTest {
    private List<String> validate(Apartment apartment) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        return validator.validate(apartment)
                .stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());
    }

    @Test
    public void whenNegativeFloor_thenAssertConstraintViolations() {
        Building building = Mockito.mock(Building.class);
        Apartment apartment = new Apartment(-2, 1, BigDecimal.valueOf(100), 0, building);

        List<String> messages = validate(apartment);

        assertEquals(1, messages.size());
        assertTrue(messages.contains("must be greater than 0"));
    }

    @Test
    public void whenNegativeApartmentNumber_thenAssertConstraintViolations() {
        Building building = Mockito.mock(Building.class);
        Apartment apartment = new Apartment(1, -1, BigDecimal.valueOf(100), 0, building);

        List<String> messages = validate(apartment);

        assertEquals(1, messages.size());
        assertTrue(messages.contains("must be greater than 0"));
    }

    @Test
    public void whenNegativeArea_thenAssertConstraintViolations() {
        Building building = Mockito.mock(Building.class);
        Apartment apartment = new Apartment(1, 1, BigDecimal.valueOf(-100), 0, building);

        List<String> messages = validate(apartment);

        assertEquals(1, messages.size());
        assertTrue(messages.contains("must be greater than 0"));
    }

    @Test
    public void whenNullArea_thenAssertConstraintViolations() {
        Building building = Mockito.mock(Building.class);
        Apartment apartment = new Apartment(1, 1, null, 0, building);

        List<String> messages = validate(apartment);

        assertEquals(1, messages.size());
        assertTrue(messages.contains("must not be null"));
    }

    @Test
    public void whenNegativePets_thenAssertConstraintViolations() {
        Building building = Mockito.mock(Building.class);
        Apartment apartment = new Apartment(1, 1, BigDecimal.valueOf(100), -1, building);

        List<String> messages = validate(apartment);

        assertEquals(1, messages.size());
        assertTrue(messages.contains("must be greater than or equal to 0"));
    }

}
