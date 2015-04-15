package com.github.kylarme.totoapp.totoitems;

import java.io.Serializable;

public class FeedItem implements Serializable {

    private static final long serialVersionUID = -7406082437623008161L;

    private long mId;
    private String mTitle;
    private String mLink;

    public FeedItem() {
    }

    public FeedItem(long id, String title, String link) {
        mId = id;
        mTitle = title;
        mLink = link;
    }

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        mId = id;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getLink() {
        return mLink;
    }

    public void setLink(String link) {
        mLink = link;
    }

    @Override
    public String toString() {
        return mTitle;
    }
}
