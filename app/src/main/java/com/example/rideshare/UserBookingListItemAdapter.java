package com.example.rideshare;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

/**
 * RecyclerView Adapter for displaying user bookings in a list one after the other
 *
 * @author Sukriti
 * @version 1.0
 */
public class UserBookingListItemAdapter extends RecyclerView.Adapter<UserBookingListItemAdapter.UserBookingListItemViewHolder> {

    private List<UserBooking> bookingList;
    private Boolean isBackgroundWhite = false;
    private Context context;
    private FirebaseFirestore db;
    private Context parentContext;

    public UserBookingListItemAdapter(List<UserBooking> bookingList, Context parentContext) {
        this.bookingList = bookingList;
        db = FirebaseFirestore.getInstance();
        this.parentContext = parentContext;
    }

    public UserBookingListItemAdapter(List<UserBooking> bookingList, Boolean isBackgroundWhite) {
        this.bookingList = bookingList;
        this.isBackgroundWhite = isBackgroundWhite;
        db = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public UserBookingListItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_booking_list_item, parent, false);
        return new UserBookingListItemViewHolder(view);
    }

    /**
     * Binds stop data to the view.
     */
    @Override
    public void onBindViewHolder(@NonNull UserBookingListItemViewHolder holder, int position) {
        UserBooking booking = bookingList.get(position);
        holder.bookingUsernameTextView.setText(booking.getUserName());
        holder.bookingOriginTextView.setText(booking.getOrigin().name);
        holder.bookingDestinationTextView.setText(booking.getDestination().name);
        holder.bookingDatetimeTextView.setText(booking.getPickupTimeBST());

        if (isBackgroundWhite)
            holder.mainLayout.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.white));
    }

    @Override
    public int getItemCount() {
        return bookingList.size();
    }

    /**
     * Holds the view
     */
    class UserBookingListItemViewHolder extends RecyclerView.ViewHolder {

        TextView bookingUsernameTextView, bookingOriginTextView, bookingDestinationTextView, bookingDatetimeTextView;
        LinearLayout mainLayout;
        ImageView getPhoneImageView;
        private static final String UK_EXTENSION = "+44";

        public UserBookingListItemViewHolder(@NonNull View itemView) {
            super(itemView);
            bookingUsernameTextView = itemView.findViewById(R.id.bookingUsernameTextView);
            bookingOriginTextView = itemView.findViewById(R.id.bookingOriginTextView);
            bookingDestinationTextView = itemView.findViewById(R.id.bookingDestinationTextView);
            bookingDatetimeTextView = itemView.findViewById(R.id.bookingDatetimeTextView);
            mainLayout = itemView.findViewById(R.id.mainLayout);
            getPhoneImageView = itemView.findViewById(R.id.getPhoneImageView);

            // If user clicks on the phone icon to display display alert dialog with user's contact number
            getPhoneImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UserBooking booking = bookingList.get(getAdapterPosition());
                    DocumentReference docRef = db.collection(FirestoreCollections.CONTACTS).document(booking.getUserId());
                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                User user = document.toObject(User.class);

                                AlertDialog.Builder builder = new AlertDialog.Builder(parentContext == null ? context : parentContext);
                                builder.setTitle("Phone number")
                                        .setMessage(UK_EXTENSION + " " + user.getPhoneNumber())
                                        .setPositiveButton("Okay", null);

                                AlertDialog alertDialog = builder.create();
                                alertDialog.show();
                            }
                        }
                    });
                }
            });
        }
    }
}

