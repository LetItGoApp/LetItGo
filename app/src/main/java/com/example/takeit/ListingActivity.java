package com.example.takeit;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.takeit.Models.Listing;

import org.parceler.Parcels;

public class ListingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listing);

        TextView tvProduct = findViewById(R.id.tvProduct);

        Listing listing = Parcels.unwrap(getIntent().getParcelableExtra("listing"));
        tvProduct.setText(listing.getTitle());
    }
}