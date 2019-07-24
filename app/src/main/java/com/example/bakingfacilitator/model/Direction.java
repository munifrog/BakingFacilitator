package com.example.bakingfacilitator.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.net.MalformedURLException;
import java.net.URL;

public class Direction implements Parcelable {
    private long mOrder;
    private String mDescribeFull;
    private String mDescribeShort;
    private URL mUrlVideo;
    private URL mUrlThumb;

    public Direction(
            long order,
            String describeFull,
            String describeShort,
            String urlVideo,
            String urlThumb
    ) {
        mOrder = order;
        mDescribeFull = describeFull;
        mDescribeShort = describeShort;
        mUrlVideo = string2url(urlVideo);
        mUrlThumb = string2url(urlThumb);
    }

    private URL string2url(String input) {
        URL output = null;
        if (input != null && !input.isEmpty()) {
            try {
                output = new URL(input);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        return output;
    }

    private String url2string(URL input) {
        return (input == null) ? "" : input.toString();
    }

    public void setOrder(long mOrder) { this.mOrder = mOrder; }
    public long getOrder() { return mOrder; }

    public void setDescribeFull(String mDescribeFull) { this.mDescribeFull = mDescribeFull; }
    public String getDescribeFull() { return mDescribeFull; }

    public void setDescribeShort(String mDescribeShort) { this.mDescribeShort = mDescribeShort; }
    public String getDescribeShort() { return mDescribeShort; }

    public void setUrlThumb(URL mUrlThumb) { this.mUrlThumb = mUrlThumb; }
    public URL getUrlThumb() { return mUrlThumb; }

    public void setUrlVideo(URL mUrlVideo) { this.mUrlVideo = mUrlVideo; }
    public URL getUrlVideo() { return mUrlVideo; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(getOrder());
        dest.writeString(getDescribeFull());
        dest.writeString(getDescribeShort());
        dest.writeString(url2string(getUrlVideo()));
        dest.writeString(url2string(getUrlThumb()));
    }

    private Direction (Parcel parcel) {
        setOrder(parcel.readLong());
        setDescribeFull(parcel.readString());
        setDescribeShort(parcel.readString());
        setUrlVideo(string2url(parcel.readString()));
        setUrlThumb(string2url(parcel.readString()));
    }

    public static final Parcelable.Creator<Direction> CREATOR = new Parcelable.Creator<Direction>() {
        @Override
        public Direction createFromParcel(Parcel source) {
            return new Direction(source);
        }

        @Override
        public Direction[] newArray(int size) {
            return new Direction[0];
        }
    };
}
