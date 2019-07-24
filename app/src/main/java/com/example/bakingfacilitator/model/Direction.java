package com.example.bakingfacilitator.model;

import java.net.MalformedURLException;
import java.net.URL;

public class Direction {
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
        mUrlVideo = null;
        if (!urlVideo.isEmpty()) {
            try {
                mUrlVideo = new URL(urlVideo);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        mUrlThumb = null;
        if (!urlThumb.isEmpty()) {
            try {
                mUrlThumb = new URL(urlThumb);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
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
}
