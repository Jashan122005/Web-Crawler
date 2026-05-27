package com.udacity.webcrawler.profiler;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

import java.time.Clock;

/**
 * Guice module that provides
 * a singleton Profiler instance.
 *
 * Requires Clock to already be bound.
 */
public final class ProfilerModule
        extends AbstractModule {

    @Provides
    @Singleton
    Profiler provideProfiler(
            Clock clock) {

        return new ProfilerImpl(clock);
    }
}