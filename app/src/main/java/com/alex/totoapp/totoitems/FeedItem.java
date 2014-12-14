package com.alex.totoapp.totoitems;

public class FeedItem {

    private String title;
    private String url;

    public FeedItem( String title, String url ) {
        this.title = title;
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
