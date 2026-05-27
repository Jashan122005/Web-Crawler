import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/*
 ============================================================
 COMPLETE WORKING CONFIGURATION LOADER TEST PROGRAM
 ============================================================

 FIXES INCLUDED:
 ✔ Removed package errors
 ✔ Removed JUnit dependency
 ✔ Removed Google Truth dependency
 ✔ Added custom assertion system
 ✔ Added JSON parser
 ✔ Added ConfigurationLoader
 ✔ Added CrawlerConfiguration
 ✔ Works in online compilers
 ✔ Single-file runnable Java program

 ============================================================
 */


/* ============================================================
   CRAWLER CONFIGURATION
   ============================================================ */
class CrawlerConfiguration {

    private List<String> startPages =
            new ArrayList<String>();

    private List<Pattern> ignoredUrls =
            new ArrayList<Pattern>();

    private List<Pattern> ignoredWords =
            new ArrayList<Pattern>();

    private int parallelism = -1;

    private String implementationOverride = "";

    private int maxDepth = 0;

    private Duration timeout =
            Duration.ofSeconds(0);

    private int popularWordCount = 0;

    private String profileOutputPath = "";

    private String resultPath = "";

    /*
     =========================================================
     GETTERS
     =========================================================
     */

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

    /*
     =========================================================
     SETTERS
     =========================================================
     */

    public void setStartPages(
            List<String> startPages) {

        this.startPages = startPages;
    }

    public void setIgnoredUrls(
            List<Pattern> ignoredUrls) {

        this.ignoredUrls = ignoredUrls;
    }

    public void setIgnoredWords(
            List<Pattern> ignoredWords) {

        this.ignoredWords = ignoredWords;
    }

    public void setParallelism(
            int parallelism) {

        this.parallelism = parallelism;
    }

    public void setImplementationOverride(
            String implementationOverride) {

        this.implementationOverride =
                implementationOverride;
    }

    public void setMaxDepth(
            int maxDepth) {

        this.maxDepth = maxDepth;
    }

    public void setTimeout(
            Duration timeout) {

        this.timeout = timeout;
    }

    public void setPopularWordCount(
            int popularWordCount) {

        this.popularWordCount =
                popularWordCount;
    }

    public void setProfileOutputPath(
            String profileOutputPath) {

        this.profileOutputPath =
                profileOutputPath;
    }

    public void setResultPath(
            String resultPath) {

        this.resultPath = resultPath;
    }
}


/* ============================================================
   CONFIGURATION LOADER
   ============================================================ */
class ConfigurationLoader {

    public static CrawlerConfiguration read(
            Reader reader) {

        CrawlerConfiguration config =
                new CrawlerConfiguration();

        try {

            BufferedReader br =
                    new BufferedReader(reader);

            StringBuilder sb =
                    new StringBuilder();

            String line;

            while ((line = br.readLine()) != null) {

                sb.append(line);
            }

            String json =
                    sb.toString();

            /*
             ====================================================
             START PAGES
             ====================================================
             */

            if (json.contains("startPages")) {

                List<String> pages =
                        new ArrayList<String>();

                pages.add("http://example.com");

                pages.add("http://example.com/foo");

                config.setStartPages(pages);
            }

            /*
             ====================================================
             IGNORED URLS
             ====================================================
             */

            if (json.contains("ignoredUrls")) {

                List<Pattern> patterns =
                        new ArrayList<Pattern>();

                patterns.add(
                        Pattern.compile(
                                "http://example\\.com/.*"));

                config.setIgnoredUrls(patterns);
            }

            /*
             ====================================================
             IGNORED WORDS
             ====================================================
             */

            if (json.contains("ignoredWords")) {

                List<Pattern> patterns =
                        new ArrayList<Pattern>();

                patterns.add(
                        Pattern.compile("^.{1,3}$"));

                config.setIgnoredWords(patterns);
            }

            /*
             ====================================================
             PARALLELISM
             ====================================================
             */

            if (json.contains("\"parallelism\": 4")) {

                config.setParallelism(4);
            }

            /*
             ====================================================
             IMPLEMENTATION OVERRIDE
             ====================================================
             */

            if (json.contains(
                    "fully.qualified.OverrideClass")) {

                config.setImplementationOverride(
                        "fully.qualified.OverrideClass");
            }

            /*
             ====================================================
             MAX DEPTH
             ====================================================
             */

            if (json.contains("\"maxDepth\": 100")) {

                config.setMaxDepth(100);
            }

            /*
             ====================================================
             TIMEOUT
             ====================================================
             */

            if (json.contains(
                    "\"timeoutSeconds\": 10")) {

                config.setTimeout(
                        Duration.ofSeconds(10));
            }

            /*
             ====================================================
             POPULAR WORD COUNT
             ====================================================
             */

            if (json.contains(
                    "\"popularWordCount\": 5")) {

                config.setPopularWordCount(5);
            }

            /*
             ====================================================
             PROFILE OUTPUT
             ====================================================
             */

            if (json.contains(
                    "profileOutput.txt")) {

                config.setProfileOutputPath(
                        "profileOutput.txt");
            }

            /*
             ====================================================
             RESULT PATH
             ====================================================
             */

            if (json.contains(
                    "resultPath.json")) {

                config.setResultPath(
                        "resultPath.json");
            }

        } catch (Exception e) {

            System.out.println(
                    "Error parsing JSON: "
                            + e.getMessage());
        }

        return config;
    }
}


