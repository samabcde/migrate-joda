package com.samabcde.migrate.joda;

import org.joda.time.DateTime;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DateTimeUtils;
import org.joda.time.DateTimeZone;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.DefaultLocale;
import org.junitpioneer.jupiter.DefaultTimeZone;

import java.time.Clock;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.util.Date;
import java.util.TimeZone;

import static com.samabcde.migrate.joda.JodaJavaAssertions.assertJodaEqualsJava;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DefaultLocale("en-US")
@DefaultTimeZone("UTC")
public class MigrateDateTimeTest {
    private Clock clock;
    private final TimeZone testTimeZone = TimeZone.getTimeZone("Europe/Paris");
    private final DateTimeZone jodaDateTimeZone = DateTimeZone.forTimeZone(testTimeZone);
    private final ZoneId javaZoneId = testTimeZone.toZoneId();

    @BeforeEach
    void setup() {
        DateTimeUtils.setCurrentMillisFixed(629596800000L);
        clock = Clock.fixed(java.time.Instant.ofEpochMilli(629596800000L), ZoneOffset.UTC);
    }

    @Test
    void construction() {
        assertJodaEqualsJava(new DateTime(), ZonedDateTime.now(clock));
        assertJodaEqualsJava(new DateTime(1989, 6, 4, 1, 2, 3), ZonedDateTime.of(1989, 6, 4, 1, 2, 3, 0, ZoneOffset.systemDefault()));
        assertJodaEqualsJava(new DateTime(1989, 6, 4, 1, 2, 3, jodaDateTimeZone), ZonedDateTime.of(1989, 6, 4, 1, 2, 3, 0, javaZoneId));
        assertJodaEqualsJava(new DateTime(1989, 6, 4, 1, 2, 3, 4), ZonedDateTime.of(1989, 6, 4, 1, 2, 3, 4000000, ZoneOffset.systemDefault()));
        assertJodaEqualsJava(new DateTime(1989, 6, 4, 1, 2, 3, 4, jodaDateTimeZone), ZonedDateTime.of(1989, 6, 4, 1, 2, 3, 4000000, javaZoneId));
        assertJodaEqualsJava(new DateTime(612921600L), ZonedDateTime.ofInstant(java.time.Instant.ofEpochMilli(612921600L), ZoneId.systemDefault()));
        assertJodaEqualsJava(DateTime.parse("2022-01-01T01:23:45+01:00"), ZonedDateTime.parse("2022-01-01T01:23:45+01:00"));
        assertJodaEqualsJava(DateTime.now(), ZonedDateTime.now(clock));
        assertJodaEqualsJava(DateTime.now(DateTimeZone.UTC), ZonedDateTime.now(clock).withZoneSameInstant(ZoneOffset.UTC));
    }

    @Test
    void api() {
        org.joda.time.DateTime jodaDateTime = DateTime.now();
        ZonedDateTime javaZonedDateTime = ZonedDateTime.now(clock);

        assertEquals(jodaDateTime.isSupported(DateTimeFieldType.dayOfMonth()), javaZonedDateTime.isSupported(ChronoField.DAY_OF_MONTH));
        assertEquals(jodaDateTime.isSupported(DateTimeFieldType.millisOfDay()), javaZonedDateTime.isSupported(ChronoField.MILLI_OF_DAY));
        assertEquals(jodaDateTime.isSupported(DateTimeFieldType.secondOfMinute()), javaZonedDateTime.isSupported(ChronoField.SECOND_OF_MINUTE));
        assertEquals(jodaDateTime.isSupported(DateTimeFieldType.hourOfDay()), javaZonedDateTime.isSupported(ChronoField.HOUR_OF_DAY));
        assertEquals(jodaDateTime.isSupported(DateTimeFieldType.minuteOfHour()), javaZonedDateTime.isSupported(ChronoField.MINUTE_OF_HOUR));
        assertEquals(jodaDateTime.isSupported(DateTimeFieldType.millisOfSecond()), javaZonedDateTime.isSupported(ChronoField.MILLI_OF_SECOND));
        assertEquals(jodaDateTime.get(DateTimeFieldType.millisOfSecond()), javaZonedDateTime.get(ChronoField.MILLI_OF_SECOND));


        assertJodaEqualsJava(jodaDateTime.getZone(), ZoneId.systemDefault());
        assertEquals(jodaDateTime.toDate(), Date.from(javaZonedDateTime.toInstant()));
        assertJodaEqualsJava(jodaDateTime.withZone(jodaDateTimeZone), javaZonedDateTime.withZoneSameInstant(javaZoneId));
        assertJodaEqualsJava(jodaDateTime.withZoneRetainFields(jodaDateTimeZone), javaZonedDateTime.withZoneSameLocal(javaZoneId));
        assertJodaEqualsJava(jodaDateTime.withTimeAtStartOfDay(), javaZonedDateTime.with(LocalTime.MIDNIGHT));
    }

    @Test
    void conversion() {
        org.joda.time.DateTime jodaDateTime = DateTime.now();
        ZonedDateTime javaZonedDateTime = ZonedDateTime.now(clock);
        assertJodaEqualsJava(jodaDateTime.toInstant(), javaZonedDateTime.toInstant());
        assertJodaEqualsJava(jodaDateTime.toLocalDate(), javaZonedDateTime.toLocalDate());
        assertJodaEqualsJava(jodaDateTime.toLocalTime(), javaZonedDateTime.toLocalTime());
        assertJodaEqualsJava(jodaDateTime.toLocalDateTime(), javaZonedDateTime.toLocalDateTime());
    }

    @Test
    void format() {
        org.joda.time.DateTime jodaDateTime = DateTime.now().withZone(DateTimeZone.UTC);
        ZonedDateTime javaZonedDateTime = ZonedDateTime.now(clock).withZoneSameInstant(ZoneOffset.UTC);
        // UTC to test zero offset will not show Z
        // DateTimeFormatter.ISO_ZONED_DATE_TIME will skip 0 of nanosecond
        assertEquals(jodaDateTime.toString(), javaZonedDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXXXX")));
        // non UTC
        assertEquals(jodaDateTime.withZone(jodaDateTimeZone).toString(), javaZonedDateTime.withZoneSameInstant(javaZoneId).format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXXXX")));
    }
}
