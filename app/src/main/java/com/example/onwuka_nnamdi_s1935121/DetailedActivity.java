package com.example.onwuka_nnamdi_s1935121;

/*
 Name: Onwuka Nnamdi
 Student ID: s1935121
*/

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class DetailedActivity extends AppCompatActivity {

    TextView title;
    TextView description;
    TextView date;
    Earthquake earthquake = new Earthquake();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailed_earthquake_item);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        title = findViewById(R.id.title);
        description = findViewById(R.id.description);
        date = findViewById(R.id.date);
        // Remaking the earthquake object from the values that were just passed from the other page
        Intent intent = getIntent();
        if(intent != null) {
            String titleString ="" , desc ="", datentry ="";
            titleString = intent.getStringExtra("title");
            desc = intent.getStringExtra("description");
            datentry = intent.getStringExtra("date");
            earthquake.setDate(datentry);
            earthquake.setTitle(titleString);
            earthquake.setDescription(desc);

            title.setText(earthquake.getTitle());
            date.setText(datentry);
            description.setText(desc);

            // Make the name on the toolbar, the same name as item selected
            getSupportActionBar().setTitle(titleString);
        }

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // What makes the back button go back to the previous page
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}