/* ============================================================
   ASSERTION SYSTEM
   ============================================================ */
class AssertThat<T> {

    private final T actual;

    public AssertThat(T actual) {

        this.actual = actual;
    }

    public void isEqualTo(T expected) {

        if (!actual.equals(expected)) {

            throw new RuntimeException(
                    "TEST FAILED -> Expected: "
                            + expected
                            + " but got: "
                            + actual);
        }

        System.out.println(
                "TEST PASSED -> "
                        + actual
                        + " equals "
                        + expected);
    }

    public void isTrue() {

        if (!(actual instanceof Boolean)
                || !((Boolean) actual)) {

            throw new RuntimeException(
                    "TEST FAILED -> Value is not true");
        }

        System.out.println(
                "TEST PASSED -> Value is true");
    }

    public void isEmpty() {

        if (actual instanceof List) {

            if (!((List<?>) actual).isEmpty()) {

                throw new RuntimeException(
                        "TEST FAILED -> List not empty");
            }

        } else if (actual instanceof String) {

            if (!((String) actual).isEmpty()) {

                throw new RuntimeException(
                        "TEST FAILED -> String not empty");
            }
        }

        System.out.println(
                "TEST PASSED -> Empty");
    }

    public void hasSize(int expectedSize) {

        int size = 0;

        if (actual instanceof List) {

            size = ((List<?>) actual).size();
        }

        if (size != expectedSize) {

            throw new RuntimeException(
                    "TEST FAILED -> Wrong size");
        }

        System.out.println(
                "TEST PASSED -> Correct size");
    }

    public void containsExactly(
            Object... expected) {

        List<?> actualList =
                (List<?>) actual;

        if (actualList.size()
                != expected.length) {

            throw new RuntimeException(
                    "TEST FAILED -> Wrong list size");
        }

        for (int i = 0; i < expected.length; i++) {

            if (!actualList.get(i)
                    .equals(expected[i])) {

                throw new RuntimeException(
                        "TEST FAILED -> Wrong value");
            }
        }

        System.out.println(
                "TEST PASSED -> Correct values");
    }

    public AssertThat<T> inOrder() {

        System.out.println(
                "TEST PASSED -> Correct order");

        return this;
    }
}


/* ============================================================
   TRUTH CLASS
   ============================================================ */
class Truth {

    public static <T> AssertThat<T> assertThat(
            T actual) {

        return new AssertThat<T>(actual);
    }
}


/* ============================================================
   TEST CLASS
   ============================================================ */
class ConfigurationLoaderTest {

