package com.example.rideshare;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * RecyclerView Adapter for displaying trip stops one after the other
 *
 * @author Sukriti
 * @version 1.0
 */
public class TripStopListItemAdapter extends RecyclerView.Adapter<TripStopListItemAdapter.TripStopListItemViewHolder> {
    private List<TripStop> stopList;
    private static final int PADDING = 30;
    private static final int TEXT_VIEW_MAX_WIDTH = 550;

    public TripStopListItemAdapter(List<TripStop> stopList) {
        this.stopList = stopList;
    }

    @NonNull
    @Override
    public TripStopListItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trip_stop_list_item, parent, false);
        return new TripStopListItemViewHolder(view);
    }

    /**
     * Binds stop data to the view.
     * Binds different icon next to the stop name
     * depending on whether it is the trip's origin or destination or a stop in the middle.
     */
    @Override
    public void onBindViewHolder(@NonNull TripStopListItemViewHolder holder, int position) {
        TripStop stop = stopList.get(position);
        if (position == stopList.size() - 1) {
            holder.durationTextView.setText(RoundDouble.round(stop.getTimeSecondsFromPreviousStop() / 60.0, 2) + " mins");
            holder.distanceTextView.setText(RoundDouble.round(stop.getDistanceMetersFromPreviousStop() / 1000.0, 2) + " km, ");
            holder.stopImageView.setImageResource(R.drawable.destination_icon);
            holder.stopImageView.setPadding(PADDING, 0, 0, PADDING);
            holder.stopNameTextView.setMaxWidth(TEXT_VIEW_MAX_WIDTH);
        } else if (position == 0) {
            holder.durationTextView.setText("");
            holder.distanceTextView.setText("");
            holder.stopImageView.setImageResource(R.drawable.origin_icon);
            holder.stopImageView.setPadding(PADDING, PADDING, 0, 0);
            holder.stopNameTextView.setMaxWidth(Integer.MAX_VALUE);
        } else {
            holder.durationTextView.setText(RoundDouble.round(stop.getTimeSecondsFromPreviousStop() / 60.0, 2) + " mins");
            holder.distanceTextView.setText(RoundDouble.round(stop.getDistanceMetersFromPreviousStop() / 1000.0, 2) + " km, ");
            holder.stopImageView.setImageResource(R.drawable.stop_icon);
            holder.stopImageView.setPadding(PADDING, 0, 0, 0);
            holder.stopNameTextView.setMaxWidth(TEXT_VIEW_MAX_WIDTH);
        }
        holder.stopNameTextView.setText(stop.getName());
    }

    @Override
    public int getItemCount() {
        return stopList.size();
    }

    /**
     * Holds the view
     */
    class TripStopListItemViewHolder extends RecyclerView.ViewHolder {

        TextView distanceTextView, durationTextView, stopNameTextView;
        ImageView stopImageView;

        public TripStopListItemViewHolder(@NonNull View itemView) {
            super(itemView);
            distanceTextView = itemView.findViewById(R.id.distanceTextView);
            durationTextView = itemView.findViewById(R.id.durationTextView);
            stopNameTextView = itemView.findViewById(R.id.stopNameTextView);
            stopImageView = itemView.findViewById(R.id.stopImageView);
        }
    }
}

