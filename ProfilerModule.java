import java.time.Duration;
import java.util.*;
import java.util.regex.Pattern;

/* ===================== PAGE PARSER ===================== */

interface PageParser {

    void parse();
}

/* ===================== PAGE PARSER IMPLEMENTATION ===================== */

class PageParserImpl
        implements PageParser {

    @Override
    public void parse() {

        System.out.println(
                "Parsing page...");
    }
}

/* ===================== PAGE PARSER FACTORY ===================== */

interface PageParserFactory {

    PageParser get(String url);
}

/* ===================== FACTORY IMPLEMENTATION ===================== */

class PageParserFactoryImpl
        implements PageParserFactory {

    private final Duration timeout;

    private final List<Pattern> ignoredWords;

    PageParserFactoryImpl(
            Duration timeout,
            List<Pattern> ignoredWords) {

        this.timeout = timeout;

        this.ignoredWords = ignoredWords;
    }

    @Override
    public PageParser get(String url) {

        System.out.println(
                "URL: " + url);

        System.out.println(
                "Timeout: " + timeout);

        System.out.println(
                "Ignored Words: "
                        + ignoredWords);

        return new PageParserImpl();
    }
}

/* ===================== PARSER MODULE ===================== */

class ParserModule {

    private final Duration timeout;

    private final List<Pattern> ignoredWords;

    /*
     * CONSTRUCTOR
     */
    private ParserModule(
            Duration timeout,
            List<Pattern> ignoredWords) {

        this.timeout = timeout;

        this.ignoredWords = ignoredWords;
    }

    /*
     * CREATE FACTORY
     */
    public PageParserFactory createFactory() {

        return new PageParserFactoryImpl(
                timeout,
                ignoredWords);
    }

    /* ================= BUILDER ================= */

    static final class Builder {

        private Duration timeout;

        private List<Pattern> ignoredWords;

        /*
         * SET TIMEOUT
         */
        public Builder setTimeout(
                Duration timeout) {

            this.timeout =
                    Objects.requireNonNull(timeout);

            return this;
        }

        /*
         * SET IGNORED WORDS
         */
        public Builder setIgnoredWords(
                List<Pattern> ignoredWords) {

            this.ignoredWords =
                    Objects.requireNonNull(
                            ignoredWords);

            return this;
        }

        /*
         * BUILD MODULE
         */
        public ParserModule build() {

            return new ParserModule(
                    timeout,
                    ignoredWords);
        }
    }
}

/* ===================== MAIN ===================== */

public class Main {

    public static void main(String[] args) {

        ParserModule module =

                new ParserModule.Builder()

                        .setTimeout(
                                Duration.ofSeconds(5))

                        .setIgnoredWords(
                                Arrays.asList(
                                        Pattern.compile(
                                                "^.{1,3}$")))

                        .build();

        PageParserFactory factory =
                module.createFactory();

        PageParser parser =
                factory.get(
                        "https://openlibrary.org");

        parser.parse();
    }
}