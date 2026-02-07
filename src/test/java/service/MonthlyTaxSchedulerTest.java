package service;

import org.cscb525.service.MonthlyApartmentTaxService;
import org.cscb525.service.util.MonthlyTaxScheduler;
import org.junit.jupiter.api.Test;

import java.time.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class MonthlyTaxSchedulerTest {

    private Clock fixedClock(LocalDateTime dateTime) {
        return Clock.fixed(
                dateTime.toInstant(ZoneOffset.UTC),
                ZoneOffset.UTC
        );
    }

    @Test
    void runIfFirstDayOfMonth_firstDay_executesTaxGeneration() {
        MonthlyApartmentTaxService taxService = mock(MonthlyApartmentTaxService.class);
        Clock clock = fixedClock(LocalDateTime.of(2026, 2, 1, 10, 0));

        MonthlyTaxScheduler scheduler =
                new MonthlyTaxScheduler(taxService, clock);

        scheduler.runIfFirstDayOfMonth();

        verify(taxService, times(1))
                .generateMonthlyTaxesForCurrentMonth();
    }

    @Test
    void runIfFirstDayOfMonth_notFirstDay_doesNothing() {
        MonthlyApartmentTaxService taxService = mock(MonthlyApartmentTaxService.class);
        Clock clock = fixedClock(LocalDateTime.of(2026, 2, 15, 10, 0));

        MonthlyTaxScheduler scheduler =
                new MonthlyTaxScheduler(taxService, clock);

        scheduler.runIfFirstDayOfMonth();

        verify(taxService, never())
                .generateMonthlyTaxesForCurrentMonth();
    }

    @Test
    void computeInitialDelayToNextMidnight_returnsCorrectSeconds() {
        MonthlyApartmentTaxService taxService = mock(MonthlyApartmentTaxService.class);

        Clock clock = fixedClock(
                LocalDateTime.of(2026, 2, 10, 23, 0)
        );

        MonthlyTaxScheduler scheduler =
                new MonthlyTaxScheduler(taxService, clock);

        long delay = scheduler.computeInitialDelayToNextMidnight();

        assertEquals(3600, delay);
    }

}