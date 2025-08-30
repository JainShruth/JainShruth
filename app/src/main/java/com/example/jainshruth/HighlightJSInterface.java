package com.example.jainshruth;

import android.content.Context;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

/**
 * Bridge between WebView's JS and Android code.
 */
public class HighlightJSInterface {
    private Context context;

    public HighlightJSInterface(Context context) {
        this.context = context;
    }

    /**
     * Called from JS when a highlight is clicked.
     */
    @JavascriptInterface
    public void onHighlightClicked(String text, String note) {
        Toast.makeText(context, "Note: " + note, Toast.LENGTH_LONG).show();
        // You can also show a dialog here or allow editing
    }

    /**
     * For debugging JS->Android
     */
    @JavascriptInterface
    public void showToast(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}