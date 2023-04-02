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
import java.util.Date;
import java.util.TimeZone;

import static com.samabcde.migrate.joda.JodaJavaAssertions.assertJodaEqualsJava;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DefaultLocale("en-US")
@DefaultTimeZone("UTC")
public class MigrateLocalDateTimeTest {
    private Clock clock;
    private final TimeZone testTimeZone = TimeZone.getTimeZone("Europe/London");
    private final DateTimeZone jodaDateTimeZone = DateTimeZone.forTimeZone(testTimeZone);
    private final ZoneId javaZoneId = testTimeZone.toZoneId();

    @BeforeEach
    void setup() {
        DateTimeUtils.setCurrentMillisFixed(15525792000000L);
        clock = Clock.fixed(java.time.Instant.ofEpochMilli(15525792000000L), ZoneOffset.UTC);
    }

    @Test
    void construction() {
        assertJodaEqualsJava(new org.joda.time.LocalDateTime(), java.time.LocalDateTime.now(clock));
        assertJodaEqualsJava(new org.joda.time.LocalDateTime(1945, 8, 6, 8, 15, 1), java.time.LocalDateTime.of(1945, 8, 6, 8, 15, 1));
        assertJodaEqualsJava(new org.joda.time.LocalDateTime(1945, 8, 9, 11, 2, 3, 4), java.time.LocalDateTime.of(1945, 8, 9, 11, 2, 3, 4000000));
        assertJodaEqualsJava(org.joda.time.LocalDateTime.parse("1976-09-09T01:23:45"), java.time.LocalDateTime.parse("1976-09-09T01:23:45"));
        assertJodaEqualsJava(org.joda.time.LocalDateTime.now(), java.time.LocalDateTime.now(clock));
    }

    @Test
    void api() {
        org.joda.time.LocalDateTime jodaLocalDateTime = new org.joda.time.LocalDateTime(1955, 4, 18, 1, 15, 1);
        java.time.LocalDateTime javaLocalDateTime = java.time.LocalDateTime.of(1955, 4, 18, 1, 15, 1);
        assertEquals(jodaLocalDateTime.getCenturyOfEra(), javaLocalDateTime.get(ChronoField.YEAR_OF_ERA) / 100);
        assertEquals(jodaLocalDateTime.isSupported(DateTimeFieldType.dayOfMonth()), javaLocalDateTime.isSupported(ChronoField.DAY_OF_MONTH));
        assertEquals(jodaLocalDateTime.isSupported(DurationFieldType.millis()), javaLocalDateTime.isSupported(ChronoField.MILLI_OF_SECOND));
        assertEquals(jodaLocalDateTime.isSupported(DurationFieldType.months()), javaLocalDateTime.isSupported(ChronoUnit.MONTHS));
        assertEquals(jodaLocalDateTime.isSupported(DurationFieldType.hours()), javaLocalDateTime.isSupported(ChronoUnit.HOURS));
        assertEquals(jodaLocalDateTime.get(DateTimeFieldType.dayOfYear()), javaLocalDateTime.get(ChronoField.DAY_OF_YEAR));
        assertEquals(jodaLocalDateTime.toDate(), Date.from(javaLocalDateTime.atZone(ZoneId.systemDefault()).toInstant()));
        assertEquals(jodaLocalDateTime.toDate(jodaDateTimeZone.toTimeZone()), Date.from(javaLocalDateTime.atZone(javaZoneId).toInstant()));
    }

    @Test
    void conversion() {
        org.joda.time.LocalDateTime jodaLocalDateTime = new org.joda.time.LocalDateTime(2022, 9, 8, 15, 10, 3);
        java.time.LocalDateTime javaLocalDateTime = java.time.LocalDateTime.of(2022, 9, 8, 15, 10, 3);
        assertJodaEqualsJava(jodaLocalDateTime.toDateTime(), javaLocalDateTime.atZone(ZoneOffset.systemDefault()));
        assertJodaEqualsJava(jodaLocalDateTime.toDateTime(jodaDateTimeZone), javaLocalDateTime.atZone(javaZoneId));
        assertJodaEqualsJava(jodaLocalDateTime.toLocalDate(), javaLocalDateTime.toLocalDate());
        assertJodaEqualsJava(jodaLocalDateTime.toLocalTime(), javaLocalDateTime.toLocalTime());
    }

    @Test
    void format() {
        org.joda.time.LocalDateTime jodaLocalDateTime = new org.joda.time.LocalDateTime(1945, 9, 2, 23, 59, 59);
        java.time.LocalDateTime javaLocalDateTime = java.time.LocalDateTime.of(1945, 9, 2, 23, 59, 59);
        // DateTimeFormatter.ISO_LOCAL_DATE_TIME will skip 0 of nanosecond
        assertEquals(jodaLocalDateTime.toString(), javaLocalDateTime.format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS")));
    }
}
