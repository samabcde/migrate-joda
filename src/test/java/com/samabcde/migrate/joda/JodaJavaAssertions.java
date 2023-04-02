package com.samabcde.migrate.joda;

import java.time.temporal.ChronoField;
import java.util.TimeZone;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class JodaJavaAssertions {
    static void assertJodaEqualsJava(org.joda.time.LocalDate joda, java.time.LocalDate java) {
        assertEquals(joda.getYear(), java.getYear());
        assertEquals(joda.getMonthOfYear(), java.getMonthValue());
        assertEquals(joda.getDayOfYear(), java.getDayOfYear());
    }

    static void assertJodaEqualsJava(org.joda.time.LocalTime joda, java.time.LocalTime java) {
        assertEquals(joda.getHourOfDay(), java.getHour());
        assertEquals(joda.getMinuteOfHour(), java.getMinute());
        assertEquals(joda.getSecondOfMinute(), java.getSecond());
        assertEquals(joda.getMillisOfSecond(), java.get(ChronoField.MILLI_OF_SECOND));
    }

    static void assertJodaEqualsJava(org.joda.time.LocalDateTime joda, java.time.LocalDateTime java) {
        assertJodaEqualsJava(joda.toLocalDate(), java.toLocalDate());
        assertJodaEqualsJava(joda.toLocalTime(), java.toLocalTime());
    }

    static void assertJodaEqualsJava(org.joda.time.DateTime joda, java.time.ZonedDateTime java) {
        assertEquals(joda.getYear(), java.getYear());
        assertEquals(joda.getMonthOfYear(), java.getMonthValue());
        assertEquals(joda.getDayOfYear(), java.getDayOfYear());
        assertEquals(joda.toInstant().getMillis(), java.toInstant().toEpochMilli());
        assertJodaEqualsJava(joda.getZone(), java.getZone());
    }

    static void assertJodaEqualsJava(org.joda.time.DateTimeZone joda, java.time.ZoneId java) {
        assertTrue(TimeZone.getTimeZone(java).hasSameRules(joda.toTimeZone()));
    }

    static void assertJodaEqualsJava(org.joda.time.Instant joda, java.time.Instant java) {
        assertEquals(joda.getMillis(), java.toEpochMilli());
    }

    static void assertJodaEqualsJava(org.joda.time.Period joda, java.time.Period java) {
        assertEquals(joda.getYears(), java.getYears());
        assertEquals(joda.getMonths(), java.getMonths());
        assertEquals(joda.getDays(), java.getDays());
        assertEquals(0, joda.getWeeks(), "week not supported in java Period");
        assertEquals(0, joda.getHours(), "hour not supported in java Period");
        assertEquals(0, joda.getMinutes(), "minute not supported in java Period");
        assertEquals(0, joda.getSeconds(), "second not supported in java Period");
        assertEquals(0, joda.getMillis(), "milli not supported in java Period");
    }

    static void assertJodaEqualsJava(org.joda.time.Period joda, java.time.Duration java) {
        assertEquals(0, joda.getYears(), "year not supported in java Duration");
        assertEquals(0, joda.getMonths(), "month not supported in java Duration");
        assertEquals(0, joda.getWeeks(), "week not supported in java Duration");
        assertEquals(joda.getDays(), java.toDaysPart());
        assertEquals(joda.getHours(), java.toHoursPart());
        assertEquals(joda.getMinutes(), java.toMinutesPart());
        assertEquals(joda.getSeconds(), java.toSecondsPart());
        assertEquals(joda.getMillis(), java.toMillisPart());
        assertEquals(joda.toStandardHours().getHours(), java.toHours());
        assertEquals(joda.toStandardMinutes().getMinutes(), java.toMinutes());
        assertEquals(joda.toStandardSeconds().getSeconds(), java.toSeconds());
    }

    static void assertJodaEqualsJava(org.joda.time.Duration joda, java.time.Duration java) {
        assertEquals(joda.getStandardDays(), java.toDays());
        assertEquals(joda.getStandardHours(), java.toHours());
        assertEquals(joda.getStandardMinutes(), java.toMinutes());
        assertEquals(joda.getStandardSeconds(), java.toSeconds());
        assertEquals(joda.getMillis(), java.toMillis());
    }

    static void assertJodaEqualsJava(org.joda.time.format.DateTimeFormatter jodaFormatter, java.time.format.DateTimeFormatter javaFormatter) {
        assertJodaEqualsJava(org.joda.time.LocalDate.parse("", jodaFormatter), java.time.LocalDate.parse("", javaFormatter));

    }
}
