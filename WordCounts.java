package com.udacity.webcrawler;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Utility class for sorting word count maps.
 */
final class WordCounts {

    /**
     * Given an unsorted map of word counts, returns a new map
     * containing only the top {@code popularWordCount} entries,
     * sorted according to {@link WordCountComparator}.
     *
     * Sorting Rules:
     * <ol>
     *     <li>Higher word frequency first</li>
     *     <li>Longer words first</li>
     *     <li>Alphabetical order for ties</li>
     * </ol>
     *
     * @param wordCounts       the unsorted word count map
     * @param popularWordCount number of top words to include
     * @return sorted map containing the most popular words
     */
    static Map<String, Integer> sort(
            Map<String, Integer> wordCounts,
            int popularWordCount) {

        return wordCounts.entrySet()
                .stream()
                .sorted(new WordCountComparator())
                .limit(popularWordCount)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (a, b) -> a,
                        LinkedHashMap::new
                ));
    }

    /**
     * Comparator used for sorting word counts.
     *
     * Sorting Priority:
     * <ol>
     *     <li>Higher word count first</li>
     *     <li>Longer word length first</li>
     *     <li>Alphabetical order ascending</li>
     * </ol>
     */
    private static final class WordCountComparator
            implements Comparator<Map.Entry<String, Integer>> {

        @Override
        public int compare(
                Map.Entry<String, Integer> a,
                Map.Entry<String, Integer> b) {

            // Compare by frequency (descending)
            int frequencyCompare =
                    Integer.compare(b.getValue(), a.getValue());

            if (frequencyCompare != 0) {
                return frequencyCompare;
            }

            // Compare by word length (descending)
            int lengthCompare =
                    Integer.compare(
                            b.getKey().length(),
                            a.getKey().length());

            if (lengthCompare != 0) {
                return lengthCompare;
            }

            // Compare alphabetically (ascending)
            return a.getKey().compareTo(b.getKey());
        }
    }

    /**
     * Prevent instantiation.
     */
    private WordCounts() {
    }
}