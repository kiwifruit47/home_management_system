package validation;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.cscb525.entity.Owner;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class OwnerTest {
    private List<String> validate(Owner owner) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        return validator.validate(owner)
                .stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());
    }

    @Test
    public void whenBlankName_thenAssertConstraintViolations() {
        Owner owner = new Owner("");
        List<String> messages = validate(owner);

        assertEquals(1, messages.size());
        assertTrue(messages.contains("Owner name cannot be blank"));
    }
}
