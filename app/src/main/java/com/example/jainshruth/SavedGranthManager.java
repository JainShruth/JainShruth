package com.example.jainshruth;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SavedGranthManager {

    private static final String PREF_NAME = "saved_granths";
    private static final String KEY_SAVED = "saved_list";

    private static final Gson gson = new Gson();

    // ✅ Get full saved GranthModel list
    public static List<GranthModel> getSavedGranths(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String json = prefs.getString(KEY_SAVED, null);
        if (json != null) {
            Type type = new TypeToken<List<GranthModel>>() {}.getType();
            return gson.fromJson(json, type);
        }
        return new ArrayList<>();
    }

    // ✅ Save a new granth
    public static void saveGranth(Context context, GranthModel granth) {
        List<GranthModel> saved = getSavedGranths(context);
        for (GranthModel g : saved) {
            if (g.getGranthLink().equals(granth.getGranthLink())) return; // already exists
        }
        saved.add(granth);
        saveList(context, saved);
    }

    // ✅ Remove granth by link
    public static void removeGranth(Context context, String granthLink) {
        List<GranthModel> saved = getSavedGranths(context);
        for (int i = 0; i < saved.size(); i++) {
            if (saved.get(i).getGranthLink().equals(granthLink)) {
                saved.remove(i);
                break;
            }
        }
        saveList(context, saved);
    }

    // ✅ Check if a granth is saved
    public static boolean isSaved(Context context, String granthLink) {
        List<GranthModel> saved = getSavedGranths(context);
        for (GranthModel g : saved) {
            if (g.getGranthLink().equals(granthLink)) return true;
        }
        return false;
    }

    // ✅ Return list of saved granth links
    public static List<String> getSavedLinks(Context context) {
        List<GranthModel> savedList = getSavedGranths(context);
        List<String> links = new ArrayList<>();
        for (GranthModel granth : savedList) {
            links.add(granth.getGranthLink());
        }
        return links;
    }

    // 🔐 Private: Save full list back to SharedPreferences
    private static void saveList(Context context, List<GranthModel> list) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String json = gson.toJson(list);
        prefs.edit().putString(KEY_SAVED, json).apply();
    }
}
