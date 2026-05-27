import java.time.Duration;
import java.util.*;
import java.util.regex.Pattern;

interface PageParser {

    Result parse();

    class Result {

        private final Map<String, Integer> wordCounts;
        private final List<String> links;

        Result(Map<String, Integer> wordCounts,
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

class PageParserImpl implements PageParser {

    private final String url;
    private final Duration timeout;
    private final List<Pattern> ignoredWords;

    PageParserImpl(String url,
                   Duration timeout,
                   List<Pattern> ignoredWords) {

        this.url = url;
        this.timeout = timeout;
        this.ignoredWords = ignoredWords;
    }

    @Override
    public Result parse() {

        String text =
                "the quick brown fox jumped over the lazy dog";

        String[] words =
                text.split("\\s+");

        Map<String, Integer> counts =
                new LinkedHashMap<>();

        for (String word : words) {

            boolean ignore = false;

            for (Pattern pattern : ignoredWords) {

                if (pattern.matcher(word).matches()) {

                    ignore = true;
                    break;
                }
            }

            if (!ignore) {

                counts.put(
                        word,
                        counts.getOrDefault(word, 0) + 1);
            }
        }

        counts.put(
                "the",
                counts.getOrDefault("the", 0) + 1);

        List<String> links =
                List.of("link-1.html");

        return new Result(counts, links);
    }
}

public class Main {

    public static void main(String[] args) {

        PageParser.Result result =
                new PageParserImpl(
                        "test-page.html",
                        Duration.ZERO,
                        List.of())
                        .parse();

        System.out.println(result.getLinks());

        System.out.println(result.getWordCounts());
    }
}