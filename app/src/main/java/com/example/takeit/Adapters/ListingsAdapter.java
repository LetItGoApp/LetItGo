package com.example.takeit.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.takeit.GPSTracker;
import com.example.takeit.ListingActivity;
import com.example.takeit.Models.Listing;

import com.example.takeit.R;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;

import org.parceler.Parcels;

import java.io.IOError;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class ListingsAdapter extends RecyclerView.Adapter<ListingsAdapter.ViewHolder>{

    private final Context context;
    private final List<Listing> listings;

    public ListingsAdapter(Context context, List<Listing> listings){
        this.context = context;
        this.listings = listings;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_listing,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Listing list = listings.get(position);
        holder.bind(list);
    }

    @Override
    public int getItemCount() {
        return listings.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        RelativeLayout container;
        private final TextView tvUsername;
        private final ImageView ivPicture;
        private final TextView tvPrice;
        private final TextView tvDescription;
        private final TextView tvTitle;
        private final TextView tvLocation;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            ivPicture = itemView.findViewById(R.id.ivPicture);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            container = itemView.findViewById(R.id.container);
            tvLocation = itemView.findViewById(R.id.tvLocation);
        }

        @SuppressLint("DefaultLocale")
        public void bind(Listing list) {
            //bind the post data to the view elements

            tvDescription.setText(list.getDescription());
            tvTitle.setText(list.getTitle());
            tvUsername.setText(list.getUser().getUsername());
            tvPrice.setText(String.format("%.2f", list.getPrice()));
            tvLocation.setText(list.getCityState());
            ParseFile image = list.getImage();

            if(image != null){
                Glide.with(context).load(list.getImage().getUrl()).into(ivPicture);
            }

            // 1. Register click listener on the entire row.
            // 2. Navigate to a new activity on tap.
            container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, ListingActivity.class);
                    i.putExtra("listing", Parcels.wrap(list));
                    context.startActivity(i);
                }
            });
        }
    }

    // Clean all elements of the recycler
    public void clear(){
        listings.clear();
        notifyDataSetChanged();
    }

}
