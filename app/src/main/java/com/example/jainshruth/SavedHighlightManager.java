package com.example.jainshruth;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.Map;

public class SavedHighlightManager {

    private static final String PREF_NAME = "granth_highlights";

    /**
     * Returns all highlights stored, key = granthUrl, value = JSON string of highlights
     */
    public static Map<String, String> getAllHighlights(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        Map<String, ?> all = prefs.getAll();
        Map<String, String> result = new HashMap<>();

        for (Map.Entry<String, ?> entry : all.entrySet()) {
            if (entry.getValue() instanceof String) {
                result.put(entry.getKey(), (String) entry.getValue());
            }
        }

        return result;
    }

    /**
     * Completely clears all saved highlights from all granths
     */
    public static void clearAll(Context context) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
                .edit()
                .clear()
                .apply();
    }

    /**
     * Optional: Remove highlights for a specific granth
     */
    public static void clearHighlightsForGranth(Context context, String granthUrl) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
                .edit()
                .remove(granthUrl)
                .apply();
    }
}
