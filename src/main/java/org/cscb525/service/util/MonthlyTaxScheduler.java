package org.cscb525.service.util;

import org.cscb525.service.MonthlyApartmentTaxService;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MonthlyTaxScheduler {
    private static final ScheduledExecutorService scheduler =
            Executors.newSingleThreadScheduledExecutor();

    public static void start() {
        long initialDelay = computeInitialDelayToNextMidnight();
        long period = TimeUnit.DAYS.toSeconds(1);

        scheduler.scheduleAtFixedRate(
                MonthlyTaxScheduler::runIfFirstDayOfMonth,
                initialDelay,
                period,
                TimeUnit.SECONDS
        );
    }

    private static void runIfFirstDayOfMonth() {
        if (LocalDate.now().getDayOfMonth() == 1) {
            MonthlyApartmentTaxService.generateMonthlyTaxesForCurrentMonth();
        }
    }

    private static long computeInitialDelayToNextMidnight() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nextMidnight = now
                .plusDays(1)
                .withHour(0)
                .withMinute(0)
                .withSecond(0)
                .withNano(0);

        return Duration.between(now, nextMidnight).getSeconds();
    }
}
