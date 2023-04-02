package com.samabcde.migrate.joda;

import org.junit.jupiter.api.Test;

import static com.samabcde.migrate.joda.JodaJavaAssertions.assertJodaEqualsJava;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MigratePeriodTest {

    @Test
    void construction() {
        // Date only joda Period -> java Period
        assertJodaEqualsJava(org.joda.time.Period.months(20), java.time.Period.ofMonths(20));
        assertJodaEqualsJava(org.joda.time.Period.parse("P2Y3M4D"), java.time.Period.parse("P2Y3M4D"));
        assertJodaEqualsJava(new org.joda.time.Period(1, 2, 0, 3, 0, 0, 0, 0), java.time.Period.of(1, 2, 3));
        assertJodaEqualsJava(org.joda.time.Period.years(2).withMonths(3).withDays(4), java.time.Period.of(2, 3, 4));

        // Time only joda Period -> java Duration
        assertJodaEqualsJava(org.joda.time.Period.hours(48).withMinutes(3).withSeconds(4).normalizedStandard(), java.time.Duration.ofHours(48).plusMinutes(3).plusSeconds(4));
        assertJodaEqualsJava(org.joda.time.Period.hours(2).withMinutes(3).withSeconds(4), java.time.Duration.ofHours(2).plusMinutes(3).plusSeconds(4));
        assertJodaEqualsJava(org.joda.time.Period.hours(2).withMinutes(63).withSeconds(4).normalizedStandard(), java.time.Duration.ofHours(2).plusMinutes(63).plusSeconds(4));
        assertJodaEqualsJava(org.joda.time.Period.hours(2).withMinutes(63).withSeconds(4).withMillis(8964).normalizedStandard(), java.time.Duration.ofHours(2).plusMinutes(63).plusSeconds(4).plusMillis(8964));
    }

    @Test
    void incompatible() {
        assertThrows(AssertionError.class, () -> assertJodaEqualsJava(org.joda.time.Period.weeks(1), java.time.Period.ofWeeks(1)));
        assertEquals(0, org.joda.time.Period.weeks(1).getDays());
        assertEquals(7, java.time.Period.ofWeeks(1).getDays());
        assertThrows(AssertionError.class, () -> assertJodaEqualsJava(org.joda.time.Period.days(8).normalizedStandard(), java.time.Period.ofDays(8).normalized()));
    }

    @Test
    void api() {
        org.joda.time.Period jodaDatePeriod = org.joda.time.Period.years(2).withMonths(30).withDays(2);
        java.time.Period javaPeriod = java.time.Period.of(2, 30, 2);
        assertEquals(jodaDatePeriod.getYears(), javaPeriod.getYears());
        assertEquals(jodaDatePeriod.getMonths(), javaPeriod.getMonths());
        assertEquals(jodaDatePeriod.getDays(), javaPeriod.getDays());
        // only works for month
        assertJodaEqualsJava(jodaDatePeriod.normalizedStandard(), javaPeriod.normalized());

        org.joda.time.Period jodaTimePeriod = org.joda.time.Period.days(2).withHours(10).withMinutes(5).withSeconds(3).withMillis(30);
        java.time.Duration javaTimeDuration = java.time.Duration.ofDays(2).plusHours(10).plusMinutes(5).plusSeconds(3).plusMillis(30);
        assertEquals(jodaTimePeriod.getDays(), javaTimeDuration.toDaysPart());
        assertEquals(jodaTimePeriod.getHours(), javaTimeDuration.toHoursPart());
        assertEquals(jodaTimePeriod.getMinutes(), javaTimeDuration.toMinutesPart());
        assertEquals(jodaTimePeriod.getSeconds(), javaTimeDuration.toSecondsPart());
        assertEquals(jodaTimePeriod.getMillis(), javaTimeDuration.toMillisPart());
        assertEquals(jodaTimePeriod.toStandardHours().getHours(), javaTimeDuration.toHours());
        assertEquals(jodaTimePeriod.toStandardMinutes().getMinutes(), javaTimeDuration.toMinutes());
        assertEquals(jodaTimePeriod.toStandardSeconds().getSeconds(), javaTimeDuration.toSeconds());
    }

}
