package com.example.onwuka_nnamdi_s1935121;

/*
 Name: Onwuka Nnamdi
 Student ID: s1935121
*/

import androidx.annotation.NonNull;

public class Earthquake {
    private String title;
    private String description;
    private String date;

    private double magnitude;

    private String depth;

    public Earthquake(String title, String description, String date){
        this.title = title;
        this.description = description;
        this.date = date;
    }

    public Earthquake(){}
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getMagnitude() {
        return magnitude;
    }

    // Method to extract and set the magnitude of the earthquake from the description using indexing
    public void setMagnitude() {
        // Split the description using ";" delimiter
        String[] parts = description.split(";");
        for (String part : parts) {
            if (part.contains("Magnitude")) {
                // Extract the magnitude value from the string
                String magnitudeString = part.substring(part.indexOf(":") + 1).trim();
                this.magnitude = Double.parseDouble(magnitudeString);
                break;
            }
        }
    }

    public String getDepth() {
        return depth;
    }

    public void setDepth() {
        // Split the description using ";" delimiter
        String[] parts = description.split(";");
        for (String part : parts) {
            if (part.contains("Depth")) {
                // Extract the magnitude value from the string
                String depthString = part.substring(part.indexOf(":") + 1).trim();
                this.depth = depthString;
                break;
            }
        }
    }

    @NonNull
    @Override
    public String toString() {
        return "EarthquakeList{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", date='" + date + '\'' +
                ", magnitude='" + magnitude + '\'' +
                ", depth='" + depth + '\'' +
                '}';
    }
}