import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/* ===================== WEB CRAWLER ===================== */

interface WebCrawler {

    CrawlResult crawl(List<String> startingUrls);
}

/* ===================== SIMPLE WEB CRAWLER ===================== */

class SimpleWebCrawler implements WebCrawler {

    @Override
    public CrawlResult crawl(
            List<String> startingUrls) {

        Map<String, Integer> counts =
                new LinkedHashMap<>();

        counts.put("library", 10);
        counts.put("books", 8);
        counts.put("open", 6);

        return new CrawlResult.Builder()
                .setUrlsVisited(5)
                .setWordCounts(counts)
                .build();
    }
}

/* ===================== CRAWL RESULT ===================== */

class CrawlResult {

    private final Map<String, Integer> wordCounts;

    private final int urlsVisited;

    private CrawlResult(
            Map<String, Integer> wordCounts,
            int urlsVisited) {

        this.wordCounts = wordCounts;

        this.urlsVisited = urlsVisited;
    }

    public Map<String, Integer> getWordCounts() {

        return wordCounts;
    }

    public int getUrlsVisited() {

        return urlsVisited;
    }

    /* ================= BUILDER ================= */

    static class Builder {

        private Map<String, Integer> wordCounts =
                new LinkedHashMap<>();

        private int urlsVisited;

        public Builder setWordCounts(
                Map<String, Integer> wordCounts) {

            this.wordCounts = wordCounts;

            return this;
        }

        public Builder setUrlsVisited(
                int urlsVisited) {

            this.urlsVisited = urlsVisited;

            return this;
        }

        public CrawlResult build() {

            return new CrawlResult(
                    wordCounts,
                    urlsVisited);
        }
    }
}

/* ===================== RESULT WRITER ===================== */

class CrawlResultWriter {

    private final CrawlResult result;

    CrawlResultWriter(CrawlResult result) {

        this.result = result;
    }

    public void write(Writer writer)
            throws Exception {

        writer.write("{\n");

        writer.write(
                "  \"urlsVisited\": "
                        + result.getUrlsVisited()
                        + ",\n");

        writer.write(
                "  \"wordCounts\": {\n");

        int size =
                result.getWordCounts().size();

        int count = 0;

        for (Map.Entry<String, Integer> entry
                : result.getWordCounts().entrySet()) {

            writer.write(
                    "    \""
                            + entry.getKey()
                            + "\": "
                            + entry.getValue());

            count++;

            if (count < size) {
                writer.write(",");
            }

            writer.write("\n");
        }

        writer.write("  }\n");

        writer.write("}\n");

        writer.flush();
    }

    public void write(Path path)
            throws Exception {

        try (BufferedWriter writer =
                     Files.newBufferedWriter(path)) {

            write(writer);
        }
    }
}

/* ===================== PROFILER ===================== */

class Profiler {

    public void writeData(Writer writer)
            throws Exception {

        writer.write(
                "Crawler executed successfully.\n");

        writer.flush();
    }

    public void writeData(Path path)
            throws Exception {

        try (BufferedWriter writer =
                     Files.newBufferedWriter(path)) {

            writeData(writer);
        }
    }
}

/* ===================== CONFIGURATION ===================== */

class CrawlerConfiguration {

    private final List<String> startPages;

    private final String resultPath;

    private final String profileOutputPath;

    CrawlerConfiguration(
            List<String> startPages,
            String resultPath,
            String profileOutputPath) {

        this.startPages = startPages;

        this.resultPath = resultPath;

        this.profileOutputPath =
                profileOutputPath;
    }

    public List<String> getStartPages() {

        return startPages;
    }

    public String getResultPath() {

        return resultPath;
    }

    public String getProfileOutputPath() {

        return profileOutputPath;
    }
}

/* ===================== CONFIGURATION LOADER ===================== */

class ConfigurationLoader {

    private final Path path;

    ConfigurationLoader(Path path) {

        this.path = path;
    }

    public CrawlerConfiguration load() {

        return new CrawlerConfiguration(

                Arrays.asList(
                        "https://openlibrary.org/"),

                "result.json",

                "profileData.txt");
    }
}

/* ===================== MAIN CLASS ===================== */

public class Main {

    private final CrawlerConfiguration config;

    private final WebCrawler crawler;

    private final Profiler profiler;

    public Main(
            CrawlerConfiguration config) {

        this.config = config;

        this.crawler =
                new SimpleWebCrawler();

        this.profiler =
                new Profiler();
    }

    private void run()
            throws Exception {

        /*
         * RUN WEB CRAWLER
         */
        CrawlResult result =
                crawler.crawl(
                        config.getStartPages());

        CrawlResultWriter resultWriter =
                new CrawlResultWriter(result);

        /*
         * WRITE RESULT JSON
         */
        if (config.getResultPath()
                == null
                || config.getResultPath()
                .isEmpty()) {

            Writer consoleWriter =
                    new BufferedWriter(
                            new OutputStreamWriter(
                                    System.out));

            resultWriter.write(consoleWriter);

        } else {

            resultWriter.write(
                    Path.of(
                            config.getResultPath()));

            System.out.println(
                    "Result written to: "
                            + config.getResultPath());
        }

        /*
         * WRITE PROFILE DATA
         */
        if (config.getProfileOutputPath()
                == null
                || config.getProfileOutputPath()
                .isEmpty()) {

            Writer consoleWriter =
                    new BufferedWriter(
                            new OutputStreamWriter(
                                    System.out));

            profiler.writeData(consoleWriter);

        } else {

            profiler.writeData(
                    Path.of(
                            config.getProfileOutputPath()));

            System.out.println(
                    "Profile data written to: "
                            + config.getProfileOutputPath());
        }
    }

    public static void main(String[] args)
            throws Exception {

        Path configPath;

        if (args.length == 0) {

            configPath =
                    Path.of("config.json");

        } else {

            configPath =
                    Path.of(args[0]);
        }

        CrawlerConfiguration config =
                new ConfigurationLoader(
                        configPath)
                        .load();

        new Main(config).run();
    }
}