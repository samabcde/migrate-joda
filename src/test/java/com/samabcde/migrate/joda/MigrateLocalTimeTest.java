package com.samabcde.migrate.joda;

import org.joda.time.DateTimeFieldType;
import org.joda.time.DateTimeUtils;
import org.joda.time.DateTimeZone;
import org.joda.time.DurationFieldType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.DefaultLocale;
import org.junitpioneer.jupiter.DefaultTimeZone;

import java.time.Clock;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.TimeZone;

import static com.samabcde.migrate.joda.JodaJavaAssertions.assertJodaEqualsJava;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DefaultLocale("en-US")
@DefaultTimeZone("UTC")
public class MigrateLocalTimeTest {
    private Clock clock;
    private final TimeZone testTimeZone = TimeZone.getTimeZone("Iran");
    private final DateTimeZone jodaDateTimeZone = DateTimeZone.forTimeZone(testTimeZone);
    private final ZoneId javaZoneId = testTimeZone.toZoneId();

    @BeforeEach
    void setup() {
        DateTimeUtils.setCurrentMillisFixed(-767804160000L);
        clock = Clock.fixed(java.time.Instant.ofEpochMilli(-767804160000L), ZoneOffset.UTC);
    }

    @Test
    void construction() {
        assertJodaEqualsJava(new org.joda.time.LocalTime(), java.time.LocalTime.now(clock));
        assertJodaEqualsJava(new org.joda.time.LocalTime(15, 55, 3), java.time.LocalTime.of(15, 55, 3));
        assertJodaEqualsJava(new org.joda.time.LocalTime(18, 36, 3, 4), java.time.LocalTime.of(18, 36, 3, 4000000));
        assertJodaEqualsJava(org.joda.time.LocalTime.parse("05:10"), java.time.LocalTime.parse("05:10"));
        assertJodaEqualsJava(org.joda.time.LocalTime.now(), java.time.LocalTime.now(clock));
    }

    @Test
    void api() {
        org.joda.time.LocalTime jodaLocalTime = new org.joda.time.LocalTime(6, 24, 0);
        java.time.LocalTime javaLocalTime = java.time.LocalTime.of(6, 24, 0);
        assertEquals(jodaLocalTime.isSupported(DateTimeFieldType.dayOfMonth()), javaLocalTime.isSupported(ChronoField.DAY_OF_MONTH));
        assertEquals(jodaLocalTime.isSupported(DurationFieldType.millis()), javaLocalTime.isSupported(ChronoField.MILLI_OF_SECOND));
        assertEquals(jodaLocalTime.isSupported(DurationFieldType.months()), javaLocalTime.isSupported(ChronoUnit.MONTHS));
        assertEquals(jodaLocalTime.isSupported(DurationFieldType.hours()), javaLocalTime.isSupported(ChronoUnit.HOURS));
        assertEquals(jodaLocalTime.get(DateTimeFieldType.hourOfDay()), javaLocalTime.get(ChronoField.HOUR_OF_DAY));
    }

    @Test
    void conversion() {
        org.joda.time.LocalTime jodaLocalTime = new org.joda.time.LocalTime(12, 30, 0);
        java.time.LocalTime javaLocalTime = java.time.LocalTime.of(12, 30, 0);
        assertJodaEqualsJava(jodaLocalTime.toDateTimeToday(jodaDateTimeZone), javaLocalTime.atDate(java.time.LocalDate.now(clock)).atZone(javaZoneId));
        assertJodaEqualsJava(jodaLocalTime.toDateTimeToday(), javaLocalTime.atDate(java.time.LocalDate.now(clock)).atZone(ZoneId.systemDefault()));
        assertJodaEqualsJava(jodaLocalTime.toDateTime(org.joda.time.Instant.now()), (java.time.ZonedDateTime.now(clock).with(javaLocalTime)));
    }

    @Test
    void format() {
        org.joda.time.LocalTime jodaLocalTime = new org.joda.time.LocalTime(23, 59, 59, 10);
        java.time.LocalTime javaLocalTime = java.time.LocalTime.of(23, 59, 59, 10000000);
        // DateTimeFormatter.ISO_LOCAL_TIME will skip 0 of nanosecond
        assertEquals(jodaLocalTime.toString(), javaLocalTime.format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss.SSS")));
    }
}
