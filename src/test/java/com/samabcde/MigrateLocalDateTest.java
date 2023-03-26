package com.samabcde;

import org.joda.time.DateTimeFieldType;
import org.joda.time.DateTimeUtils;
import org.joda.time.DateTimeZone;
import org.joda.time.DurationFieldType;
import org.joda.time.LocalTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.DefaultTimeZone;

import java.time.Clock;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.TimeZone;

import static com.samabcde.JodaJavaAssertions.assertJodaEqualsJava;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DefaultTimeZone("UTC")
public class MigrateLocalDateTest {
    private Clock clock;

    private final TimeZone testTimeZone = TimeZone.getTimeZone("Asia/Abidjan");
    private final DateTimeZone jodaDateTimeZone = DateTimeZone.forTimeZone(testTimeZone);
    private final ZoneId javaZoneId = testTimeZone.toZoneId();

    @BeforeEach
    void setup() {
        DateTimeUtils.setCurrentMillisFixed(612921600000L);
        clock = Clock.fixed(java.time.Instant.ofEpochMilli(612921600000L), ZoneOffset.UTC);
    }

    @Test
    void construction() {
        assertJodaEqualsJava(new org.joda.time.LocalDate(), java.time.LocalDate.now(clock));
        assertJodaEqualsJava(new org.joda.time.LocalDate(1914, 7, 28), java.time.LocalDate.of(1914, 7, 28));
        assertJodaEqualsJava(org.joda.time.LocalDate.parse("1914-11-11"), java.time.LocalDate.parse("1914-11-11"));
        assertJodaEqualsJava(org.joda.time.LocalDate.now(), java.time.LocalDate.now(clock));
    }

    @Test
    void api() {
        org.joda.time.LocalDate jodaLocalDate = new org.joda.time.LocalDate(1939, 9, 1);
        java.time.LocalDate javaLocalDate = java.time.LocalDate.of(1939, 9, 1);
        assertEquals(jodaLocalDate.getCenturyOfEra(), javaLocalDate.get(ChronoField.YEAR_OF_ERA) / 100);
        assertEquals(jodaLocalDate.isSupported(DateTimeFieldType.dayOfMonth()), javaLocalDate.isSupported(ChronoField.DAY_OF_MONTH));
        assertEquals(jodaLocalDate.isSupported(DurationFieldType.millis()), javaLocalDate.isSupported(ChronoField.MILLI_OF_SECOND));
        assertEquals(jodaLocalDate.isSupported(DurationFieldType.months()), javaLocalDate.isSupported(ChronoUnit.MONTHS));
        assertEquals(jodaLocalDate.isSupported(DurationFieldType.hours()), javaLocalDate.isSupported(ChronoUnit.HOURS));
        assertEquals(jodaLocalDate.get(DateTimeFieldType.dayOfYear()), javaLocalDate.get(ChronoField.DAY_OF_YEAR));
        assertEquals(jodaLocalDate.toDate(), Date.from(javaLocalDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
    }

    @Test
    void conversion() {
        org.joda.time.LocalDate jodaLocalDate = new org.joda.time.LocalDate(1945, 9, 2);
        java.time.LocalDate javaLocalDate = java.time.LocalDate.of(1945, 9, 2);
        assertJodaEqualsJava(jodaLocalDate.toDateTime(LocalTime.MIDNIGHT), javaLocalDate.atTime(java.time.LocalTime.MIDNIGHT).atZone(ZoneId.systemDefault()));
        assertJodaEqualsJava(jodaLocalDate.toDateTime(LocalTime.MIDNIGHT, jodaDateTimeZone), javaLocalDate.atTime(java.time.LocalTime.MIDNIGHT).atZone(javaZoneId));
        assertJodaEqualsJava(jodaLocalDate.toDateTimeAtCurrentTime(), javaLocalDate.atTime(java.time.LocalTime.now(clock)).atZone(ZoneId.systemDefault()));
        assertJodaEqualsJava(jodaLocalDate.toDateTimeAtCurrentTime(jodaDateTimeZone), javaLocalDate.atTime(java.time.LocalTime.now(clock)).atZone(javaZoneId));
        assertJodaEqualsJava(jodaLocalDate.toDateTimeAtStartOfDay(), javaLocalDate.atStartOfDay(ZoneId.systemDefault()));
        assertJodaEqualsJava(jodaLocalDate.toDateTimeAtStartOfDay(jodaDateTimeZone), javaLocalDate.atStartOfDay(javaZoneId));
        assertJodaEqualsJava(jodaLocalDate.toLocalDateTime(org.joda.time.LocalTime.MIDNIGHT), javaLocalDate.atTime(java.time.LocalTime.MIDNIGHT));
    }
}
