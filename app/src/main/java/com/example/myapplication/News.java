package com.example.myapplication;

public class News {
    private String id;
    private String img;

    private String title;
    private String time;
    private String section;
    private String webURL;
    private String bookmark;
    private String newsImgURL;
    public  News(){

    }


    public News(String id, String img, String title , String time, String section, String webURL, String bookmark, String newsImgURL){
        this.id = id;
        this.img = img;
        this.title = title;
        this.time = time;
        this.section = section;
        this.webURL = webURL;
        this.bookmark = bookmark;
        this.newsImgURL = newsImgURL;
    }

    public String getNewsImgURL() {
        return newsImgURL;
    }

    public void setNewsImgURL(String newsImgURL) {
        this.newsImgURL = newsImgURL;
    }

    public String getBookmark() {
        return bookmark;
    }

    public void setBookmark(String bookmark) {
        this.bookmark = bookmark;
    }
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getWebURL() {
        return webURL;
    }

    public void setWebURL(String webURL) {
        this.webURL = webURL;
    }

}

