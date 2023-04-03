package com.samabcde.migrate.joda;

import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;
import org.joda.time.DateTimeZone;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junitpioneer.jupiter.DefaultLocale;
import org.junitpioneer.jupiter.DefaultTimeZone;

import java.time.Clock;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.TimeZone;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@DefaultLocale("en-US")
@DefaultTimeZone("UTC")
public class MigrateDateTimeFormatterTest {
    private Clock clock;
    private final TimeZone testTimeZone = TimeZone.getTimeZone("Europe/Kyiv");
    private final DateTimeZone jodaDateTimeZone = DateTimeZone.forTimeZone(testTimeZone);
    private final ZoneId javaZoneId = testTimeZone.toZoneId();

    @BeforeEach
    void setup() {
        DateTimeUtils.setCurrentMillisFixed(1645666800000L);
        clock = Clock.fixed(java.time.Instant.ofEpochMilli(1645666800000L), ZoneOffset.UTC);
    }

    @Test
    void dateTime() {
        org.joda.time.format.DateTimeFormatter jodaFormat = org.joda.time.format.ISODateTimeFormat.dateTime();
        java.time.format.DateTimeFormatter javaFormat = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXXXX");
        assertEquals(jodaFormat.print(DateTime.now()), javaFormat.format(ZonedDateTime.now(clock)));
    }

    @ParameterizedTest
    @CsvSource(textBlock = """
            yyyy-MM-dd'T'HH:mmZ,yyyy-MM-dd'T'HH:mmxxxx
            yyyy-MM-dd'T'HH:mmZZ,yyyy-MM-dd'T'HH:mmxxxxx
            yyyy-MM-dd'T'HH:mmZZZ,yyyy-MM-dd'T'HH:mmVV
            """)
    void zoneNotUTC(String jodaPattern, String javaPattern) {
        org.joda.time.format.DateTimeFormatter jodaFormat = org.joda.time.format.DateTimeFormat.forPattern(jodaPattern);
        java.time.format.DateTimeFormatter javaFormat = java.time.format.DateTimeFormatter.ofPattern(javaPattern);
        assertEquals(jodaFormat.print(DateTime.now().withZone(jodaDateTimeZone)), javaFormat.format(ZonedDateTime.now(clock).withZoneSameInstant(javaZoneId)));
    }

    @ParameterizedTest
    @CsvSource(textBlock = """
            yyyy-MM-dd'T'HH:mmZ,yyyy-MM-dd'T'HH:mmxxxx
            yyyy-MM-dd'T'HH:mmZZ,yyyy-MM-dd'T'HH:mmxxxxx
            """)
        // joda ZZZ always show "UTC" but java show Z
    void zoneInUTC(String jodaPattern, String javaPattern) {
        org.joda.time.format.DateTimeFormatter jodaFormat = org.joda.time.format.DateTimeFormat.forPattern(jodaPattern);
        java.time.format.DateTimeFormatter javaFormat = java.time.format.DateTimeFormatter.ofPattern(javaPattern);
        assertEquals(jodaFormat.print(DateTime.now()), javaFormat.format(ZonedDateTime.now(clock)));
    }

    @Test
    void year() {
        org.joda.time.format.DateTimeFormatter jodaFormat = org.joda.time.format.DateTimeFormat.forPattern("YYYY-MM-dd");
        java.time.format.DateTimeFormatter javaFormat = java.time.format.DateTimeFormatter.ofPattern("YYYY-MM-dd");
        // YYYY is week year in java but year of era in joda
        assertNotEquals(jodaFormat.print(new org.joda.time.LocalDate(2015, 12, 30)), javaFormat.format(java.time.LocalDate.of(2015, 12, 30)));
    }
}
