import java.util.*;

/* ===================== PAGE PARSER ===================== */

interface PageParser {

    Result parse();

    /* ================= RESULT CLASS ================= */

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

class PageParserImpl implements PageParser {

    private final String url;

    PageParserImpl(String url) {

        this.url = url;
    }

    @Override
    public Result parse() {

        Map<String, Integer> counts =
                new HashMap<>();

        counts.put("java", 2);

        counts.put("crawler", 1);

        List<String> links =
                Arrays.asList(
                        "https://example.com",
                        "https://openlibrary.org");

        return new Result(counts, links);
    }
}

/* ===================== PAGE PARSER FACTORY ===================== */

interface PageParserFactory {

    /*
     * RETURN PAGE PARSER FOR URL
     */
    PageParser get(String url);
}

/* ===================== FACTORY IMPLEMENTATION ===================== */

class PageParserFactoryImpl
        implements PageParserFactory {

    @Override
    public PageParser get(String url) {

        return new PageParserImpl(url);
    }
}

/* ===================== MAIN ===================== */

public class Main {

    public static void main(String[] args) {

        PageParserFactory factory =
                new PageParserFactoryImpl();

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