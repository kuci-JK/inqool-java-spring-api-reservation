package com.example.inqooltennisreservationapi.matchers;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class LocalDateTimeMatcher extends TypeSafeMatcher<String> {

    private final LocalDateTime expected;

    public LocalDateTimeMatcher(LocalDateTime expected) {
        this.expected = expected;
    }

    @Override
    protected boolean matchesSafely(String localDateTimeString) {
        try {
            var x = LocalDateTime.parse(localDateTimeString);
            if (ChronoUnit.SECONDS.between(expected, x) != 0) {
                throw new RuntimeException(String.format("Expected: %s, Actual: %s", expected, x));
            }
            return ChronoUnit.SECONDS.between(expected, x) == 0;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("to be a LocalDateTime within 0 seconds of " + expected);
    }
}
