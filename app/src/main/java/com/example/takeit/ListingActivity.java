package com.example.takeit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.takeit.Adapters.CommentAdapter;
import com.example.takeit.Models.Comment;
import com.example.takeit.Models.Listing;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.parceler.Parcels;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.example.takeit.Models.Listing.KEY_CREATED_AT;

public class ListingActivity extends AppCompatActivity {
    public static final String TAG = "ListingActivity";

    private List<Comment> allComments;
    private CommentAdapter adapter;
    SwipeRefreshLayout swipeContainer;


    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listing);
        RecyclerView rvComments = findViewById(R.id.rvComments);
        allComments = new ArrayList<>();

        adapter = new CommentAdapter(this, allComments);
        rvComments.setAdapter(adapter);
        rvComments.setLayoutManager(new LinearLayoutManager(this));

        Button btnAddComment = findViewById(R.id.btnAddComment);
        EditText etWriteComment = findViewById(R.id.etWriteComment);
        ImageView ivPicture = findViewById(R.id.ivPicture);
        TextView tvPrice= findViewById(R.id.tvPrice);
        TextView tvDescription = findViewById(R.id.tvDescription);
        TextView tvTitle = findViewById(R.id.tvTitle);
        TextView tvUsername = findViewById(R.id.tvUsername);
        TextView tvLocation = findViewById(R.id.tvLocation);
        swipeContainer = findViewById(R.id.swipeContainer);

        Listing listing = Parcels.unwrap(getIntent().getParcelableExtra("listing"));

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
                queryComments(listing);
            }
        });

        queryComments(listing);

        tvDescription.setText(listing.getDescription());
        tvTitle.setText(listing.getTitle());
        tvUsername.setText(listing.getUser().getUsername());
        tvPrice.setText(String.format("%.2f", listing.getPrice()));
        tvLocation.setText(listing.getCityState());
        ParseFile image = listing.getImage();
        if(image != null){
            Glide.with(ListingActivity.this).load(listing.getImage().getUrl()).into(ivPicture);
        }

        btnAddComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content;
                 //username of the comment being commented on
            Log.i(TAG, listing.getObjectId());


                if(etWriteComment.getText().toString().isEmpty()) {
                    Toast.makeText(ListingActivity.this, "Comment cannot be empty.", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    content = etWriteComment.getText().toString();
                }

               ParseUser currentUser = ParseUser.getCurrentUser();
                savePost(content, currentUser, listing);
                etWriteComment.setText(null);
            }
        });
    }

    private void savePost(String content, ParseUser currentUser, ParseObject commentParent) {

        Comment comment = new Comment();
        comment.setContent(content);
        comment.setUser(currentUser);
        comment.setCommentParent(commentParent);

        comment.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e != null){
                    Log.e(TAG, "Error while saving", e);
                    Toast.makeText(ListingActivity.this, "Error while saving!", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(ListingActivity.this, "Comment posted!", Toast.LENGTH_SHORT).show();
                Log.i(TAG, "Comment save was successful!");
            }
        });
    }

    private void queryComments(ParseObject listing) {
        ParseQuery<Comment> query = ParseQuery.getQuery(Comment.class);
        query.include(Comment.KEY_USER_COMMENTING);
        query.include(Comment.KEY_COMMENT_PARENT);
        query.whereContains(Comment.KEY_COMMENT_PARENT, listing.getObjectId());
        query.findInBackground(new FindCallback<Comment>() {
            public void done(List<Comment> comments, ParseException e) {
                if(e != null){
                    Log.e(TAG, "Issue getting comments", e);
                    return;
                }
                for(Comment comment : comments) {
                    Log.i(TAG,"Comment: "+ comment.getContent() + ", username: "
                            + comment.getUserCommenting().getUsername());
                }
                allComments.addAll(comments);
                adapter.notifyDataSetChanged();
                swipeContainer.setRefreshing(false);
            }
        });
    }
}


