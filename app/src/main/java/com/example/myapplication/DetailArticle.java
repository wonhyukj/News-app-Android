package com.example.myapplication;

import android.text.Spanned;

public class DetailArticle {
    private String articleImg;
    private String articleTitle;

    private String articleSource;
    private String articleDate;
    private Spanned articleContent;
    private String articleURL;

    public DetailArticle() {

    }

    public DetailArticle(String articleImg, String articleTitle, String articleSource, String articleDate, Spanned articleContent, String articleURL, String bookmark) {
        this.articleImg = articleImg;
        this.articleTitle = articleTitle;
        this.articleSource = articleSource;
        this.articleDate = articleDate;
        this.articleContent = articleContent;
        this.articleURL = articleURL;
    }

    public String getArticleImg() {
        return articleImg;
    }

    public void setArticleImg(String articleImg) {
        this.articleImg = articleImg;
    }

    public String getArticleTitle() {
        return articleTitle;
    }

    public void setArticleTitle(String articleTitle) {
        this.articleTitle = articleTitle;
    }

    public String getArticleSource() {
        return articleSource;
    }

    public void setArticleSource(String articleSource) {
        this.articleSource = articleSource;
    }

    public String getArticleDate() {
        return articleDate;
    }

    public void setArticleDate(String articleDate) {
        this.articleDate = articleDate;
    }

    public Spanned getArticleContent() {
        return articleContent;
    }

    public void setArticleContent(Spanned articleContent) {
        this.articleContent = articleContent;
    }

    public String getArticleURL() {
        return articleURL;
    }

    public void setArticleURL(String articleURL) {
        this.articleURL = articleURL;
    }
}