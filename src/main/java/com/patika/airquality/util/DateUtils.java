package com.patika.airquality.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;

@Component
public class DateUtils
{

    private final LocalDate earliest;

    @Autowired
    public DateUtils(Environment env)
    {
        String s = env.getProperty("app.constraints.earliestDate", "2020-11-27");
        this.earliest = LocalDate.parse(s);
    }

    public DateUtils(String earliestDate)
    {
        this.earliest = LocalDate.parse(earliestDate != null ? earliestDate : "2020-11-27");
    }

    public DateUtils(LocalDate earliestDate)
    {
        this.earliest = earliestDate != null ? earliestDate : LocalDate.parse("2020-11-27");
    }

    public LocalDate defaultStart() { return earliest; }

    public LocalDate defaultEnd() { return LocalDate.now(ZoneOffset.UTC); }

    public void ensureWithinBounds(LocalDate start, LocalDate end)
    {
        if (start.isBefore(earliest)) {
            throw new IllegalArgumentException("startDate cannot be before " + earliest);
        }
        LocalDate today = defaultEnd();
        if (end.isAfter(today)) {
            throw new IllegalArgumentException("endDate cannot be after " + today);
        }
        if (start.isAfter(end)) {
            throw new IllegalArgumentException("startDate cannot be after endDate");
        }
    }

    public long toUnixStartOfDay(LocalDate d)
    {
        return d.atStartOfDay(ZoneOffset.UTC).toEpochSecond();
    }

    public long toUnixEndOfDay(LocalDate d)
    {
        return d.plusDays(1).atStartOfDay(ZoneOffset.UTC).toEpochSecond() - 1;
    }

    public Instant toInstant(LocalDate d)
    {
        return d.atStartOfDay(ZoneOffset.UTC).toInstant();
    }
}

