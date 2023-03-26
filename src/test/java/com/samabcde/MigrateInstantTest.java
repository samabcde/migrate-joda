package com.samabcde;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DateTimeUtils;
import org.joda.time.DateTimeZone;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.DefaultTimeZone;

import java.time.Clock;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.temporal.ChronoField;
import java.util.Date;
import java.util.TimeZone;

import static com.samabcde.JodaJavaAssertions.assertJodaEqualsJava;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@DefaultTimeZone("UTC")
public class MigrateInstantTest {
    private Clock clock;

    private final TimeZone testTimeZone = TimeZone.getTimeZone("Asia/Abidjan");
    private final DateTimeZone jodaDateTimeZone = DateTimeZone.forTimeZone(testTimeZone);
    private final ZoneId javaZoneId = testTimeZone.toZoneId();
    @BeforeEach
    void setup() {
        DateTimeUtils.setCurrentMillisFixed(0);
        clock = Clock.fixed(java.time.Instant.ofEpochMilli(0), ZoneOffset.UTC);
    }

    @Test
    void construction() {
        assertJodaEqualsJava(new org.joda.time.Instant(), java.time.Instant.now(clock));
        assertJodaEqualsJava(new org.joda.time.Instant(987654321L), java.time.Instant.ofEpochMilli(987654321L));
        assertJodaEqualsJava(org.joda.time.Instant.parse("2022-01-01T01:23:45+01:00"), java.time.Instant.parse("2022-01-01T01:23:45+01:00"));
        assertJodaEqualsJava(org.joda.time.Instant.now(), java.time.Instant.now(clock));
        assertJodaEqualsJava(org.joda.time.Instant.EPOCH, java.time.Instant.EPOCH);
    }

    @Test
    void api() {
        org.joda.time.Instant jodaInstant = new org.joda.time.Instant(987654321L);
        java.time.Instant javaInstant = java.time.Instant.ofEpochMilli(987654321L);

        assertNotEquals(jodaInstant.isSupported(DateTimeFieldType.dayOfMonth()), javaInstant.isSupported(ChronoField.DAY_OF_MONTH));
        assertNotEquals(jodaInstant.isSupported(DateTimeFieldType.millisOfDay()), javaInstant.isSupported(ChronoField.MILLI_OF_DAY));
        assertNotEquals(jodaInstant.isSupported(DateTimeFieldType.secondOfMinute()), javaInstant.isSupported(ChronoField.SECOND_OF_MINUTE));
        assertNotEquals(jodaInstant.isSupported(DateTimeFieldType.hourOfDay()), javaInstant.isSupported(ChronoField.HOUR_OF_DAY));
        assertNotEquals(jodaInstant.isSupported(DateTimeFieldType.minuteOfHour()), javaInstant.isSupported(ChronoField.MINUTE_OF_HOUR));

        assertEquals(jodaInstant.isSupported(DateTimeFieldType.millisOfSecond()), javaInstant.isSupported(ChronoField.MILLI_OF_SECOND));
        assertEquals(jodaInstant.get(DateTimeFieldType.millisOfSecond()), javaInstant.get(ChronoField.MILLI_OF_SECOND));
        assertJodaEqualsJava(jodaInstant.getZone(), ZoneId.systemDefault());
        assertEquals(jodaInstant.toDate(), Date.from(javaInstant));
    }

    @Test
    void conversion() {
        org.joda.time.Instant jodaInstant = new org.joda.time.Instant(987654321L);
        java.time.Instant javaInstant = java.time.Instant.ofEpochMilli(987654321L);
        assertJodaEqualsJava(jodaInstant.toDateTime(), javaInstant.atZone(ZoneId.systemDefault()));
        assertJodaEqualsJava(jodaInstant.toDateTime(jodaDateTimeZone), javaInstant.atZone(javaZoneId));
    }
}
