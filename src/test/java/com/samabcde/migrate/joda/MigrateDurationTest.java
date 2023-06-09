package com.samabcde.migrate.joda;

import org.junit.jupiter.api.Test;

import java.time.temporal.ChronoUnit;

import static com.samabcde.migrate.joda.JodaJavaAssertions.assertJodaEqualsJava;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class MigrateDurationTest {

    @Test
    void construction() {
        assertJodaEqualsJava(org.joda.time.Duration.standardDays(1), java.time.Duration.ofDays(1));
        assertJodaEqualsJava(org.joda.time.Duration.standardHours(48), java.time.Duration.ofHours(48));
        assertJodaEqualsJava(org.joda.time.Duration.standardMinutes(78), java.time.Duration.ofMinutes(78));
        assertJodaEqualsJava(org.joda.time.Duration.standardSeconds(480), java.time.Duration.ofSeconds(480));
        assertJodaEqualsJava(org.joda.time.Duration.millis(86400000), java.time.Duration.ofMillis(86400000));
        assertJodaEqualsJava(new org.joda.time.Duration(org.joda.time.Instant.ofEpochMilli(1), org.joda.time.Instant.ofEpochMilli(2)),
                java.time.Duration.between(java.time.Instant.ofEpochMilli(1), java.time.Instant.ofEpochMilli(2)));
        assertJodaEqualsJava(new org.joda.time.Duration(10, 20), java.time.Duration.between(java.time.Instant.ofEpochMilli(10), java.time.Instant.ofEpochMilli(20)));
        assertJodaEqualsJava(new org.joda.time.Duration(100), java.time.Duration.ofMillis(100));
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

    @Test
    void incompatible() {
        org.joda.time.Duration jodaDuration = org.joda.time.Duration.millis(86400001);
        java.time.Duration javaDuration = java.time.Duration.ofMillis(86400001);
        // java will standardize the value
        // joda PT86400.001S
        // java PT24H0.001S
        assertNotEquals(jodaDuration.toString(), javaDuration.toString());
    }

}
