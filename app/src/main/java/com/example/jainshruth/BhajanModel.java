package com.example.jainshruth;

public class BhajanModel {
    private String hindiTitle;
    private String hinglishTitle;
    private String hindiWriter;
    private String hinglishWriter;
    private String type;
    private String imageUrl;
    private String description;

    public BhajanModel(String hindiTitle, String hinglishTitle, String hindiWriter, String hinglishWriter,
                       String type, String imageUrl, String description) {
        this.hindiTitle = hindiTitle;
        this.hinglishTitle = hinglishTitle;
        this.hindiWriter = hindiWriter;
        this.hinglishWriter = hinglishWriter;
        this.type = type;
        this.imageUrl = imageUrl;
        this.description = description;
    }

    public String getHindiTitle() {
        return hindiTitle;
    }

    public String getHinglishTitle() {
        return hinglishTitle;
    }

    public String getHindiWriter() {
        return hindiWriter;
    }

    public String getHinglishWriter() {
        return hinglishWriter;
    }

    public String getType() {
        return type;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getDescription() {
        return description;
    }
}
