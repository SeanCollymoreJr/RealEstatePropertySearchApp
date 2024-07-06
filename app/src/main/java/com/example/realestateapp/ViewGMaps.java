package com.example.realestateapp;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.AdvancedMarkerOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class ViewGMaps extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap gMap;

    String selectedState ="";
    String selectedStateAbv ="";
    String selectedCounty ="";

    String response ="";

    String responseCount ="";

    JSONArray jsonArray;

    // Basic property Information
    List<String> address = new ArrayList<>();
    List<String> city = new ArrayList<>();
    List<String> county = new ArrayList<>();
    List<String> state = new ArrayList<>();
    List<String> street = new ArrayList<>();
    List<String> zip = new ArrayList<>();

    // In-Depth Property Information

    List<Double> longitude = new ArrayList<>();
    List<Double> latitude = new ArrayList<>();
    List<String> bedrooms = new ArrayList<>();
    List<String> bathrooms = new ArrayList<>();
    List<String> squareFeet = new ArrayList<>();
    List<String> assessedValue = new ArrayList<>();
    List<String> assessedLandValue = new ArrayList<>();
    List<String> estimatedValue = new ArrayList<>();
    List<String> estimatedEquity = new ArrayList<>();
    List<String> equityPercent = new ArrayList<>();
    List<String> companyName = new ArrayList<>();
    List<String> lenderName = new ArrayList<>();
    List<String> openMortgageBalance = new ArrayList<>();
    List<String> lastSaleDate = new ArrayList<>();
    List<String> lastSaleAmount = new ArrayList<>();
    List<String> priorSaleAmount = new ArrayList<>();
    List<Boolean> auction = new ArrayList<>();
    List<String> auctionDate = new ArrayList<>();
    List<Boolean> foreclosure = new ArrayList<>();
    List<Boolean> preForeclosure = new ArrayList<>();
    List<Boolean> investorBuyer = new ArrayList<>();
    List<String> loanTypeCode = new ArrayList<>();
    List<Boolean> floodZone = new ArrayList<>();
    List<String> floodZoneDescription = new ArrayList<>();
    List<String> floodZoneType = new ArrayList<>();

    SlidingUpPanelLayout slidingUpPanelLayout;
    ListView propertyInformation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_gmaps);

        ImageButton exitButton = findViewById(R.id.exitButton);
        exitButton.setZ(30);

        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewGMaps.this, MainActivity.class);
                startActivity(intent);
            }
        });

        slidingUpPanelLayout = findViewById(R.id.sliding_layout);
        propertyInformation = findViewById(R.id.propertyInformation);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            selectedState = extras.getString("state");
            selectedStateAbv = extras.getString("stateAbv");
            selectedCounty = extras.getString("county");
            response = extras.getString("response");
            responseCount = extras.getString("responseCount");
        }

        try {
            parseJSON(response, false);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.id_map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        int numOfCoordinates = 8;

        double latAvg = 0;
        double longAvg = 0;

        for(int i = 0; i < numOfCoordinates; i++){
            String locationTitle = address.get(i);
            LatLng location = new LatLng(latitude.get(i), longitude.get(i));
            latAvg += latitude.get(i);
            longAvg += longitude.get(i);
            googleMap.addMarker(new AdvancedMarkerOptions().position(location).title(locationTitle).snippet(String.valueOf(i)));
        }

        googleMap.getUiSettings().setZoomControlsEnabled(true);

        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                String mIndex = marker.getSnippet();
                assert mIndex != null;
                int markerIndex = Integer.parseInt(mIndex);
                setListViewInformation(markerIndex);
                LatLng newLocation = marker.getPosition();
                double log = newLocation.longitude;
                double lat = newLocation.latitude  - 0.0025;
                newLocation = new LatLng(lat, log);
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newLocation, 16));
                slidingUpPanelLayout.setZ(20);

                return true;
            }
        });

        ImageButton closePanelButton = findViewById(R.id.closePanelButton);

        closePanelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slidingUpPanelLayout.setZ(-20);
            }
        });

        latAvg /= numOfCoordinates;
        longAvg /= numOfCoordinates;

        LatLng finalLocation = new LatLng(latAvg, longAvg);

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(finalLocation,9));
    }

    public void setListViewInformation(int index){

        List<String> data = new ArrayList<>();
        data.add("Street: " + street.get(index));
        data.add("City: " + city.get(index));
        data.add("County: " + county.get(index));
        data.add("State: " + state.get(index));
        data.add("Zip: " + zip.get(index));

        data.add("bedrooms: " + bedrooms.get(index));
        data.add("bathrooms: " + bathrooms.get(index));
        data.add("squareFeet: " + squareFeet.get(index) + " ft");
        data.add("assessedValue: $" + assessedValue.get(index));
        data.add("assessedLandValue: $" + assessedLandValue.get(index));
        data.add("estimatedValue: $" + estimatedValue.get(index));
        data.add("estimatedEquity: $" + estimatedEquity.get(index));
        data.add("equityPercent: " + equityPercent.get(index) + "%");
        // data.add("companyName: " + companyName.get(index));
        /*data.add("lenderName: " + lenderName.get(index));
        data.add("openMortgageBalance: " + openMortgageBalance.get(index));
        data.add("lastSaleDate: " + lastSaleDate.get(index));
        data.add("lastSaleAmount: " + lastSaleAmount.get(index));
        data.add("priorSaleAmount: " + priorSaleAmount.get(index));
        if(auction.get(index)){
            data.add("Currently In Auction");
            data.add("auctionDate: " + auctionDate.get(index));
        }
        if(foreclosure.get(index)){
            data.add("Currently In Foreclosure");
        }
        if(preForeclosure.get(index)){
            data.add("Currently In Pre-Foreclosure");
        }
        if(investorBuyer.get(index)){
            data.add("Owned By Investor Buyer");
        }
        // data.add("loanTypeCode: " + loanTypeCode.get(index));
        if(floodZone.get(index)){
            data.add("In A Flood Zone");
            data.add("floodZoneDescription: " + floodZoneDescription.get(index));
            data.add("floodZoneType: " + floodZoneType.get(index));
        }*/

        String[] newData = new String[data.size()];
        data.toArray(newData);
        propertyInformation.setAdapter(new ArrayAdapter<>(ViewGMaps.this, R.layout.dropdown_item, newData));
    }

    public void parseJSON(String jsonString, boolean count) throws JSONException {
        if(!count) {
            // First Nest
            JSONObject jsonObject = new JSONObject(jsonString);
            jsonArray = jsonObject.getJSONArray("data");

            for (int i = 0; i < jsonArray.length(); i++) {
                // Second Nest
                JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                longitude.add(Double.parseDouble(jsonObject2.getString("longitude")));
                latitude.add(Double.parseDouble(jsonObject2.getString("latitude")));

                bedrooms.add(jsonObject2.getString("bedrooms"));
                bathrooms.add(jsonObject2.getString("bathrooms"));
                squareFeet.add(jsonObject2.getString("squareFeet"));
                assessedValue.add(jsonObject2.getString("assessedValue"));
                assessedLandValue.add(jsonObject2.getString("assessedLandValue"));
                estimatedValue.add(jsonObject2.getString("estimatedValue"));
                estimatedEquity.add(jsonObject2.getString("estimatedEquity"));
                equityPercent.add(jsonObject2.getString("equityPercent"));
                /*companyName.add(jsonObject2.getString("companyName"));
                lenderName.add(jsonObject2.getString("lenderName"));
                openMortgageBalance.add(jsonObject2.getString("openMortgageBalance"));
                lastSaleDate.add(jsonObject2.getString("lastSaleDate"));
                lastSaleAmount.add(jsonObject2.getString("lastSaleAmount"));
                priorSaleAmount.add(jsonObject2.getString("priorSaleAmount"));
                auction.add(Boolean.parseBoolean(jsonObject2.getString("auction")));
                auctionDate.add(jsonObject2.getString("auctionDate"));
                foreclosure.add(Boolean.parseBoolean(jsonObject2.getString("foreclosure")));
                preForeclosure.add(Boolean.parseBoolean(jsonObject2.getString("preForeclosure")));
                investorBuyer.add(Boolean.parseBoolean(jsonObject2.getString("investorBuyer")));
                loanTypeCode.add(jsonObject2.getString("loanTypeCode"));
                floodZone.add(Boolean.parseBoolean(jsonObject2.getString("floodZone")));
                floodZoneDescription.add(jsonObject2.getString("floodZoneDescription"));
                floodZoneType.add(jsonObject2.getString("floodZoneType"));*/

                // Third Nest
                JSONObject jsonObject3 = new JSONObject(jsonObject2.getString("address"));
                address.add(jsonObject3.getString("address"));
                city.add(jsonObject3.getString("city"));
                county.add(jsonObject3.getString("county"));
                state.add(jsonObject3.getString("state"));
                street.add(jsonObject3.getString("street"));
                zip.add(jsonObject3.getString("zip"));

            }
        }
    }


}