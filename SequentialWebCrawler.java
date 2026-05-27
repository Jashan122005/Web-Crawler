package com.udacity.webcrawler;

import javax.inject.Qualifier;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Binding annotation used for injecting ignored URL patterns
 * into the web crawler application.
 *
 * <p>The value associated with this annotation represents
 * the list of URL patterns that should be skipped during crawling,
 * as defined in the crawler configuration JSON file.
 */
@Qualifier
@Retention(RetentionPolicy.RUNTIME)
public @interface IgnoredUrls {
}