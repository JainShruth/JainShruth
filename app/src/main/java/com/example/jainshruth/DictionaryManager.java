package com.example.jainshruth;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DictionaryManager {

    private static final String SHEET_URL = "https://opensheet.elk.sh/1TREF3vK38z49j4B2unwToC_s-59xG2CY2BPyI3UXSL0/Sheet1";
    private static final Map<String, String> dictionaryMap = new HashMap<>();
    private static boolean isLoaded = false;

    public interface DictionaryCallback {
        void onLoaded();
    }

    public static void loadDictionary(Context context, DictionaryCallback callback) {
        if (isLoaded) {
            callback.onLoaded();
            return;
        }

        RequestQueue queue = Volley.newRequestQueue(context);
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, SHEET_URL, null, response -> {
            try {
                for (int i = 0; i < response.length(); i++) {
                    JSONObject obj = response.getJSONObject(i);
                    String word = obj.optString("Word").toLowerCase();      // ✅ Fix here
                    String meaning = obj.optString("Meaning");              // ✅ Fix here
                    dictionaryMap.put(word, meaning);
                }
                isLoaded = true;
                callback.onLoaded();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, error -> Log.e("DictionaryManager", "Error loading dictionary"));
        queue.add(request);
    }

    public static String getMeaning(String word) {
        if (word == null) return null;
        return dictionaryMap.get(word.toLowerCase());
    }
}
