import java.time.Duration;
import java.util.*;
import java.util.regex.Pattern;

/* ===================== PROFILER ===================== */

interface Profiler {

    <T> T wrap(
            Class<T> klass,
            T delegate);
}

/* ===================== SIMPLE PROFILER ===================== */

class SimpleProfiler
        implements Profiler {

    @Override
    public <T> T wrap(
            Class<T> klass,
            T delegate) {

        System.out.println(
                "Profiling enabled for: "
                        + klass.getSimpleName());

        return delegate;
    }
}

/* ===================== PAGE PARSER ===================== */

interface PageParser {

    Result parse();

    /* ================= RESULT ================= */

    class Result {

        private final Map<String, Integer> wordCounts;

        private final List<String> links;

        Result(
                Map<String, Integer> wordCounts,
                List<String> links) {

            this.wordCounts = wordCounts;

            this.links = links;
        }

        public Map<String, Integer> getWordCounts() {

            return wordCounts;
        }

        public List<String> getLinks() {

            return links;
        }
    }
}

/* ===================== PAGE PARSER IMPLEMENTATION ===================== */

class PageParserImpl
        implements PageParser {

    private final String url;

    private final Duration timeout;

    private final List<Pattern> ignoredWords;

    PageParserImpl(
            String url,
            Duration timeout,
            List<Pattern> ignoredWords) {

        this.url = url;

        this.timeout = timeout;

        this.ignoredWords = ignoredWords;
    }

    @Override
    public Result parse() {

        Map<String, Integer> counts =
                new HashMap<>();

        counts.put("java", 3);

        counts.put("crawler", 2);

        List<String> links =
                Arrays.asList(
                        "https://example.com");

        return new Result(counts, links);
    }
}

/* ===================== PAGE PARSER FACTORY ===================== */

interface PageParserFactory {

    PageParser get(String url);
}

/* ===================== FACTORY IMPLEMENTATION ===================== */

class PageParserFactoryImpl
        implements PageParserFactory {

    private final Profiler profiler;

    private final List<Pattern> ignoredWords;

    private final Duration timeout;

    PageParserFactoryImpl(
            Profiler profiler,
            List<Pattern> ignoredWords,
            Duration timeout) {

        this.profiler = profiler;

        this.ignoredWords = ignoredWords;

        this.timeout = timeout;
    }

    @Override
    public PageParser get(String url) {

        /*
         * CREATE PARSER
         */
        PageParser delegate =
                new PageParserImpl(
                        url,
                        timeout,
                        ignoredWords);

        /*
         * WRAP USING PROFILER
         */
        return profiler.wrap(
                PageParser.class,
                delegate);
    }
}

/* ===================== MAIN ===================== */

public class Main {

    public static void main(String[] args) {

        Profiler profiler =
                new SimpleProfiler();

        List<Pattern> ignoredWords =
                Arrays.asList(
                        Pattern.compile("^.{1,3}$"));

        Duration timeout =
                Duration.ofSeconds(5);

        PageParserFactory factory =
                new PageParserFactoryImpl(
                        profiler,
                        ignoredWords,
                        timeout);

        PageParser parser =
                factory.get(
                        "https://openlibrary.org");

        PageParser.Result result =
                parser.parse();

        System.out.println(
                result.getWordCounts());

        System.out.println(
                result.getLinks());
    }
}