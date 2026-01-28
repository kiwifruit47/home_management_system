package validation;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.cscb525.entity.Company;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import jakarta.validation.ConstraintViolation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CompanyTest {

    private List<String> validate(Company company) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        return validator.validate(company)
                .stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());
    }

    @Test
    public void whenBlankCompanyName_thenAssertConstraintViolations() {
        Company company = new Company("");

        List<String> messages = validate(company);

        assertEquals(2, messages.size());
        assertTrue(messages.contains("Company name cannot be blank"));
    }

    @Test
    public void whenInvalidCompanyName_thenAssertConstraintViolations() {
        Company company = new Company("company");

        List<String> messages = validate(company);

        assertEquals(1, messages.size());
        assertTrue(messages.contains("Company name must start with a capital letter and consist only of letters, numbers and spaces"));
    }


}
