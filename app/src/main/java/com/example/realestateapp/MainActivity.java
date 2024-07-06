package com.example.realestateapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    AutoCompleteTextView autoCompleteTextView;
    ArrayAdapter<String> adapterItems;

    String[] newDataAbv;
    String stateName;
    String stateNameAbv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get the list of states
        String[] states;
        try {
            states = getStatesList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        autoCompleteTextView = findViewById(R.id.state_drop_down_menu);
        adapterItems = new ArrayAdapter<>(this, R.layout.dropdown_item, states);

        autoCompleteTextView.setAdapter(adapterItems);

        autoCompleteTextView.setOnItemClickListener((parent, view, position, id) -> {
            String item = parent.getItemAtPosition(position).toString();
            Toast.makeText(this, item, Toast.LENGTH_LONG).show();
            stateName = item;
            stateNameAbv = newDataAbv[position];
            enableTransitionToNewActivity();
        });

        // FUTURE UPDATE
        // Only make button clickable once the user has selected a state
        // If a state hasn't been selected the button should be a lighter shade (grey tone) & not clickable
    }

    void enableTransitionToNewActivity(){
        // Get submit button and handling button events
        Button submitBtn = findViewById(R.id.stateButton);

        Intent intent = new Intent(MainActivity.this, ActivityChooseCounty.class);
        String selectedState = stateName;
        intent.putExtra("state", selectedState);
        intent.putExtra("stateAbv", stateNameAbv);
        submitBtn.setOnClickListener(v -> startActivity(intent));
    }

    public String[] getStatesList() throws IOException {
        // open CSV
        List<String> data = new ArrayList<>();
        List<String> dataAbv = new ArrayList<>();

        InputStream is = getResources().openRawResource(R.raw.statecountynames);
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(is, StandardCharsets.UTF_8)
        );

        String line;

        for(int i = 0; i < 52; i++){
            try {
                line = reader.readLine();
                String[] tokens = line.split(",");

                if(i != 0 && i != 2 && i != 9 && i != 12) {
                    data.add(tokens[3]);
                    dataAbv.add(tokens[4]);
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }

        String[] newData = new String[data.size()];
        data.toArray(newData);
        newDataAbv = new String[dataAbv.size()];
        dataAbv.toArray(newDataAbv);

        return newData;
    }

}