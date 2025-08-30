package com.example.jainshruth;

import java.io.Serializable;

public class WordModel implements Serializable {
    private String hindiWord;
    private String hinglishWord;
    private String meaning;
    private String granth;
    private String reference;
    private String description;

    public WordModel(String hindiWord, String hinglishWord, String meaning,
                     String Granth, String reference, String description) {
        this.hindiWord = hindiWord;
        this.hinglishWord = hinglishWord;
        this.meaning = meaning;
        this.granth = Granth;
        this.reference = reference;
        this.description = description;
    }

    public String getHindiWord() {
        return hindiWord;
    }

    public String getHinglishWord() {
        return hinglishWord;
    }

    public String getMeaning() {
        return meaning;
    }

    public String getGranth() {
        return granth;
    }

    public String getReference() {
        return reference;
    }

    public String getDescription() {
        return description;
    }
}
