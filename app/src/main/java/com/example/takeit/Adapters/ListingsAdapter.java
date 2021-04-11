package com.example.takeit.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.takeit.ListingActivity;
import com.example.takeit.Models.Listing;

import com.example.takeit.R;
import com.parse.ParseFile;

import org.parceler.Parcels;

import java.util.List;

public class ListingsAdapter extends RecyclerView.Adapter<ListingsAdapter.ViewHolder>{

    private Context context;
    private List<Listing> listings;

    public ListingsAdapter(Context context, List<Listing> listings){
        this.context = context;
        this.listings = listings;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =LayoutInflater.from(context).inflate(R.layout.item_listing,parent,false);
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
        private TextView tvUsername;
        private ImageView ivPicture;
        private TextView tvPrice;
        private TextView tvDescription;
        private TextView tvTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            ivPicture = itemView.findViewById(R.id.ivPicture);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            container = itemView.findViewById(R.id.container);
        }

        public void bind(Listing list) {
            //bind the post data to the view elements

            tvDescription.setText(list.getDescription());
            tvTitle.setText(list.getTitle());
            tvUsername.setText(list.getUser().getUsername());
            String price = Double.toString(list.getPrice());
            tvPrice.setText(price);
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
