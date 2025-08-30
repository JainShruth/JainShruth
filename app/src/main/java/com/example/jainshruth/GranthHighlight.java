package com.example.jainshruth;

public class GranthHighlight {
    private String selectedText;
    private String note;

    public GranthHighlight(String selectedText, String note) {
        this.selectedText = selectedText;
        this.note = note;
    }

    public String getSelectedText() {
        return selectedText;
    }

    public void setSelectedText(String selectedText) {
        this.selectedText = selectedText;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
