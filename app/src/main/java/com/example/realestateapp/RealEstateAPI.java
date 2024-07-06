package com.example.realestateapp;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RealEstateAPI{
    /**
     * Constructs a new RealEstateAPI
     * stateName The state selected by the user
     * countyName The county selected by the user
     */

    String response ="";
    String responseCount ="";

    String dataObject;

    Handler handler;

    public RealEstateAPI(String stateName, String countyName, String filterObject) throws IOException, InterruptedException {

        dataObject = filterObject;
        dataObject = dataObject + "\"state\":\"" + stateName + "\"," + "\"county\":\"" + countyName + "\"";

        Thread thread = new Thread(() -> {

            /*try {
                responseCount = getSearchCount(dataObject);
            } catch (Exception e) {
                e.printStackTrace();
            }*/
            try {
                response = propertySearch(dataObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        thread.start();
        thread.join();
    }

    String getResponseString(){
        return response;
    }

    String getResponseCountString(){
        return responseCount;
    }

    private String getSearchCount(String dataObject) throws IOException{
        String newFilterObject = dataObject + ",\"count\":true}";
        newFilterObject = newFilterObject.replaceAll("\\s+","");

        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(newFilterObject, mediaType);
        Request request = new Request.Builder()
                .url("https://api.realestateapi.com/v2/PropertySearch")
                .post(body)
                .addHeader("accept", "application/json")
                .addHeader("x-api-key", "xxxxxxxxxxxx")
                .addHeader("x-user-id", "UniqueUserIdentifier")
                .addHeader("content-type", "application/json")
                .build();

        Response response = client.newCall(request).execute();

        assert response.body() != null;
        String temp_resp = response.body().string();
        // Log.v("Print API Response Count", temp_resp);
        return temp_resp;
    }

    private String propertySearch(String dataObject) throws IOException {
        dataObject = dataObject + "}";
        dataObject = dataObject.replaceAll("\\s+","");

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(dataObject, mediaType);
        Request request = new Request.Builder()
                .url("https://api.realestateapi.com/v2/PropertySearch")
                .post(body)
                .addHeader("accept", "application/json")
                .addHeader("x-api-key", "CONNIEBUTLER-cf81-79cc-86a0-5b778a9fca9f")
                .addHeader("x-user-id", "UniqueUserIdentifier")
                .addHeader("content-type", "application/json")
                .build();

        Response response = client.newCall(request).execute();

        assert response.body() != null;
        String temp_resp = response.body().string();
        // Log.v("Print API Response", temp_resp);

        return temp_resp;
    }
}
