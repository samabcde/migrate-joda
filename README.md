# Migration from Joda to Java Time

Repository to make migration from Joda to Java Time API more easy
My [blog post](https://samabcde.blogspot.com/2023/04/migrate-from-joda-to-javajsr-310.html) for the whole story.

Read to get a brief idea: https://blog.joda.org/2014/11/converting-from-joda-time-to-javatime.html

Check below for the target class you are migrating

- [LocalDate](src/test/java/com/samabcde/migrate/joda/MigrateLocalDateTest.java)
- [LocalDateTime](src/test/java/com/samabcde/migrate/joda/MigrateLocalDateTimeTest.java)
- [LocalTime](src/test/java/com/samabcde/migrate/joda/MigrateLocalTimeTest.java)
- [Instant](src/test/java/com/samabcde/migrate/joda/MigrateInstantTest.java)
- [DateTime](src/test/java/com/samabcde/migrate/joda/MigrateDateTimeTest.java)
- [Period](src/test/java/com/samabcde/migrate/joda/MigratePeriodTest.java)
- [Duration](src/test/java/com/samabcde/migrate/joda/MigrateDurationTest.java)
- [DateTimeFormatter](src/test/java/com/samabcde/migrate/joda/MigrateDateTimeFormatterTest.java)  
  Pattern in Joda and Java has subtle difference, e.g.(Joda 'Y' vs Java 'Y')
  So do checkout the documentation of 
  - [Joda DateTimeFormat](https://www.joda.org/joda-time/apidocs/org/joda/time/format/DateTimeFormat)  
  - [Java DateTimeFormatter](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/time/format/DateTimeFormatter.html) for details.

Feel free to raise PR for adding useful cases, or issue if you find something wrong.