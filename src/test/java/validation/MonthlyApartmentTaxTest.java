package validation;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.cscb525.entity.Apartment;
import org.cscb525.entity.MonthlyApartmentTax;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MonthlyApartmentTaxTest {
    private List<String> validate(MonthlyApartmentTax tax) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        return validator.validate(tax)
                .stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());
    }

    @Test
    public void whenNullMonth_thenAssertConstraintViolations() {
        Apartment apartment = Mockito.mock(Apartment.class);
        MonthlyApartmentTax monthlyApartmentTax = new MonthlyApartmentTax(null, BigDecimal.valueOf(20), apartment);
        List<String> messages = validate(monthlyApartmentTax);

        assertEquals(1, messages.size());
        assertTrue(messages.contains("must not be null"));
    }

    @Test
    public void whenNegativePaymentValue_thenAssertConstraintViolations() {
        Apartment apartment = Mockito.mock(Apartment.class);
        MonthlyApartmentTax monthlyApartmentTax = new MonthlyApartmentTax(YearMonth.now(), BigDecimal.valueOf(-1), apartment);
        List<String> messages = validate(monthlyApartmentTax);

        assertEquals(1, messages.size());
        assertTrue(messages.contains("must be greater than or equal to 0"));
    }

    @Test
    public void whenNullPaymentValue_thenAssertConstraintViolations() {
        Apartment apartment = Mockito.mock(Apartment.class);
        MonthlyApartmentTax monthlyApartmentTax = new MonthlyApartmentTax(YearMonth.now(), null, apartment);
        List<String> messages = validate(monthlyApartmentTax);

        assertEquals(1, messages.size());
        assertTrue(messages.contains("must not be null"));
    }

    @Test
    public void whenNullApartment_thenAssertConstraintViolations() {
        MonthlyApartmentTax monthlyApartmentTax = new MonthlyApartmentTax(YearMonth.now(), BigDecimal.valueOf(20), null);
        List<String> messages = validate(monthlyApartmentTax);

        assertEquals(1, messages.size());
        assertTrue(messages.contains("must not be null"));
    }
}
