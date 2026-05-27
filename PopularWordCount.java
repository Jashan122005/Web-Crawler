package com.udacity.webcrawler.profiler;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Method;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Stores and writes profiling information.
 */
final class ProfilingState {

    /*
     * METHOD -> TOTAL EXECUTION TIME
     */
    private final Map<String, Duration> data =
            new ConcurrentHashMap<>();

    /**
     * Records execution time for a method.
     *
     * @param callingClass class invoking method
     * @param method invoked method
     * @param elapsed execution duration
     */
    void record(
            Class<?> callingClass,
            Method method,
            Duration elapsed) {

        Objects.requireNonNull(callingClass);

        Objects.requireNonNull(method);

        Objects.requireNonNull(elapsed);

        /*
         * NEGATIVE DURATION CHECK
         */
        if (elapsed.isNegative()) {

            throw new IllegalArgumentException(
                    "negative elapsed time");
        }

        /*
         * METHOD IDENTIFIER
         */
        String key =
                formatMethodCall(
                        callingClass,
                        method);

        /*
         * ADD OR UPDATE DURATION
         */
        data.compute(

                key,

                (k, v) ->

                        (v == null)

                                ? elapsed

                                : v.plus(elapsed));
    }

    /**
     * Writes profiling data.
     */
    void write(Writer writer)
            throws IOException {

        List<String> entries =

                data.entrySet()

                        .stream()

                        .sorted(
                                Map.Entry.comparingByKey())

                        .map(e ->

                                e.getKey()

                                        + " took "

                                        + formatDuration(
                                        e.getValue())

                                        + System.lineSeparator())

                        .collect(Collectors.toList());

        /*
         * WRITE EACH ENTRY
         */
        for (String entry : entries) {

            writer.write(entry);
        }
    }

    /**
     * Formats method name.
     */
    private static String formatMethodCall(
            Class<?> callingClass,
            Method method) {

        return String.format(

                "%s#%s",

                callingClass.getName(),

                method.getName());
    }

    /**
     * Formats duration.
     */
    private static String formatDuration(
            Duration duration) {

        return String.format(

                "%sm %ss %sms",

                duration.toMinutes(),

                duration.toSecondsPart(),

                duration.toMillisPart());
    }
}