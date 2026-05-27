package com.udacity.webcrawler;

import javax.inject.Qualifier;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Binding annotation for the number of most popular words
 * the web crawler should return in the final result.
 *
 * <p>This value is loaded from the crawler configuration JSON
 * using the key:
 *
 * <pre>
 * "popularWordCount"
 * </pre>
 *
 * Example:
 *
 * <pre>
 * {
 *   "popularWordCount": 10
 * }
 * </pre>
 */
@Qualifier
@Retention(RetentionPolicy.RUNTIME)
public @interface PopularWordCount {
}