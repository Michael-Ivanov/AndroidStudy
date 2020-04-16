package com.android.tvchannelsadapter;

import android.os.Parcel;
import android.os.Parcelable;
import android.widget.RatingBar;

import java.io.Serializable;

public class Channel implements Parcelable {
    private String name;
    private String url;
    private float rating;

    public Channel(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public Channel(String name, String url, float rating) {
        this.name = name;
        this.url = url;
        this.rating = rating;
    }

    protected Channel(Parcel in) {
        name = in.readString();
        url = in.readString();
        rating = in.readFloat();
    }

    public static final Creator<Channel> CREATOR = new Creator<Channel>() {
        @Override
        public Channel createFromParcel(Parcel in) {
            return new Channel(in);
        }

        @Override
        public Channel[] newArray(int size) {
            return new Channel[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(url);
        dest.writeFloat(rating);
    }
}
