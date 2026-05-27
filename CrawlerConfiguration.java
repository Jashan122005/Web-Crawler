import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.Reader;
import java.io.UncheckedIOException;

import java.nio.file.Files;
import java.nio.file.Path;

import java.time.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
/* ===================== CRAWLER CONFIGURATION ===================== */

class CrawlerConfiguration {

    private final List<String> startPages;
    private final List<Pattern> ignoredUrls;
    private final List<Pattern> ignoredWords;
    private final int parallelism;
    private final String implementationOverride;
    private final int maxDepth;
    private final Duration timeout;
    private final int popularWordCount;
    private final String profileOutputPath;
    private final String resultPath;

    private CrawlerConfiguration(Builder builder) {

        this.startPages = builder.startPages;
        this.ignoredUrls = builder.ignoredUrls;
        this.ignoredWords = builder.ignoredWords;
        this.parallelism = builder.parallelism;
        this.implementationOverride =
                builder.implementationOverride;
        this.maxDepth = builder.maxDepth;
        this.timeout = builder.timeout;
        this.popularWordCount =
                builder.popularWordCount;
        this.profileOutputPath =
                builder.profileOutputPath;
        this.resultPath = builder.resultPath;
    }

    public List<String> getStartPages() {
        return startPages;
    }

    public List<Pattern> getIgnoredUrls() {
        return ignoredUrls;
    }

    public List<Pattern> getIgnoredWords() {
        return ignoredWords;
    }

    public int getParallelism() {
        return parallelism;
    }

    public String getImplementationOverride() {
        return implementationOverride;
    }

    public int getMaxDepth() {
        return maxDepth;
    }

    public Duration getTimeout() {
        return timeout;
    }

    public int getPopularWordCount() {
        return popularWordCount;
    }

    public String getProfileOutputPath() {
        return profileOutputPath;
    }

    public String getResultPath() {
        return resultPath;
    }

    /* ===================== BUILDER ===================== */

    static class Builder {

        private List<String> startPages =
                new ArrayList<>();

        private List<Pattern> ignoredUrls =
                new ArrayList<>();

        private List<Pattern> ignoredWords =
                new ArrayList<>();

        private int parallelism = -1;

        private String implementationOverride =
                "";

        private int maxDepth = 0;

        private Duration timeout =
                Duration.ZERO;

        private int popularWordCount = 0;

        private String profileOutputPath =
                "";

        private String resultPath =
                "";

        public Builder setStartPages(
                List<String> startPages) {

            this.startPages = startPages;
            return this;
        }

        public Builder setIgnoredUrls(
                List<Pattern> ignoredUrls) {

            this.ignoredUrls = ignoredUrls;
            return this;
        }

        public Builder setIgnoredWords(
                List<Pattern> ignoredWords) {

            this.ignoredWords = ignoredWords;
            return this;
        }

        public Builder setParallelism(
                int parallelism) {

            this.parallelism = parallelism;
            return this;
        }

        public Builder setImplementationOverride(
                String implementationOverride) {

            this.implementationOverride =
                    implementationOverride;

            return this;
        }

        public Builder setMaxDepth(
                int maxDepth) {

            this.maxDepth = maxDepth;
            return this;
        }

        public Builder setTimeout(
                Duration timeout) {

            this.timeout = timeout;
            return this;
        }

        public Builder setPopularWordCount(
                int popularWordCount) {

            this.popularWordCount =
                    popularWordCount;

            return this;
        }

        public Builder setProfileOutputPath(
                String profileOutputPath) {

            this.profileOutputPath =
                    profileOutputPath;

            return this;
        }

        public Builder setResultPath(
                String resultPath) {

            this.resultPath = resultPath;
            return this;
        }

        public CrawlerConfiguration build() {

            return new CrawlerConfiguration(this);
        }
    }
}

/* ===================== CONFIGURATION LOADER ===================== */

class ConfigurationLoader {

    private final Path path;

    public ConfigurationLoader(Path path) {

        this.path =
                Objects.requireNonNull(path);
    }

    /*
     * LOAD FROM FILE
     */
    public CrawlerConfiguration load() {

        try (Reader reader = Files.newBufferedReader(path)) {

            return read(reader);

        } catch (IOException e) {

            throw new UncheckedIOException(e);
        }
    }
    /*
     * LOAD FROM READER
     */
    public static CrawlerConfiguration read(Reader reader) {

        try {

            ObjectMapper mapper = new ObjectMapper();

            JsonNode root = mapper.readTree(reader);

            CrawlerConfiguration.Builder builder =
                    new CrawlerConfiguration.Builder();

            /*
             * START PAGES
             */
            JsonNode startPages = root.get("startPages");

            if (startPages != null) {

                for (JsonNode page : startPages) {

                    builder.addStartPages(
                            page.asText());
                }
            }

            /*
             * IGNORED URLS
             */
            JsonNode ignoredUrls = root.get("ignoredUrls");

            if (ignoredUrls != null) {

                for (JsonNode url : ignoredUrls) {

                    builder.addIgnoredUrls(
                            url.asText());
                }
            }

            /*
             * IGNORED WORDS
             */
            JsonNode ignoredWords = root.get("ignoredWords");

            if (ignoredWords != null) {

                for (JsonNode word : ignoredWords) {

                    builder.addIgnoredWords(
                            word.asText());
                }
            }

            /*
             * PARALLELISM
             */
            if (root.has("parallelism")) {

                builder.setParallelism(
                        root.get("parallelism").asInt());
            }

            /*
             * MAX DEPTH
             */
            if (root.has("maxDepth")) {

                builder.setMaxDepth(
                        root.get("maxDepth").asInt());
            }

            /*
             * TIMEOUT
             */
            if (root.has("timeoutSeconds")) {

                builder.setTimeout(
                        Duration.ofSeconds(
                                root.get("timeoutSeconds").asInt()));
            }

            /*
             * POPULAR WORD COUNT
             */
            if (root.has("popularWordCount")) {

                builder.setPopularWordCount(
                        root.get("popularWordCount").asInt());
            }

            /*
             * IMPLEMENTATION OVERRIDE
             */
            if (root.has("implementationOverride")) {

                builder.setImplementationOverride(
                        root.get("implementationOverride").asText());
            }

            /*
             * PROFILE OUTPUT PATH
             */
            if (root.has("profileOutputPath")) {

                builder.setProfileOutputPath(
                        Path.of(
                                root.get("profileOutputPath").asText()));
            }

            /*
             * RESULT PATH
             */
            if (root.has("resultPath")) {

                builder.setResultPath(
                        Path.of(
                                root.get("resultPath").asText()));
            }

            return builder.build();

        } catch (IOException e) {

            throw new UncheckedIOException(e);
        }
    }
}

/* ===================== MAIN ===================== */

public class Main {

    public static void main(String[] args)
            throws Exception {

        String json =
                "{"
                        + "\"startPages\": [\"http://example.com\"],"
                        + "\"parallelism\": 4,"
                        + "\"maxDepth\": 100,"
                        + "\"timeoutSeconds\": 10,"
                        + "\"popularWordCount\": 5"
                        + "}";

        Reader reader =
                new StringReader(json);

        CrawlerConfiguration config =
                ConfigurationLoader.read(reader);

        System.out.println(
                config.getStartPages());

        System.out.println(
                config.getParallelism());

        System.out.println(
                config.getMaxDepth());

        System.out.println(
                config.getTimeout());

        System.out.println(
                config.getPopularWordCount());
    }
}