package com.example.jainshruth;

import android.app.AlertDialog;
import android.content.Context;
import android.text.Html;
import android.text.InputType;
import android.text.Spanned;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import java.util.List;

public class Utils {

    /**
     * Converts HTML to Spanned for rendering.
     */
    public static Spanned fromHtml(String html) {
        if (html == null) return Html.fromHtml("", Html.FROM_HTML_MODE_LEGACY);
        return Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
    }

    /**
     * Strip HTML tags and return plain text.
     */
    public static String stripHtml(String html) {
        if (html == null) return "";
        return Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY).toString().trim();
    }

    /**
     * Capitalize first letter.
     */
    public static String capitalize(String word) {
        if (word == null || word.isEmpty()) return "";
        return Character.toUpperCase(word.charAt(0)) + word.substring(1).toLowerCase();
    }

    /**
     * Is empty or null string check.
     */
    public static boolean isEmpty(String input) {
        return input == null || input.trim().isEmpty();
    }

    /**
     * Truncate long text.
     */
    public static String truncate(String text, int maxLength) {
        if (text == null || text.length() <= maxLength) return text;
        return text.substring(0, maxLength).trim() + "...";
    }

    /**
     * Show popup to enter user note.
     * @param context Activity context
     * @param callback Callback with user input
     */
    public static void showNotePopup(Context context, NoteCallback callback) {
        EditText input = new EditText(context);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        input.setHint("Write your note...");
        input.setMinLines(3);
        input.setPadding(24, 24, 24, 24);

        ScrollView scrollView = new ScrollView(context);
        scrollView.addView(input);

        LinearLayout layout = new LinearLayout(context);
        layout.setPadding(24, 24, 24, 24);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(scrollView);

        new AlertDialog.Builder(context)
                .setTitle("Add Note")
                .setView(layout)
                .setPositiveButton("Save", (dialog, which) -> {
                    String note = input.getText().toString().trim();
                    callback.onNoteSaved(note);
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    public interface NoteCallback {
        void onNoteSaved(String note);
    }

    /**
     * Injects previously saved highlights into WebView (optional, call onPageFinished).
     */
    public static void injectHighlights(android.webkit.WebView webView, List<GranthHighlight> highlights) {
        for (GranthHighlight h : highlights) {
            String js = "(function() {" +
                    "var bodyText = document.body.innerHTML;" +
                    "var target = " + toJsString(h.getSelectedText()) + ";" +
                    "var note = " + toJsString(h.getNote()) + ";" +
                    "if (target.length > 0 && bodyText.includes(target)) {" +
                    "document.body.innerHTML = bodyText.replace(target, '<span style=\"background-color:#FFEB3B\" title=\"' + note + '\">' + target + '</span>');" +
                    "}" +
                    "})();";
            webView.evaluateJavascript(js, null);
        }
    }

    /**
     * Escapes string for JS injection.
     */
    private static String toJsString(String text) {
        if (text == null) return "\"\"";
        return "\"" + text.replace("\"", "\\\"").replace("\n", "\\n") + "\"";
    }
}
