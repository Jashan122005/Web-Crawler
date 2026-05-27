package com.udacity.webcrawler;

import com.udacity.webcrawler.json.CrawlResult;
import com.udacity.webcrawler.parser.PageParser;
import com.udacity.webcrawler.parser.PageParserFactory;
import java.util.ArrayList;
import javax.inject.Inject;
import javax.inject.Provider;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * A concrete implementation of {@link WebCrawler} that runs multiple threads on a
 * {@link ForkJoinPool} to fetch and process multiple web pages in parallel.
 */
final class ParallelWebCrawler implements WebCrawler {

    private final Clock clock;
    private final Duration timeout;
    private final int popularWordCount;
    private final ForkJoinPool pool;

    private final PageParserFactory parserFactory;
    private final List<Pattern> ignoredUrls;
    private final int maxDepth;

    @Inject
    ParallelWebCrawler(
            Clock clock,
            PageParserFactory parserFactory,
            @Timeout Duration timeout,
            @PopularWordCount int popularWordCount,
            @MaxDepth int maxDepth,
            @IgnoredUrls List<Pattern> ignoredUrls,
            @TargetParallelism int threadCount) {

        this.clock = clock;

        this.parserFactory = parserFactory;

        this.timeout = timeout;

        this.popularWordCount = popularWordCount;

        this.maxDepth = maxDepth;

        this.ignoredUrls = ignoredUrls;

        this.pool = new ForkJoinPool(
                Math.min(
                        threadCount,
                        getMaxParallelism()));
    }

    @Override
    public CrawlResult crawl(
            List<String> startingUrls) {

        Instant deadline =
                clock.instant().plus(timeout);

        ConcurrentMap<String, Integer> counts =
                new ConcurrentHashMap<>();

        ConcurrentSkipListSet<String> visitedUrls =
                new ConcurrentSkipListSet<>();

        List<ForkJoinTask<?>> tasks =
                new ArrayList<>();

        /*
         * SUBMIT ALL TASKS FIRST
         */
        for (String url : startingUrls) {

            CrawlInternalTask task =
                    new CrawlInternalTask(
                            url,
                            deadline,
                            maxDepth,
                            counts,
                            visitedUrls);

            tasks.add(
                    pool.submit(task));
        }

        /*
         * JOIN ALL TASKS
         */
        for (ForkJoinTask<?> task : tasks) {

            task.join();
        }

        return new CrawlResult.Builder()

                .setWordCounts(
                        WordCounts.sort(
                                counts,
                                popularWordCount))

                .setUrlsVisited(
                        visitedUrls.size())

                .build();
    }

    @Override
    public int getMaxParallelism() {
        return Runtime.getRuntime().availableProcessors();
    }

    /**
     * Recursive crawler task executed in parallel.
     */
    private final class CrawlInternalTask
            extends RecursiveAction {

        private final String url;

        private final Instant deadline;

        private final int depth;

        private final ConcurrentMap<String, Integer> counts;

        private final ConcurrentSkipListSet<String> visitedUrls;

        CrawlInternalTask(
                String url,
                Instant deadline,
                int depth,
                ConcurrentMap<String, Integer> counts,
                ConcurrentSkipListSet<String> visitedUrls) {

            this.url = url;

            this.deadline = deadline;

            this.depth = depth;

            this.counts = counts;

            this.visitedUrls = visitedUrls;
        }

        @Override
        protected void compute() {

            /*
             * DEPTH/TIME CHECK
             */
            if (depth == 0
                    || clock.instant().isAfter(deadline)) {

                return;
            }

            /*
             * IGNORE URLS
             */
            for (Pattern pattern : ignoredUrls) {

                if (pattern.matcher(url).matches()) {

                    return;
                }
            }

            /*
             * SKIP VISITED URLS
             */
            if (!visitedUrls.add(url)) {

                return;
            }

            /*
             * PARSE PAGE
             */
            PageParser.Result result =
                    parserFactory.get(url).parse();

            /*
             * MERGE COUNTS
             */
            result.getWordCounts().forEach(

                    (word, count) ->

                            counts.merge(
                                    word,
                                    count,
                                    Integer::sum));

            /*
             * CREATE SUBTASKS
             */
            List<CrawlInternalTask> subtasks =
                    new ArrayList<>();

            for (String link : result.getLinks()) {

                subtasks.add(

                        new CrawlInternalTask(

                                link,

                                deadline,

                                depth - 1,

                                counts,

                                visitedUrls));
            }

            invokeAll(subtasks);
        }
    }
}