package com.example.takeit;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.takeit.Models.Listing;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView btnLogout = findViewById(R.id.logout);

        queryPost();
        Log.i(TAG, "Logged in user: " + ParseUser.getCurrentUser().getUsername());
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });
    }

    public void signOut() {
        Log.i(TAG,"Logged out.");
        ParseUser.logOut();
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
        finish();
    }

    private void queryPost() {
        ParseQuery<Listing> query = ParseQuery.getQuery(Listing.class);
        query.include(Listing.KEY_USER);
        query.findInBackground(new FindCallback<Listing>() {
            @Override
            public void done(List<Listing> listings, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue getting listings.");
                    return;
                }
                for (Listing listing : listings) {
                    Log.i(TAG, "Post: " + listing.getDescription() + ", Username: " + listing.getUser().getUsername());
                }
            }
        });
    }
}