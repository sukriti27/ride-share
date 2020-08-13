package com.example.rideshare;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * RecyclerView Adapter for displaying recommended trips in a list one after the other
 *
 * @author Sukriti
 * @version 1.0
 */
public class RecommendedTripListItemAdapter extends RecyclerView.Adapter<RecommendedTripListItemAdapter.RecommendedTripListViewHolder> {

    private Context context;
    private RecyclerView.RecycledViewPool tripBookingsRecyclerViewPool = new RecyclerView.RecycledViewPool();
    private RecyclerView.RecycledViewPool tripStopsRecyclerViewPool = new RecyclerView.RecycledViewPool();
    private List<Trip> tripList;
    private Trip selectedTrip;

    public RecommendedTripListItemAdapter(Context context, List<Trip> tripList, Trip selectedTrip) {
        this.context = context;
        this.tripList = tripList;
        this.selectedTrip = selectedTrip;
    }

    @NonNull
    @Override
    public RecommendedTripListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trip_list_item, parent, false);
        return new RecommendedTripListViewHolder(view);
    }

    /**
     * Binds trip data to the view.
     */
    @Override
    public void onBindViewHolder(@NonNull RecommendedTripListViewHolder holder, int position) {
        Trip trip = tripList.get(position);

        String savings = RoundDouble.round(trip.getIncreaseInSavings() / 1000.0, 2) + " km";
        String oldString = "Increase savings by " + savings + " worth of fuel.";
        String newString = oldString.replaceAll(savings, "<b><i>" + savings + "</i></b>");
        holder.savingsTextView.setText(Html.fromHtml(newString));

        // Binds trip bookings data
        // Create layout manager with initial prefetch item count
        LinearLayoutManager tripBookingsLayoutManager = new LinearLayoutManager(
                holder.tripBookingsRecyclerView.getContext(),
                LinearLayoutManager.VERTICAL,
                false
        );
        tripBookingsLayoutManager.setInitialPrefetchItemCount(trip.getBookings().size());
        // Create sub item view adapter
        UserBookingListItemAdapter bookingListItemAdapter = new UserBookingListItemAdapter(trip.getBookings(), context);
        holder.tripBookingsRecyclerView.setLayoutManager(tripBookingsLayoutManager);
        holder.tripBookingsRecyclerView.setAdapter(bookingListItemAdapter);
        holder.tripBookingsRecyclerView.setRecycledViewPool(tripBookingsRecyclerViewPool);

        // Binds trip stops data
        // Create layout manager with initial prefetch item count
        LinearLayoutManager tripStopsLayoutManager = new LinearLayoutManager(
                holder.tripStopsRecyclerView.getContext(),
                LinearLayoutManager.VERTICAL,
                false
        );
        tripStopsLayoutManager.setInitialPrefetchItemCount(trip.getStops().size());
        // Create sub item view adapter
        TripStopListItemAdapter tripStopListItemAdapter = new TripStopListItemAdapter(trip.getStops());
        holder.tripStopsRecyclerView.setLayoutManager(tripStopsLayoutManager);
        holder.tripStopsRecyclerView.setAdapter(tripStopListItemAdapter);
        holder.tripStopsRecyclerView.setRecycledViewPool(tripStopsRecyclerViewPool);
    }

    @Override
    public int getItemCount() {
        return tripList.size();
    }

    /**
     * Holds the view
     */
    class RecommendedTripListViewHolder extends RecyclerView.ViewHolder {

        private TextView savingsTextView;
        private RecyclerView tripBookingsRecyclerView, tripStopsRecyclerView;
        private Button viewTripButton;

        public RecommendedTripListViewHolder(@NonNull View itemView) {
            super(itemView);
            tripBookingsRecyclerView = itemView.findViewById(R.id.tripBookingsRecyclerView);
            tripStopsRecyclerView = itemView.findViewById(R.id.tripStopsRecyclerView);
            savingsTextView = itemView.findViewById(R.id.savingsTextView);
            viewTripButton = itemView.findViewById(R.id.viewTripButton);

            // If the user clicks on the View Trip button, they are redirected to Show Recommended Trip Activity
            viewTripButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Trip trip = tripList.get(getAdapterPosition());
                    Log.i("Adapter position is", String.valueOf(getAdapterPosition()));
                    Intent intent = new Intent(context, ShowRecommendedTripActivity.class);
                    intent.putExtra("selectedTrip", selectedTrip);
                    intent.putExtra("recommendedTrip", trip);
                    context.startActivity(intent);
                }
            });
        }
    }
}


