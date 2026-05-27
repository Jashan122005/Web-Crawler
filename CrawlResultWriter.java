import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

class CrawlResult {

    private final Map<String, Integer> wordCounts;

    private final int urlsVisited;

    /*
     * CONSTRUCTOR
     */
    private CrawlResult(
            Map<String, Integer> wordCounts,
            int urlsVisited) {

        this.wordCounts = wordCounts;

        this.urlsVisited = urlsVisited;
    }

    /*
     * RETURN WORD COUNTS
     */
    public Map<String, Integer> getWordCounts() {

        return wordCounts;
    }

    /*
     * RETURN URL COUNT
     */
    public int getUrlsVisited() {

        return urlsVisited;
    }

    /*
     * BUILDER CLASS
     */
    public static final class Builder {

        private Map<String, Integer> wordFrequencies =
                new HashMap<>();

        private int pageCount;

        /*
         * SET WORD COUNTS
         */
        public Builder setWordCounts(
                Map<String, Integer> wordCounts) {

            this.wordFrequencies =
                    Objects.requireNonNull(wordCounts);

            return this;
        }

        /*
         * SET URLS VISITED
         */
        public Builder setUrlsVisited(
                int pageCount) {

            this.pageCount = pageCount;

            return this;
        }

        /*
         * BUILD OBJECT
         */
        public CrawlResult build() {

            return new CrawlResult(
                    Collections.unmodifiableMap(
                            wordFrequencies),
                    pageCount);
        }
    }
}

/* ===================== MAIN ===================== */

public class Main {

    public static void main(String[] args) {

        Map<String, Integer> counts =
                new HashMap<>();

        counts.put("java", 10);

        counts.put("crawler", 5);

        CrawlResult result =
                new CrawlResult.Builder()
                        .setWordCounts(counts)
                        .setUrlsVisited(15)
                        .build();

        System.out.println(
                result.getWordCounts());

        System.out.println(
                result.getUrlsVisited());
    }
}