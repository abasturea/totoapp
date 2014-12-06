package com.alex.totoapp.totoitems;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Kylarme on 13.11.2014.
 */
public class RssItem implements Parcelable {

    private String headline;
    private String link;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(headline);
        dest.writeString(link);
    }

    public RssItem(String headline, String link) {
        this.headline = headline;
        this.link = link;
    }

    private RssItem(Parcel in) {
        this.headline = in.readString();
        this.link = in.readString();
    }

    public static final Creator<RssItem> CREATOR = new Creator<RssItem>() {
        @Override
        public RssItem createFromParcel(Parcel source) {
            return new RssItem(source);
        }

        @Override
        public RssItem[] newArray(int size) {
            return new RssItem[size];
        }
    };

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @Override
    public String toString() {
        return headline;
    }
}
