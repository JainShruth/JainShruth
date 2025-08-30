package com.example.jainshruth;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class SavedWordsManager {

    private static final String PREF_NAME = "SavedWordsPref";
    private static final String KEY = "savedWords";

    public static ArrayList<WordModel> getSavedWords(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String json = prefs.getString(KEY, null);
        if (json != null) {
            Type type = new TypeToken<ArrayList<WordModel>>(){}.getType();
            return new Gson().fromJson(json, type);
        } else {
            return new ArrayList<>();
        }
    }

    public static void saveWords(Context context, ArrayList<WordModel> list) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        String json = new Gson().toJson(list);
        editor.putString(KEY, json);
        editor.apply();
    }
}
