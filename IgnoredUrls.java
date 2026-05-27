package com.udacity.webcrawler.profiler;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Path;

/**
 * Interface for profiling method execution.
 *
 * The profiler wraps objects whose methods are annotated
 * with {@link Profiled} and records execution statistics.
 */
public interface Profiler {

    /**
     * Wraps the given delegate object so that
     * profiled methods can be monitored.
     *
     * @param klass interface class type
     * @param delegate implementation object
     * @param <T> delegate type
     * @return wrapped delegate object
     *
     * @throws IllegalArgumentException
     * if no methods are annotated with @Profiled
     */
    <T> T wrap(Class<T> klass, T delegate);

    /**
     * Writes profiling data to the given file path.
     *
     * If the file already exists,
     * data should be appended.
     *
     * @param path output file path
     * @throws IOException if writing fails
     */
    void writeData(Path path)
            throws IOException;

    /**
     * Writes profiling data using the given writer.
     *
     * @param writer destination writer
     * @throws IOException if writing fails
     */
    void writeData(Writer writer)
            throws IOException;
}