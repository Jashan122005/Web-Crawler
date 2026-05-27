import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.*;
import java.util.regex.Pattern;

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

        /* ================= BUILDER ================= */

        static class Builder {

            private final Map<String, Integer> wordCounts =
                    new HashMap<>();

            private final List<String> links =
                    new ArrayList<>();

            void addWord(String word) {

                wordCounts.put(
                        word,
                        wordCounts.getOrDefault(
                                word,
                                0) + 1);
            }

            void addLink(String link) {

                links.add(link);
            }

            Result build() {

                return new Result(
                        Collections.unmodifiableMap(
                                wordCounts),
                        Collections.unmodifiableList(
                                links));
            }
        }
    }
}

/* ===================== PAGE PARSER IMPLEMENTATION ===================== */

class PageParserImpl
        implements PageParser {

    /*
     * WHITESPACE
     */
    private static final Pattern WHITESPACE =
            Pattern.compile("\\s+");

    /*
     * NON WORD CHARACTERS
     */
    private static final Pattern NON_WORD_CHARACTERS =
            Pattern.compile("\\W");

    private final String uri;

    private final Duration timeout;

    private final List<Pattern> ignoredWords;

    PageParserImpl(
            String uri,
            Duration timeout,
            List<Pattern> ignoredWords) {

        this.uri =
                Objects.requireNonNull(uri);

        this.timeout =
                Objects.requireNonNull(timeout);

        this.ignoredWords =
                Objects.requireNonNull(ignoredWords);
    }

    @Override
    public Result parse() {

        URI parsedUri;

        try {

            parsedUri =
                    new URI(uri);

        } catch (URISyntaxException e) {

            return new Result.Builder()
                    .build();
        }

        /*
         * SIMULATED HTML CONTENT
         */
        String html =
                "<html>"
                        + "<body>"
                        + "<p>The quick brown fox jumped over the lazy dog</p>"
                        + "<a href='https://example.com'>Example</a>"
                        + "</body>"
                        + "</html>";

        Result.Builder builder =
                new Result.Builder();

        /*
         * EXTRACT TEXT
         */
        String text =
                html.replaceAll(
                        "<[^>]*>",
                        " ");

        Arrays.stream(
                        WHITESPACE.split(text))

                .filter(s -> !s.isBlank())

                .map(String::trim)

                .map(String::toLowerCase)

                .map(s ->
                        NON_WORD_CHARACTERS
                                .matcher(s)
                                .replaceAll(""))

                .filter(s -> !s.isBlank())

                .filter(s ->
                        ignoredWords.stream()
                                .noneMatch(
                                        p -> p.matcher(s)
                                                .matches()))

                .forEach(builder::addWord);

        /*
         * EXTRACT LINKS
         */
        if (html.contains("href='https://example.com'")) {

            builder.addLink(
                    "https://example.com");
        }

        return builder.build();
    }

    /*
     * CHECK LOCAL FILE
     */
    private static boolean isLocalFile(
            URI uri) {

        return uri.getScheme() != null
                && uri.getScheme()
                .equals("file");
    }
}

/* ===================== MAIN ===================== */

public class Main {

    public static void main(String[] args) {

        List<Pattern> ignoredWords =
                Arrays.asList(
                        Pattern.compile("^.{1,3}$"));

        PageParser parser =
                new PageParserImpl(
                        "https://example.com",
                        Duration.ofSeconds(5),
                        ignoredWords);

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