import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.*;
import java.util.stream.Collectors;

/* ===================== PROFILED ANNOTATION ===================== */

@Retention(RetentionPolicy.RUNTIME)
@interface Profiled {
}

/* ===================== PAGE PARSER ===================== */

interface PageParser {

    /*
     * PARSE METHOD
     */
    @Profiled
    Result parse();

    /* ===================== RESULT CLASS ===================== */

    final class Result {

        private final Map<String, Integer> wordCounts;

        private final List<String> links;

        private Result(
                Map<String, Integer> wordCounts,
                List<String> links) {

            this.wordCounts =
                    Objects.requireNonNull(wordCounts);

            this.links =
                    Objects.requireNonNull(links);
        }

        /*
         * RETURN WORD COUNTS
         */
        public Map<String, Integer> getWordCounts() {

            return wordCounts;
        }

        /*
         * RETURN LINKS
         */
        public List<String> getLinks() {

            return links;
        }

        /* ================= BUILDER ================= */

        static final class Builder {

            private final Map<String, Integer> wordCounts =
                    new HashMap<>();

            private final Set<String> links =
                    new HashSet<>();

            /*
             * ADD WORD
             */
            void addWord(String word) {

                Objects.requireNonNull(word);

                wordCounts.compute(
                        word,
                        (k, v) -> (v == null)
                                ? 1
                                : v + 1);
            }

            /*
             * ADD LINK
             */
            void addLink(String link) {

                links.add(
                        Objects.requireNonNull(link));
            }

            /*
             * BUILD RESULT
             */
            Result build() {

                return new Result(

                        Collections.unmodifiableMap(
                                wordCounts),

                        links.stream()
                                .collect(
                                        Collectors.toUnmodifiableList())
                );
            }
        }
    }
}

/* ===================== PAGE PARSER IMPLEMENTATION ===================== */

class PageParserImpl implements PageParser {

    @Override
    public Result parse() {

        Result.Builder builder =
                new Result.Builder();

        builder.addWord("java");

        builder.addWord("java");

        builder.addWord("crawler");

        builder.addLink(
                "https://example.com");

        builder.addLink(
                "https://openlibrary.org");

        return builder.build();
    }
}

/* ===================== MAIN ===================== */

public class Main {

    public static void main(String[] args) {

        PageParser parser =
                new PageParserImpl();

        PageParser.Result result =
                parser.parse();

        System.out.println(
                "WORD COUNTS:");

        System.out.println(
                result.getWordCounts());

        System.out.println(
                "\nLINKS:");

        System.out.println(
                result.getLinks());
    }
}