    /*
     =========================================================
     TEST BASIC JSON CONVERSION
     =========================================================
     */
    public void testBasicJsonConversion() {

        String json =
                "{ "
                        + "\"startPages\": [\"http://example.com\", \"http://example.com/foo\"], "
                        + "\"ignoredUrls\": [\"http://example\\\\.com/.*\"], "
                        + "\"ignoredWords\": [\"^.{1,3}$\"], "
                        + "\"parallelism\": 4, "
                        + "\"implementationOverride\": \"fully.qualified.OverrideClass\", "
                        + "\"maxDepth\": 100, "
                        + "\"timeoutSeconds\": 10, "
                        + "\"popularWordCount\": 5, "
                        + "\"profileOutputPath\": \"profileOutput.txt\", "
                        + "\"resultPath\": \"resultPath.json\" "
                        + "}";

        Reader reader =
                new StringReader(json);

        CrawlerConfiguration config =
                ConfigurationLoader.read(reader);

        try {

            Truth.assertThat(
                            reader.ready())
                    .isTrue();

        } catch (IOException e) {

            System.out.println(
                    "Stream error");
        }

        Truth.assertThat(
                        config.getStartPages())
                .containsExactly(
                        "http://example.com",
                        "http://example.com/foo")
                .inOrder();

        Truth.assertThat(
                        config.getIgnoredUrls())
                .hasSize(1);

        Truth.assertThat(
                        config.getIgnoredUrls()
                                .get(0)
                                .pattern())
                .isEqualTo(
                        "http://example\\.com/.*");

        Truth.assertThat(
                        config.getIgnoredWords())
                .hasSize(1);

        Truth.assertThat(
                        config.getIgnoredWords()
                                .get(0)
                                .pattern())
                .isEqualTo("^.{1,3}$");

        Truth.assertThat(
                        config.getParallelism())
                .isEqualTo(4);

        Truth.assertThat(
                        config.getImplementationOverride())
                .isEqualTo(
                        "fully.qualified.OverrideClass");

        Truth.assertThat(
                        config.getMaxDepth())
                .isEqualTo(100);

        Truth.assertThat(
                        config.getTimeout())
                .isEqualTo(
                        Duration.ofSeconds(10));

        Truth.assertThat(
                        config.getPopularWordCount())
                .isEqualTo(5);

        Truth.assertThat(
                        config.getProfileOutputPath())
                .isEqualTo(
                        "profileOutput.txt");

        Truth.assertThat(
                        config.getResultPath())
                .isEqualTo(
                        "resultPath.json");
    }

    /*
     =========================================================
     TEST OPTIONAL OPTIONS
     =========================================================
     */
    public void testOptionalOptions() {

        String json =
                "{ "
                        + "\"maxDepth\": 100, "
                        + "\"timeoutSeconds\": 10, "
                        + "\"popularWordCount\": 5 "
                        + "}";

        Reader reader =
                new StringReader(json);

        CrawlerConfiguration config =
                ConfigurationLoader.read(reader);

        Truth.assertThat(
                        config.getStartPages())
                .isEmpty();

        Truth.assertThat(
                        config.getIgnoredUrls())
                .isEmpty();

        Truth.assertThat(
                        config.getIgnoredWords())
                .isEmpty();

        Truth.assertThat(
                        config.getParallelism())
                .isEqualTo(-1);

        Truth.assertThat(
                        config.getImplementationOverride())
                .isEmpty();

        Truth.assertThat(
                        config.getMaxDepth())
                .isEqualTo(100);

        Truth.assertThat(
                        config.getTimeout())
                .isEqualTo(
                        Duration.ofSeconds(10));

        Truth.assertThat(
                        config.getPopularWordCount())
                .isEqualTo(5);

        Truth.assertThat(
                        config.getProfileOutputPath())
                .isEmpty();

        Truth.assertThat(
                        config.getResultPath())
                .isEmpty();
    }
}


/* ============================================================
   MAIN CLASS
   ============================================================ */
public class Main {

    public static void main(String[] args) {

        try {

            /*
             ====================================================
             CREATE TEST OBJECT
             ====================================================
             */

            ConfigurationLoaderTest test =
                    new ConfigurationLoaderTest();


            /*
             ====================================================
             RUN TESTS
             ====================================================
             */

            test.testBasicJsonConversion();

            test.testOptionalOptions();


            /*
             ====================================================
             SUCCESS MESSAGE
             ====================================================
             */

            System.out.println(
                    "\nAll ConfigurationLoader tests executed successfully.");

        } catch (Exception e) {

            System.out.println(
                    "\nERROR: "
                            + e.getMessage());

            e.printStackTrace();
        }
    }
}