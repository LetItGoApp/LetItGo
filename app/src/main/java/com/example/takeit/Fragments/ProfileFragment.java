package com.example.takeit.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.takeit.Adapters.ListingsAdapter;
import com.example.takeit.LoginActivity;
import com.example.takeit.Models.Listing;
import com.example.takeit.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import static com.example.takeit.Models.Listing.KEY_CREATED_AT;

public class ProfileFragment extends Fragment {

    public static final String TAG = "ProfileFragment";
    private RecyclerView rvProfile;
    private ListingsAdapter adapter;
    private TextView tvUser;
    private List<Listing> allListings;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvProfile = view.findViewById(R.id.rvProfile);
        allListings = new ArrayList<>();
        adapter = new ListingsAdapter(getContext(), allListings);
        tvUser = view.findViewById(R.id.tvUser);

        rvProfile.setAdapter(adapter);

        //may be an error
        rvProfile.setLayoutManager(new LinearLayoutManager(getContext()));

        if(ParseUser.getCurrentUser() != null)
        {
            tvUser.setText(ParseUser.getCurrentUser().getUsername());
        }

        queryListings();

        Log.i(TAG, "Logged in user: " + ParseUser.getCurrentUser().getUsername());
        Button btnLogout = view.findViewById(R.id.btnLogout);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParseUser.logOut();
                ParseUser currentUser = ParseUser.getCurrentUser();

                Toast.makeText(getContext(), "Log Out Successful!", Toast.LENGTH_LONG).show();
                goLoginActivity();
            }
        });

    }

    private void goLoginActivity() {
        Intent intent = new Intent(getContext(), LoginActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    private void queryListings(){
        ParseQuery<Listing> query = ParseQuery.getQuery(Listing.class);
        query.include(Listing.KEY_USER);
        query.addDescendingOrder(KEY_CREATED_AT);
        query.whereEqualTo(Listing.KEY_USER, ParseUser.getCurrentUser());
        query.findInBackground(new FindCallback<Listing>() {
            @Override
            public void done(List<Listing> listings, ParseException e) {
                if(e != null){
                    Log.e(TAG, "Issue getting listings", e);
                    return;
                }
                for(Listing listing : listings){
                    Log.i(TAG,"Listing: "+ listing.getDescription() + ", username" + listing.getUser().getUsername());
                }
                allListings.addAll(listings);
                adapter.notifyDataSetChanged();
            }
        });
    }
}