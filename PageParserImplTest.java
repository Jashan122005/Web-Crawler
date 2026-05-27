import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;

/*
 ============================================================
 COMPLETE WORKING CRAWL RESULT WRITER TEST PROGRAM
 ============================================================

 FIXES INCLUDED:
 ✔ Removed package errors
 ✔ Removed JUnit dependency
 ✔ Removed Google Truth dependency
 ✔ Added custom assertion framework
 ✔ Added CrawlResult implementation
 ✔ Added CrawlResultWriter implementation
 ✔ Added CloseableStringWriter implementation
 ✔ Works in online compilers
 ✔ Single-file runnable Java program

 ============================================================
 */


/* ============================================================
   CRAWL RESULT
   ============================================================ */
class CrawlResult {

    private final int urlsVisited;

    private final Map<String, Integer> wordCounts;

    private CrawlResult(
            int urlsVisited,
            Map<String, Integer> wordCounts) {

        this.urlsVisited = urlsVisited;
        this.wordCounts = wordCounts;
    }

    public int getUrlsVisited() {
        return urlsVisited;
    }

    public Map<String, Integer> getWordCounts() {
        return wordCounts;
    }

    /*
     =========================================================
     BUILDER
     =========================================================
     */
    public static class Builder {

        private int urlsVisited;

        private Map<String, Integer> wordCounts =
                new LinkedHashMap<String, Integer>();

        public Builder setUrlsVisited(
                int urlsVisited) {

            this.urlsVisited = urlsVisited;

            return this;
        }

        public Builder setWordCounts(
                Map<String, Integer> wordCounts) {

            this.wordCounts = wordCounts;

            return this;
        }

        public CrawlResult build() {

            return new CrawlResult(
                    urlsVisited,
                    wordCounts);
        }
    }
}


/* ============================================================
   CRAWL RESULT WRITER
   ============================================================ */
class CrawlResultWriter {

    private final CrawlResult result;

    public CrawlResultWriter(
            CrawlResult result) {

        this.result = result;
    }

    public void write(Writer writer)
            throws IOException {

        StringBuilder json =
                new StringBuilder();

        json.append("{\n");

        /*
         =======================================================
         WORD COUNTS
         =======================================================
         */

        json.append("  \"wordCounts\": {\n");

        int index = 0;

        int size =
                result.getWordCounts().size();

        for (Map.Entry<String, Integer> entry
                : result.getWordCounts().entrySet()) {

            json.append("    \"")
                    .append(entry.getKey())
                    .append("\": ")
                    .append(entry.getValue());

            if (index < size - 1) {
                json.append(",");
            }

            json.append("\n");

            index++;
        }

        json.append("  },\n");

        /*
         =======================================================
         URLS VISITED
         =======================================================
         */

        json.append("  \"urlsVisited\": ")
                .append(result.getUrlsVisited())
                .append("\n");

        json.append("}");

        writer.write(json.toString());

        writer.flush();
    }
}


/* ============================================================
   CLOSEABLE STRING WRITER
   ============================================================ */
class CloseableStringWriter extends StringWriter {

    private boolean closed = false;

    @Override
    public void close()
            throws IOException {

        closed = true;

        super.close();
    }

    public boolean isClosed() {

        return closed;
    }
}


/* ============================================================
   ASSERTION FRAMEWORK
   ============================================================ */
class AssertThat<T> {

    private final T actual;

    public AssertThat(T actual) {

        this.actual = actual;
    }

    public void isFalse() {

        if (!(actual instanceof Boolean)
                || ((Boolean) actual)) {

            throw new RuntimeException(
                    "TEST FAILED -> Expected false");
        }

        System.out.println(
                "TEST PASSED -> Value is false");
    }

    public void matches(Pattern pattern) {

        if (!(actual instanceof String)) {

            throw new RuntimeException(
                    "TEST FAILED -> Not a string");
        }

        String value =
                (String) actual;

        if (!pattern.matcher(value).matches()) {

            throw new RuntimeException(
                    "TEST FAILED -> Pattern mismatch");
        }

        System.out.println(
                "TEST PASSED -> Pattern matched successfully");
    }
}


/* ============================================================
   ASSERT WITH MESSAGE
   ============================================================ */
class AssertWithMessage {

    private final String message;

    public AssertWithMessage(
            String message) {

        this.message = message;
    }

    public <T> AssertThat<T> that(T actual) {

        System.out.println(
                "\nASSERTION: "
                        + message);

        return new AssertThat<T>(actual);
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

    public static AssertWithMessage assertWithMessage(
            String message) {

        return new AssertWithMessage(message);
    }
}


/* ============================================================
   TEST CLASS
   ============================================================ */
class CrawlResultWriterTest {

    public void testBasicJsonFormatting()
            throws Exception {

        /*
         =======================================================
         CREATE WORD COUNTS
         =======================================================
         */

        Map<String, Integer> counts =
                new LinkedHashMap<String, Integer>();

        counts.put("foo", 12);

        counts.put("bar", 1);

        counts.put("foobar", 98);


        /*
         =======================================================
         CREATE RESULT
         =======================================================
         */

        CrawlResult result =
                new CrawlResult.Builder()
                        .setUrlsVisited(17)
                        .setWordCounts(counts)
                        .build();


        /*
         =======================================================
         CREATE WRITER
         =======================================================
         */

        CrawlResultWriter resultWriter =
                new CrawlResultWriter(result);

        CloseableStringWriter stringWriter =
                new CloseableStringWriter();


        /*
         =======================================================
         WRITE JSON
         =======================================================
         */

        resultWriter.write(stringWriter);


        /*
         =======================================================
         VERIFY WRITER NOT CLOSED
         =======================================================
         */

        Truth.assertWithMessage(
                        "Streams should usually be closed in the same scope where they were created")
                .that(stringWriter.isClosed())
                .isFalse();


        /*
         =======================================================
         GET WRITTEN JSON
         =======================================================
         */

        String written =
                stringWriter.toString();


        /*
         =======================================================
         EXPECTED PATTERN
         =======================================================
         */

        Pattern expected =
                Pattern.compile(
                        ".*\\{"
                                + ".*\"wordCounts\".*:.*\\{"
                                + ".*\"foo\".*:.*12,"
                                + ".*\"bar\".*:.*1,"
                                + ".*\"foobar\".*:.*98"
                                + ".*}.*,.*"
                                + ".*\"urlsVisited\".*:.*17"
                                + ".*}.*",
                        Pattern.DOTALL);


        /*
         =======================================================
         VERIFY JSON FORMAT
         =======================================================
         */

        Truth.assertThat(written)
                .matches(expected);


        /*
         =======================================================
         PRINT OUTPUT
         =======================================================
         */

        System.out.println(
                "\nGenerated JSON:\n");

        System.out.println(written);
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

            CrawlResultWriterTest test =
                    new CrawlResultWriterTest();


            /*
             ====================================================
             RUN TEST
             ====================================================
             */

            test.testBasicJsonFormatting();


            /*
             ====================================================
             SUCCESS MESSAGE
             ====================================================
             */

            System.out.println(
                    "\nAll CrawlResultWriter tests executed successfully.");

        } catch (Exception e) {

            System.out.println(
                    "\nERROR: "
                            + e.getMessage());

            e.printStackTrace();
        }
    }
}