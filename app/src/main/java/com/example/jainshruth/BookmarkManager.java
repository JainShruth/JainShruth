package com.example.jainshruth;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class BookmarkManager {

    private static final String PREF_NAME = "bookmarks_pref";
    private static final String KEY_BOOKMARKS = "bookmarked_words";

    private final SharedPreferences prefs;
    private final Gson gson;

    public BookmarkManager(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        gson = new Gson();
    }

    public void saveBookmark(WordModel word) {
        List<WordModel> bookmarks = getBookmarks();
        if (!isBookmarked(word)) {
            bookmarks.add(word);
            saveList(bookmarks);
        }
    }

    public void removeBookmark(WordModel word) {
        List<WordModel> bookmarks = getBookmarks();
        bookmarks.removeIf(w -> w.getHindiWord().equals(word.getHindiWord()));
        saveList(bookmarks);
    }

    public List<WordModel> getBookmarks() {
        String json = prefs.getString(KEY_BOOKMARKS, "");
        if (json.isEmpty()) return new ArrayList<>();
        Type type = new TypeToken<List<WordModel>>() {}.getType();
        return gson.fromJson(json, type);
    }

    public boolean isBookmarked(WordModel word) {
        for (WordModel w : getBookmarks()) {
            if (w.getHindiWord().equals(word.getHindiWord())) {
                return true;
            }
        }
        return false;
    }

    private void saveList(List<WordModel> list) {
        String json = gson.toJson(list);
        prefs.edit().putString(KEY_BOOKMARKS, json).apply();
    }
}
