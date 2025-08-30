package com.example.jainshruth;

public class ArticleModel {
    private String title;
    private String writer;
    private String tags;
    private String articleLink;

    public ArticleModel(String title, String writer, String tags, String articleLink) {
        this.title = title;
        this.writer = writer;
        this.tags = tags;
        this.articleLink = articleLink;
    }

    public String getTitle() {
        return title;
    }

    public String getWriter() {
        return writer;
    }

    public String getTags() {
        return tags;
    }

    public String getArticleLink() {
        return articleLink;
    }
}
