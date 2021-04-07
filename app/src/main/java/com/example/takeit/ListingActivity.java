package com.example.takeit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.takeit.Models.Comment;
import com.example.takeit.Models.Listing;
import com.parse.ParseFile;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class ListingActivity extends AppCompatActivity {

    private RecyclerView rvComments;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listing);




        ImageView ivPicture = findViewById(R.id.ivPicture);
        TextView tvPrice= findViewById(R.id.tvPrice);
        TextView tvDescription = findViewById(R.id.tvDescription);
        TextView tvTitle = findViewById(R.id.tvTitle);
        TextView tvUsername = findViewById(R.id.tvUsername);
        RecyclerView rvComments;

        Context context;


        Listing listing = Parcels.unwrap(getIntent().getParcelableExtra("listing"));

        tvDescription.setText(listing.getDescription());
        tvTitle.setText(listing.getTitle());
        tvUsername.setText(listing.getUser().getUsername());
        String price = Double.toString(listing.getPrice());
        tvPrice.setText(price);
//        ParseFile image = listing.getImage();
//        if(image != null){
//            Glide.with(context).load(listing.getImage().getUrl()).into(ivPicture);
//        }
    }
}