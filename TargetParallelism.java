package com.udacity.webcrawler;

import javax.inject.Qualifier;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Binding annotation used for injecting the maximum crawl depth
 * into the web crawler application.
 *
 * <p>The value associated with this annotation represents
 * the maximum depth level the crawler is allowed to traverse,
 * as specified in the crawler configuration JSON file.
 */
@Qualifier
@Retention(RetentionPolicy.RUNTIME)
public @interface MaxDepth {
}