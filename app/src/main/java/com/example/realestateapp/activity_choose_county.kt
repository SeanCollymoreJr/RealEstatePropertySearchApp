package com.example.realestateapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets

class ActivityChooseCounty : AppCompatActivity() {

    var countyName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_county)

        val selectedState : String? = intent.extras?.getString("state")
        val selectedStateAbv : String? = intent.extras?.getString("stateAbv")

        val counties : MutableList<String> = getCounties(selectedState)

        val countyPosition = IntArray(1)

        var autoCompleteTextView: AutoCompleteTextView? = null
        var adapterItems: ArrayAdapter<String?>? = null

        autoCompleteTextView = findViewById(R.id.county_drop_down_menu)
        adapterItems = ArrayAdapter<String?>(this, R.layout.dropdown_item2,
            counties as List<String?>
        )

        autoCompleteTextView.setAdapter(adapterItems)

        autoCompleteTextView.onItemClickListener =
            OnItemClickListener { parent: AdapterView<*>, view: View?, position: Int, id: Long ->
                val item = parent.getItemAtPosition(position).toString()
                Toast.makeText(this, item, Toast.LENGTH_LONG).show()
                countyName = item
                enableTransitionToNewActivity(selectedState, selectedStateAbv)
            }


        val backButton = findViewById<TextView>(R.id.backButton)

        backButton.setOnClickListener {
            // Log.v("Test log", "Button Successfully Clicked")
            finish()
        }
    }

    private fun enableTransitionToNewActivity(selectedState : String?, selectedStateAbv : String?) {
        // Get submit button and handling button events
        val submitBtn = findViewById<Button>(R.id.countyButton)

        // FUTURE UPDATE
        // Only make button clickable once the user has selected a county
        // If a county hasn't been selected the button should be a lighter shade (grey tone) & not clickable
        val intent = Intent(
            this@ActivityChooseCounty,
            Maps::class.java
        )
        intent.putExtra("county", countyName)
        intent.putExtra("state", selectedState)
        intent.putExtra("stateAbv", selectedStateAbv)
        submitBtn.setOnClickListener { v: View? ->
            startActivity(
                intent
            )
        }
    }

    private fun getCounties(selectedState : String?) : MutableList<String> {
        // open CSV
        val data: MutableList<String> = ArrayList()

        val `is` = resources.openRawResource(R.raw.statecountynames)
        val reader = BufferedReader(
            InputStreamReader(`is`, StandardCharsets.UTF_8)
        )

        var line: String

        val startingIndex : Int = getSelectedStatePositionStart(selectedState)

        var i = 0
        while (i < 3158) {
            try {
                line = reader.readLine()
                val tokens = line.split(",".toRegex()).dropLastWhile { it.isEmpty() }
                    .toTypedArray()
                if (tokens[2] == selectedState) {
                    data.add(tokens[1])
                }

            } catch (e: IOException) {
                throw RuntimeException(e)
            }
            i++
        }

        return data

    }

    // Function used for possible optimization
    private fun getSelectedStatePositionStart(selectedState : String?) : Int{
        var startingIndex  = 0

        if (selectedState == "ALABAMA") {
            startingIndex = 1
        }
        if (selectedState == "ALASKA") {
            startingIndex = 68
        }
        if (selectedState == "ARIZONA") {
            startingIndex = 97
        }
        if (selectedState == "ARKANSAS") {
            startingIndex = 112
        }
        if (selectedState == "CALIFORNIA") {
            startingIndex = 202
        }
        if (selectedState == "COLORADO") {
            startingIndex = 260
        }
        if (selectedState == "CONNECTICUT") {
            startingIndex = 324
        }
        if (selectedState == "DELAWARE") {
            startingIndex = 332
        }
        if (selectedState == "FLORIDA") {
            startingIndex = 336
        }
        if (selectedState == "GEORGIA") {
            startingIndex = 403
        }
        if (selectedState == "IDAHO") {
            startingIndex = 567
        }
        if (selectedState == "ILLINOIS") {
            startingIndex = 611
        }
        if (selectedState == "INDIANA") {
            startingIndex = 713
        }
        if (selectedState == "IOWA") {
            startingIndex = 805
        }
        if (selectedState == "KANSAS") {
            startingIndex = 904
        }
        if (selectedState == "KENTUCKY") {
            startingIndex = 1009
        }
        if (selectedState == "LOUISIANA") {
            startingIndex = 1129
        }
        if (selectedState == "MAINE") {
            startingIndex = 1193
        }
        if (selectedState == "MARYLAND") {
            startingIndex = 1209
        }
        if (selectedState == "MASSACHUSETTS") {
            startingIndex = 1233
        }
        if (selectedState == "MICHIGAN") {
            startingIndex = 1247
        }
        if (selectedState == "MINNESOTA") {
            startingIndex = 1330
        }
        if (selectedState == "MISSISSIPPI") {
            startingIndex = 1417
        }
        if (selectedState == "MISSOURI") {
            startingIndex = 1499
        }
        if (selectedState == "MONTANA") {
            startingIndex = 1614
        }
        if (selectedState == "NEBRASKA") {
            startingIndex = 1670
        }
        if (selectedState == "NEVADA") {
            startingIndex = 1763
        }
        if (selectedState == "NEW HAMPSHIRE") {
            startingIndex = 1780
        }
        if (selectedState == "NEW JERSEY") {
            startingIndex = 1790
        }
        if (selectedState == "NEW MEXICO") {
            startingIndex = 1811
        }
        if (selectedState == "NEW YORK") {
            startingIndex = 1844
        }
        if (selectedState == "NORTH CAROLINA") {
            startingIndex = 1906
        }
        if (selectedState == "NORTH DAKOTA") {
            startingIndex = 2006
        }
        if (selectedState == "OHIO") {
            startingIndex = 2059
        }
        if (selectedState == "OKLAHOMA") {
            startingIndex = 2147
        }
        if (selectedState == "OREGON") {
            startingIndex = 2224
        }
        if (selectedState == "PENNSYLVANIA") {
            startingIndex = 2260
        }
        if (selectedState == "RHODE ISLAND") {
            startingIndex = 2327
        }
        if (selectedState == "SOUTH CAROLINA") {
            startingIndex = 2332
        }
        if (selectedState == "SOUTH DAKOTA") {
            startingIndex = 2378
        }
        if (selectedState == "TENNESSEE") {
            startingIndex = 2444
        }
        if (selectedState == "TEXAS") {
            startingIndex = 2539
        }
        if (selectedState == "UTAH") {
            startingIndex = 2793
        }
        if (selectedState == "VERMONT") {
            startingIndex = 2822
        }
        if (selectedState == "VIRGINIA") {
            startingIndex = 2836
        }
        if (selectedState == "WASHINGTON") {
            startingIndex = 2970
        }
        if (selectedState == "WEST VIRGINIA") {
            startingIndex = 3009
        }
        if (selectedState == "WISCONSIN") {
            startingIndex = 3064
        }
        if (selectedState == "WYOMING") {
            startingIndex = 3136
        }

        return startingIndex
    }

}
