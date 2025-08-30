package com.example.jainshruth;

import java.io.Serializable;

public class GranthModel implements Serializable {
    private String titleHindi;
    private String titleHinglish;
    private String writer;
    private String anuyog;
    private String language;
    private String description;
    private String imageUrl;
    private String granthLink;
    private String tags;

    public GranthModel(String titleHindi, String titleHinglish, String writer,
                       String anuyog, String language, String description,
                       String imageUrl, String granthLink, String tags) {
        this.titleHindi = titleHindi;
        this.titleHinglish = titleHinglish;
        this.writer = writer;
        this.anuyog = anuyog;
        this.language = language;
        this.description = description;
        this.imageUrl = imageUrl;
        this.granthLink = granthLink;
        this.tags = tags;
    }

    public String getTitleHindi() {
        return titleHindi;
    }

    public String getTitleHinglish() {
        return titleHinglish;
    }

    public String getWriter() {
        return writer;
    }

    public String getAnuyog() {
        return anuyog;
    }

    public String getLanguage() {
        return language;
    }

    public String getDescription() {
        return description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getGranthLink() {
        return granthLink;
    }

    public String getTags() {
        return tags;
    }
}
