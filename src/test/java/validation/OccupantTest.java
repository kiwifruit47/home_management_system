package validation;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.cscb525.entity.Apartment;
import org.cscb525.entity.Occupant;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class OccupantTest {
    private List<String> validate(Occupant occupant) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        return validator.validate(occupant)
                .stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());
    }

    @Test
    public void whenNegativeAge_thenAssertConstraintViolations() {
        Apartment apartment = Mockito.mock(Apartment.class);
        Occupant occupant = new Occupant(-1, "gosho", false, apartment);
        List<String> messages = validate(occupant);

        assertEquals(1, messages.size());
        assertTrue(messages.contains("must be greater than or equal to 0"));
    }

    @Test
    public void whenBlankName_thenAssertConstraintViolations() {
        Apartment apartment = Mockito.mock(Apartment.class);
        Occupant occupant = new Occupant(18, "", false, apartment);
        List<String> messages = validate(occupant);

        assertEquals(1, messages.size());
        assertTrue(messages.contains("Occupant name cannot be blank"));
    }

    @Test
    public void whenNullApartment_thenAssertConstraintViolations() {
        Occupant occupant = new Occupant(18, "gosho", false, null);
        List<String> messages = validate(occupant);

        assertEquals(1, messages.size());
        assertTrue(messages.contains("must not be null"));
    }
}
