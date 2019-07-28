package com.example.bakingfacilitator.util;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class Http {
    private static final String DEFAULT_RECIPE_DOWNLOAD =
            "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";

    public static String getResponse(URL url) {
        String body = null;

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();

        try {
            Response response = client.newCall(request).execute();
            ResponseBody rBody = response.body();
            if(rBody != null) {
                body = rBody.string();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return body;
    }

    public static String getDefaultResponse() {
        try {
            URL url = new URL(DEFAULT_RECIPE_DOWNLOAD);
            return getResponse(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return "";
    }
}
