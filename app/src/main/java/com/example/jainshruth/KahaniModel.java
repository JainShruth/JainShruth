package com.example.jainshruth;

import java.io.Serializable;

public class KahaniModel implements Serializable {
    private String titleHindi;
    private String writerHindi;
    private String tags;
    private String link;

    public KahaniModel(String titleHindi, String writerHindi, String tags, String link) {
        this.titleHindi = titleHindi;
        this.writerHindi = writerHindi;
        this.tags = tags;
        this.link = link;
    }

    public String getTitleHindi() {
        return titleHindi;
    }

    public String getWriterHindi() {
        return writerHindi;
    }

    public String getTags() {
        return tags;
    }

    public String getLink() {
        return link;
    }
}
