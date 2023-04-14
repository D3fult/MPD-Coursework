package com.example.onwuka_nnamdi_s1935121;

/*
 Name: Onwuka Nnamdi
 Student ID: s1935121
*/

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

class EarthquakeAdapter extends RecyclerView.Adapter<EarthquakeAdapter.EarthquakeViewHolder> {

    private final List<Earthquake> earthquakeList;
    Context context;

    public EarthquakeAdapter(List<Earthquake> earthquakeList, Context context) {
        this.earthquakeList = earthquakeList;
        this.context = context;
    }

    @NonNull
    @Override
    public EarthquakeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.earthquake_item, parent, false);
        return new EarthquakeViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull EarthquakeViewHolder holder, int position) {
        Earthquake earthquake = earthquakeList.get(position);

        earthquake.setMagnitude();

        earthquake.setDepth();

        // Set earthquake title
        holder.title.setText(earthquake.getTitle());

        // Set earthquake magnitude
        holder.magnitude.setText(String.format("%.1f", earthquake.getMagnitude()));

        // Set earthquake magnitude
        holder.depth.setText(earthquake.getDepth());

        // Set card color based on earthquake magnitude
        holder.card.setBackgroundColor(getColorForMagnitude(earthquake.getMagnitude()));

        holder.card.setOnClickListener(view -> {
            Intent intent = new Intent(context, DetailedActivity.class);
            intent.putExtra("title", earthquake.getTitle());
            intent.putExtra("date", earthquake.getDate());
            intent.putExtra("description", earthquake.getDescription());
            // If you want to pass more info about the item on to the next page, do it here as shown above
            context.startActivity(intent);
        });
    }

    private int getColorForMagnitude(double magnitude) {
        int color;
        if (magnitude >= 7.0) {
            color = ContextCompat.getColor(context, R.color.colorMagnitudeHigh);
        } else if (magnitude >= 5.0) {
            color = ContextCompat.getColor(context, R.color.colorMagnitudeMedium);
        } else {
            color = ContextCompat.getColor(context, R.color.colorMagnitudeLow);
        }
        return color;
    }

    @Override
    public int getItemCount() {
        return earthquakeList.size();
    }

    public static class EarthquakeViewHolder extends RecyclerView.ViewHolder {

        TextView title;

        LinearLayout card;

        TextView magnitude;

        TextView depth;

        public EarthquakeViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.titleTextView);
            magnitude = itemView.findViewById(R.id.magnitude);
            depth = itemView.findViewById(R.id.deepestEarthquake);
            card = itemView.findViewById(R.id.cardItself);
        }
    }
}