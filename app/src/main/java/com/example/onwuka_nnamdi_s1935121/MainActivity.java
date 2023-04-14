package com.example.onwuka_nnamdi_s1935121;

/*
 Name: Onwuka Nnamdi
 Student ID: s1935121
*/

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RetrieveXML.CompletionResponse {

    RecyclerView recyclerView;

    EarthquakeAdapter adapter;
    SearchView searchBox;

    Button searchButton;

    List<Earthquake> earthquakeList = new ArrayList<>();

    SearchView dateSearchBox;

    Button dateSearchButton;

    Button compareMagnitudeButton;

    Button compareDepthButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // --- Initialize the views
        recyclerView = findViewById(R.id.listOfItems);
        searchBox = findViewById(R.id.searchBox);
        searchButton = findViewById(R.id.searchButton);
        dateSearchBox = findViewById(R.id.dateSearchBox);
        dateSearchButton = findViewById(R.id.dateSearchButton);
        compareMagnitudeButton = findViewById(R.id.compareMagnitudeButton);
        compareDepthButton = findViewById(R.id.compareDepthButton);

        // Set layout manager for recycler view
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Create and set adapter for recycler view
        earthquakeList = new ArrayList<>();
        adapter = new EarthquakeAdapter(earthquakeList, this);
        recyclerView.setAdapter(adapter);

        // --- Load data from website
        RetrieveXML.fetchData(this);

        // Set click listener for search button
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Show back button in the action bar
                // Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

                String query = searchBox.getQuery().toString();

                // Check if search query is empty
                if (query.isEmpty()) {
                    // Inform the user that no search query has been entered
                    Toast.makeText(MainActivity.this, "Please enter a search term", Toast.LENGTH_SHORT).show();
                } else {
                    // Search for earthquakes with matching titles
                    List<Earthquake> searchResults = searchEarthquakes(query);

                    // If no matching earthquakes are found, inform the user
                    if (searchResults.isEmpty()) {
                        Toast.makeText(MainActivity.this, "No earthquakes occurred in " + query, Toast.LENGTH_SHORT).show();
                    } else {
                        // Display the matching earthquakes in the RecyclerView
                        adapter = new EarthquakeAdapter(searchResults, MainActivity.this);
                        recyclerView.setAdapter(adapter);
                    }
                }
            }

            private List<Earthquake> searchEarthquakes(String query) {
                List<Earthquake> results = new ArrayList<>();
                for (Earthquake earthquake : earthquakeList) {
                    if (earthquake.getTitle().toLowerCase().contains(query.toLowerCase())) {
                        results.add(earthquake);
                    }
                }
                return results;
            }
        });
        // Set click listener for date search button
        dateSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String dateQuery = dateSearchBox.getQuery().toString();

                // Check if date search query is empty
                if (dateQuery.isEmpty()) {
                    // Inform the user that no date search query has been entered
                    Toast.makeText(MainActivity.this, "Please enter a date in the format YYYY-MM-DD", Toast.LENGTH_SHORT).show();
                } else {
                    // Search for earthquakes with matching dates
                    List<Earthquake> searchResults = searchEarthquakesByDate(dateQuery);

                    // If no matching earthquakes are found, inform the user
                    if (searchResults.isEmpty()) {
                        Toast.makeText(MainActivity.this, "No earthquakes occurred on " + dateQuery, Toast.LENGTH_SHORT).show();
                    } else {
                        // Display the matching earthquakes in the RecyclerView
                        adapter = new EarthquakeAdapter(searchResults, MainActivity.this);
                        recyclerView.setAdapter(adapter);
                    }
                }
            }

            private List<Earthquake> searchEarthquakesByDate(String dateQuery) {
                List<Earthquake> results = new ArrayList<>();
                for (Earthquake earthquake : earthquakeList) {
                    if (earthquake.getDate().equals(dateQuery)) {
                        results.add(earthquake);
                    }
                }
                return results;
            }
        });

        compareMagnitudeButton.setOnClickListener(view -> {
            // Find the earthquake with the highest magnitude
            Earthquake highestMagnitudeEarthquake = null;
            double highestMagnitude = Double.MIN_VALUE;
            for (Earthquake earthquake : earthquakeList) {
                if (earthquake.getMagnitude() > highestMagnitude) {
                    highestMagnitude = earthquake.getMagnitude();
                    highestMagnitudeEarthquake = earthquake;
                }
            }

            // Create a new list containing only the highest magnitude earthquake
            List<Earthquake> highestMagnitudeEarthquakeList = new ArrayList<>();
            highestMagnitudeEarthquakeList.add(highestMagnitudeEarthquake);

            // Update the RecyclerView with the new list
            EarthquakeAdapter adapter = new EarthquakeAdapter(highestMagnitudeEarthquakeList, MainActivity.this);
            recyclerView.setAdapter(adapter);
        });

        compareDepthButton.setOnClickListener(view -> {
            // Find the earthquake with the deepest depth
            Earthquake deepestEarthquake = null;
            double deepestDepth = Double.MAX_VALUE;
            for (Earthquake earthquake : earthquakeList) {
                String depthString = earthquake.getDepth();
                if (depthString != null) {
                    depthString = depthString.replaceAll("[^\\d.]", ""); // remove non-numeric characters from the string
                    double depth = Double.parseDouble(depthString);
                    if (depth > deepestDepth) {
                        deepestDepth = depth;
                        deepestEarthquake = earthquake;
                    }
                }
            }

            // Create a new list containing only the deepest earthquake
            List<Earthquake> deepestEarthquakeList = new ArrayList<>();
            deepestEarthquakeList.add(deepestEarthquake);

            // Update the RecyclerView with the new list
            EarthquakeAdapter adapter = new EarthquakeAdapter(deepestEarthquakeList, MainActivity.this);
            recyclerView.setAdapter(adapter);
        });
    }


    @Override
    public void callback(String response) {
        // When this fires, it means we have the response(which is the XML stuff we are supposed to collect form teh website)
        // So we pass the string on to be parsed with PULL PARSER
        parseXML(response);

        if (!response.isEmpty()) {
            parseXML(response);
        }

    }

    public void parseXML(String xmlString){
        // This is where we use pull parser to extract the items we are supposed to show
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new StringReader(xmlString));

            Earthquake earthquakeItem = null;

            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tagName = parser.getName();

                if (eventType == XmlPullParser.START_TAG) {
                    if (tagName.equalsIgnoreCase("item")) {
                        earthquakeItem = new Earthquake();
                    } else if (earthquakeItem != null) {
                        switch (tagName.toLowerCase()) {
                            case "title":
                                earthquakeItem.setTitle(parser.nextText());
                                break;
                            case "description":
                                earthquakeItem.setDescription(parser.nextText());
                                break;
                            case "pubdate":
                                earthquakeItem.setDate(parser.nextText());
                                break;
                        }
                    }
                } else if (eventType == XmlPullParser.END_TAG && tagName.equalsIgnoreCase("item")) {
                    earthquakeList.add(earthquakeItem);
                }

                eventType = parser.next();
            }

            // Inflate the recycler view here with the items
            EarthquakeAdapter adapter = new EarthquakeAdapter(earthquakeList,this);
            recyclerView.setAdapter(adapter);
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }
    }
}
