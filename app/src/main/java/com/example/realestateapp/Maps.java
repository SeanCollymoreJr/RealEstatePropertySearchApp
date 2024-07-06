package com.example.realestateapp;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import okhttp3.Response;

public class Maps extends AppCompatActivity {

    private GoogleMap gMap;

    Handler handler;

    String testResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        String selectedState ="";
        String selectedStateAbv ="";
        String selectedCounty ="";
        String filterData = "{\"ids_only\":false,\"obfuscate\":false,\"summary\":false,\"size\":10,";

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            selectedState = extras.getString("state");
            selectedStateAbv = extras.getString("stateAbv");
            selectedCounty = extras.getString("county");
        }

        try {
            testResponse = testGetSampleResponseString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Get the Real Estate API class and create an object of that class
        RealEstateAPI realEstateAPI;
        try {
            realEstateAPI = new RealEstateAPI(selectedStateAbv, selectedCounty, filterData);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        handler = new Handler();
        String finalSelectedState = selectedState;
        String finalSelectedStateAbv = selectedStateAbv;
        String finalSelectedCounty = selectedCounty;
        String response = realEstateAPI.getResponseString();
        String responseCount = realEstateAPI.getResponseCountString();
        handler.postDelayed(() -> transitionToMapFragment(finalSelectedState, finalSelectedStateAbv, finalSelectedCounty, response, responseCount), 5);
    }


    void transitionToMapFragment(String selectedState,String selectedStateAbv, String selectedCounty, String response, String responseCount){
        Intent intent = new Intent(Maps.this, ViewGMaps.class);
        intent.putExtra("state", selectedState);
        intent.putExtra("stateAbv", selectedStateAbv);
        intent.putExtra("county", selectedCounty);
        intent.putExtra("response", response);
        intent.putExtra("responseCount", responseCount);
        startActivity(intent);
    }

    private String testGetSampleResponseString() throws IOException {
        InputStream is = getResources().openRawResource(R.raw.reapi_response_string);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
        String json = "";
        try {
            StringBuilder sb = new StringBuilder();
            String line = reader.readLine();

            while (line != null) {
                sb.append(line);
                sb.append("\n");
                line = reader.readLine();
            }
            json = sb.toString();
        } finally {
            reader.close();
        }
        return json;
    }
}