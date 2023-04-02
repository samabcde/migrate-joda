package com.samabcde.migrate.joda;

import org.junit.jupiter.api.Test;

import java.time.temporal.ChronoUnit;

import static com.samabcde.migrate.joda.JodaJavaAssertions.assertJodaEqualsJava;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MigrateDurationTest {

    @Test
    void construction() {
        assertJodaEqualsJava(org.joda.time.Duration.standardDays(1), java.time.Duration.ofDays(1));
        assertJodaEqualsJava(org.joda.time.Duration.standardHours(48), java.time.Duration.ofHours(48));
        assertJodaEqualsJava(org.joda.time.Duration.standardMinutes(78), java.time.Duration.ofMinutes(78));
        assertJodaEqualsJava(org.joda.time.Duration.standardSeconds(480), java.time.Duration.ofSeconds(480));
        assertJodaEqualsJava(org.joda.time.Duration.millis(86400000), java.time.Duration.ofMillis(86400000));
    }

    @Test
    void api() {
        org.joda.time.Duration jodaDuration = org.joda.time.Duration.millis(86400001);
        java.time.Duration javaDuration = java.time.Duration.ofMillis(86400001);
        assertEquals(jodaDuration.getStandardDays(), javaDuration.toDays());
        assertEquals(jodaDuration.getStandardHours(), javaDuration.toHours());
        assertEquals(jodaDuration.getStandardMinutes(), javaDuration.toMinutes());
        assertEquals(jodaDuration.getStandardSeconds(), javaDuration.toSeconds());
        assertJodaEqualsJava(jodaDuration.withDurationAdded(1, 1000), javaDuration.plus(1, ChronoUnit.SECONDS));
        assertJodaEqualsJava(jodaDuration.withDurationAdded(1, 60000), javaDuration.plus(1, ChronoUnit.MINUTES));
        assertJodaEqualsJava(jodaDuration.plus(1), javaDuration.plus(1, ChronoUnit.MILLIS));
    }

}
