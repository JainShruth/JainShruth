package com.example.jainshruth;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class HighlightNoteManager {

    private static final String PREF_NAME = "granth_highlights";

    /**
     * Save a new highlight for a specific granth URL.
     */
    public static void saveHighlight(Context context, String granthUrl, GranthHighlight newHighlight) {
        List<GranthHighlight> highlights = getHighlights(context, granthUrl);

        // Prevent duplicate highlights
        for (GranthHighlight h : highlights) {
            if (h.getSelectedText().equalsIgnoreCase(newHighlight.getSelectedText())) {
                return;
            }
        }

        highlights.add(newHighlight);

        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String json = new Gson().toJson(highlights);
        prefs.edit().putString(granthUrl, json).apply();
    }

    /**
     * Retrieve all highlights for a specific granth.
     */
    public static List<GranthHighlight> getHighlights(Context context, String granthUrl) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String json = prefs.getString(granthUrl, null);

        if (json != null) {
            try {
                Type listType = new TypeToken<List<GranthHighlight>>() {}.getType();
                return new Gson().fromJson(json, listType);
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
            }
        }

        return new ArrayList<>();
    }

    /**
     * Retrieve a specific highlight by selected text.
     */
    public static GranthHighlight getHighlightByText(Context context, String granthUrl, String selectedText) {
        List<GranthHighlight> highlights = getHighlights(context, granthUrl);
        for (GranthHighlight h : highlights) {
            if (h.getSelectedText().equalsIgnoreCase(selectedText)) {
                return h;
            }
        }
        return null;
    }

    /**
     * Remove a specific highlight by selected text.
     */
    public static void removeHighlight(Context context, String granthUrl, String selectedText) {
        List<GranthHighlight> highlights = getHighlights(context, granthUrl);
        List<GranthHighlight> updatedList = new ArrayList<>();

        for (GranthHighlight h : highlights) {
            if (!h.getSelectedText().equalsIgnoreCase(selectedText)) {
                updatedList.add(h);
            }
        }

        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String json = new Gson().toJson(updatedList);
        prefs.edit().putString(granthUrl, json).apply();
    }

    /**
     * Check if a specific text is already highlighted.
     */
    public static boolean contains(Context context, String granthUrl, String selectedText) {
        List<GranthHighlight> highlights = getHighlights(context, granthUrl);
        for (GranthHighlight h : highlights) {
            if (h.getSelectedText().equalsIgnoreCase(selectedText)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Clear all highlights for a specific granth.
     */
    public static void clearHighlights(Context context, String granthUrl) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
                .edit()
                .remove(granthUrl)
                .apply();
    }

    /**
     * Get all stored granth URLs (keys).
     */
    public static List<String> getAllGranthUrls(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return new ArrayList<>(prefs.getAll().keySet());
    }
}
