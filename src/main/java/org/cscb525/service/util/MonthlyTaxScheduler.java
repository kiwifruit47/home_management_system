package org.cscb525.service.util;

import org.cscb525.service.MonthlyApartmentTaxService;

import java.time.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MonthlyTaxScheduler {

    private final ScheduledExecutorService scheduler;
    private final MonthlyApartmentTaxService taxService;
    private final Clock clock;

    public MonthlyTaxScheduler(
            MonthlyApartmentTaxService taxService,
            Clock clock
    ) {
        this.scheduler = Executors.newSingleThreadScheduledExecutor();
        this.taxService = taxService;
        this.clock = clock;
    }

    public void start() {
        long initialDelay = computeInitialDelayToNextMidnight();
        long period = TimeUnit.DAYS.toSeconds(1);

        scheduler.scheduleAtFixedRate(
                this::runIfFirstDayOfMonth,
                initialDelay,
                period,
                TimeUnit.SECONDS
        );
    }

    public void runIfFirstDayOfMonth() {
        if (LocalDate.now(clock).getDayOfMonth() == 1) {
            taxService.generateMonthlyTaxesForCurrentMonth();
        }
    }

    public long computeInitialDelayToNextMidnight() {
        LocalDateTime now = LocalDateTime.now(clock);
        LocalDateTime nextMidnight = now
                .plusDays(1)
                .withHour(0)
                .withMinute(0)
                .withSecond(0)
                .withNano(0);

        return Duration.between(now, nextMidnight).getSeconds();
    }
}
