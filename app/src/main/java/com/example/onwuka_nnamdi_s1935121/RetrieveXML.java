package com.example.onwuka_nnamdi_s1935121;

/*
 Name: Onwuka Nnamdi
 Student ID: s1935121
*/

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class RetrieveXML {
    public interface CompletionResponse {
        void callback(String response);
    }

    public static void fetchData(CompletionResponse completion) {
        new Thread(() -> {
            HttpURLConnection urlConnection = null;

            try {
                URL url = new URL("https://quakes.bgs.ac.uk/feeds/WorldSeismology.xml");
                urlConnection = (HttpURLConnection) url.openConnection();

                BufferedReader rd = new BufferedReader(new InputStreamReader(
                        urlConnection.getInputStream()));
                String line;

                final StringBuilder sb = new StringBuilder();
                while ((line = rd.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                completion.callback(sb.toString());
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) urlConnection.disconnect();
            }
        }).start();
    }
}