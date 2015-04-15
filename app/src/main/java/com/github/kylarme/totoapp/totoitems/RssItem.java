package com.github.kylarme.totoapp.totoitems;

import java.io.Serializable;

public class RssItem implements Serializable {

    private static final long serialVersionUID = -7406082437623008161L;

    private long mId;
    private String mHeadline;
    private String mLink;
    private String mImageLink;
    private String mDescription;
    private long mFeedId;

    public RssItem() {

    }

    public RssItem(long id, String headline, String link, String imageLink, String description, int feedId) {
        mId = id;
        mHeadline = headline;
        mLink = link;
        mImageLink = imageLink;
        mDescription = description;
        mFeedId = feedId;
    }

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        mId = id;
    }

    public String getHeadline() {
        return mHeadline;
    }

    public void setHeadline(String headline) {
        mHeadline = headline;
    }

    public String getLink() {
        return mLink;
    }

    public void setLink(String link) {
        mLink = link;
    }

    public String getImageLink() {
        return mImageLink;
    }

    public void setImageLink(String imageLink) {
        mImageLink = imageLink;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public long getFeedId() {
        return mFeedId;
    }

    public void setFeedId(long feedId) {
        mFeedId = feedId;
    }

    @Override
    public String toString() {
        return mHeadline;
    }
}
