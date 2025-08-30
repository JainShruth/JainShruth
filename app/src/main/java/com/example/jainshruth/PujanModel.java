package com.example.jainshruth;

public class PujanModel {
    private String pujanName;
    private String writerName;
    private String htmlWithoutMeaning;
    private String htmlWithMeaning;
    private String meaningAvailable;
    private String tags;

    public PujanModel(String pujanName, String writerName, String htmlWithoutMeaning, String htmlWithMeaning, String meaningAvailable, String tags) {
        this.pujanName = pujanName;
        this.writerName = writerName;
        this.htmlWithoutMeaning = htmlWithoutMeaning;
        this.htmlWithMeaning = htmlWithMeaning;
        this.meaningAvailable = meaningAvailable;
        this.tags = tags;
    }

    public String getPujanName() { return pujanName; }
    public String getWriterName() { return writerName; }
    public String getHtmlWithoutMeaning() { return htmlWithoutMeaning; }
    public String getHtmlWithMeaning() { return htmlWithMeaning; }
    public String getMeaningAvailable() { return meaningAvailable; }
    public String getTags() { return tags; }
}