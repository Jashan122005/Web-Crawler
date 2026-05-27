package com.udacity.webcrawler;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static com.google.common.truth.Truth.assertWithMessage;

/**
 * Tests for WordCounts utility.
 */
public final class WordCountsTest {

    @Test
    public void testBasicOrder() {

        Map<String, Integer> unsortedCounts =
                new HashMap<>();

        unsortedCounts.put("the", 2);
        unsortedCounts.put("quick", 1);
        unsortedCounts.put("brown", 1);
        unsortedCounts.put("fox", 1);
        unsortedCounts.put("jumped", 1);
        unsortedCounts.put("over", 1);
        unsortedCounts.put("lazy", 1);
        unsortedCounts.put("dog", 1);

        Map<String, Integer> result =
                WordCounts.sort(
                        unsortedCounts,
                        4);

        assertWithMessage(
                "Returned the wrong number of popular words")

                .that(result)

                .hasSize(4);

        assertWithMessage(
                "Returned incorrect entry")

                .that(result)

                .containsEntry("the", 2);

        assertWithMessage(
                "Returned incorrect entry")

                .that(result)

                .containsEntry("jumped", 1);

        assertWithMessage(
                "Returned incorrect entry")

                .that(result)

                .containsEntry("brown", 1);

        assertWithMessage(
                "Returned incorrect entry")

                .that(result)

                .containsEntry("quick", 1);

        assertWithMessage(
                "Wrong ordering")

                .that(result.entrySet())

                .containsExactly(

                        Map.entry("the", 2),

                        Map.entry("jumped", 1),

                        Map.entry("brown", 1),

                        Map.entry("quick", 1))

                .inOrder();
    }

    @Test
    public void testNotEnoughWords() {

        Map<String, Integer> unsortedCounts =
                new HashMap<>();

        unsortedCounts.put("the", 2);
        unsortedCounts.put("quick", 1);
        unsortedCounts.put("brown", 1);
        unsortedCounts.put("fox", 1);

        Map<String, Integer> result =
                WordCounts.sort(
                        unsortedCounts,
                        5);

        assertWithMessage(
                "Returned wrong number of words")

                .that(result)

                .hasSize(4);

        assertWithMessage(
                "Returned incorrect entry")

                .that(result)

                .containsEntry("the", 2);

        assertWithMessage(
                "Returned incorrect entry")

                .that(result)

                .containsEntry("brown", 1);

        assertWithMessage(
                "Returned incorrect entry")

                .that(result)

                .containsEntry("quick", 1);

        assertWithMessage(
                "Returned incorrect entry")

                .that(result)

                .containsEntry("fox", 1);

        assertWithMessage(
                "Wrong ordering")

                .that(result.entrySet())

                .containsExactly(

                        Map.entry("the", 2),

                        Map.entry("brown", 1),

                        Map.entry("quick", 1),

                        Map.entry("fox", 1))

                .inOrder();
    }
}