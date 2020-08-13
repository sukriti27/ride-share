package com.example.rideshare;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
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
 * RecyclerView Adapter for displaying user trips in a list one after the other
 *
 * @author Sukriti
 * @version 1.0
 */
public class HistoryTripListItemAdapter extends RecyclerView.Adapter<HistoryTripListItemAdapter.HistoryTripListViewHolder> {

    private Context context;
    private RecyclerView.RecycledViewPool tripBookingsRecyclerViewPool = new RecyclerView.RecycledViewPool();
    private RecyclerView.RecycledViewPool tripStopsRecyclerViewPool = new RecyclerView.RecycledViewPool();
    private List<Trip> tripList;

    public HistoryTripListItemAdapter(Context context, List<Trip> tripList) {
        this.context = context;
        this.tripList = tripList;
    }

    @NonNull
    @Override
    public HistoryTripListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trip_list_item, parent, false);
        return new HistoryTripListViewHolder(view);
    }

    /**
     * Binds trip data to the view.
     */
    @Override
    public void onBindViewHolder(@NonNull HistoryTripListViewHolder holder, int position) {
        Trip trip = tripList.get(position);

        String savings = RoundDouble.round(trip.getSavingsDistanceMeters() / 1000.0, 2) + " km";
        String oldString = "Trip savings worth " + savings + " of fuel.";
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
    class HistoryTripListViewHolder extends RecyclerView.ViewHolder {

        private TextView savingsTextView;
        private RecyclerView tripBookingsRecyclerView, tripStopsRecyclerView;
        private Button viewTripButton;

        public HistoryTripListViewHolder(@NonNull View itemView) {
            super(itemView);
            tripBookingsRecyclerView = itemView.findViewById(R.id.tripBookingsRecyclerView);
            tripStopsRecyclerView = itemView.findViewById(R.id.tripStopsRecyclerView);
            savingsTextView = itemView.findViewById(R.id.savingsTextView);
            viewTripButton = itemView.findViewById(R.id.viewTripButton);

            // If the user clicks on the View Trip button, they are redirected to Show Trip Activity
            viewTripButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Trip trip = tripList.get(getAdapterPosition());
                    Intent intent = new Intent(context, ShowTripActivity.class);
                    intent.putExtra("tripId", trip.getTripId());
                    context.startActivity(intent);
                }
            });
        }
    }
}




