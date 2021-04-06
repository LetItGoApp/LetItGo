package com.example.takeit.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.takeit.ListingsAdapter;
import com.example.takeit.Models.Listing;
import com.example.takeit.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

import static com.example.takeit.Models.Listing.KEY_CREATED_AT;

public class HomeFragment extends Fragment {
    public static final String TAG = "PostsFragment";
    private RecyclerView rvHome;
    private ListingsAdapter adapter;
    private List<Listing> allListings;
    SwipeRefreshLayout swipeContainer;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvHome = view.findViewById(R.id.rvHome);
        allListings = new ArrayList<>();
        adapter = new ListingsAdapter(getContext(), allListings);

        rvHome.setAdapter(adapter);

        //may be an error
        rvHome.setLayoutManager(new LinearLayoutManager(getContext()));

        swipeContainer = view.findViewById(R.id.swipeContainer);
        swipeContainer.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light
        );
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.i(TAG, "fetching new data");
                adapter.clear();
                queryListings();
            }
        });

        queryListings();

    }
    private void queryListings(){
        ParseQuery<Listing> query = ParseQuery.getQuery(Listing.class);
        query.include(Listing.KEY_USER);
        query.addDescendingOrder(KEY_CREATED_AT);
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
                swipeContainer.setRefreshing(false);
            }
        });
    }

}