package validation;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.cscb525.entity.Company;
import org.cscb525.entity.Employee;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class EmployeeTest {
    private List<String> validate(Employee employee) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        return validator.validate(employee)
                .stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());
    }

    @Test
    public void whenBlankEmployeeName_thenAssertConstraintViolations() {
        Company company = Mockito.mock(Company.class);
        Employee employee = new Employee("", company);

        List<String> messages = validate(employee);

        assertEquals(2, messages.size());
        assertTrue(messages.contains("Employee name cannot be blank"));
    }

    @Test
    public void whenInvalidEmployeeName_thenAssertConstraintViolations() {
        Company company = Mockito.mock(Company.class);
        Employee employee = new Employee("gosho", company);

        List<String> messages = validate(employee);

        assertEquals(1, messages.size());
        assertTrue(messages.contains("Employee name has to start with capital letter and consist only of letters."));
    }

    @Test
    public void whenNullCompany_thenAssertConstraintViolations() {
        Employee employee = new Employee("Company", null);

        List<String> messages =validate(employee);

        assertEquals(1, messages.size());
        assertTrue(messages.contains("must not be null"));
    }
}
