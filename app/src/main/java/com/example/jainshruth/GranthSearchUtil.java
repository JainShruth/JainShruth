package com.example.jainshruth;

import java.util.ArrayList;
import java.util.List;

public class GranthSearchUtil {

    /**
     * Finds all occurrences of a query in a given content string.
     * Case-insensitive. Partial matches allowed.
     *
     * @param content The full HTML/plain text content
     * @param query   The search term
     * @return List of start indices of matched terms
     */
    public static List<Integer> findOccurrences(String content, String query) {
        List<Integer> positions = new ArrayList<>();
        if (content == null || query == null || query.trim().isEmpty()) return positions;

        String lowerContent = content.toLowerCase();
        String lowerQuery = query.toLowerCase();

        int index = lowerContent.indexOf(lowerQuery);
        while (index >= 0) {
            positions.add(index);
            index = lowerContent.indexOf(lowerQuery, index + 1);
        }
        return positions;
    }

    /**
     * Optional: Finds occurrences where query is a whole word only
     */
    public static List<Integer> findWholeWordMatches(String content, String query) {
        List<Integer> positions = new ArrayList<>();
        if (content == null || query == null || query.trim().isEmpty()) return positions;

        String[] words = content.split("\\s+");
        int cursor = 0;

        for (String word : words) {
            if (word.equalsIgnoreCase(query)) {
                positions.add(cursor);
            }
            cursor += word.length() + 1;
        }

        return positions;
    }
}
