package com.example.takeit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.takeit.Models.Listing;

import com.parse.ParseFile;

import org.w3c.dom.Text;

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

        private TextView tvUsername;
        private ImageView ivPicture;
        private TextView tvPrice;
        private TextView tvDescription;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            ivPicture = itemView.findViewById(R.id.ivPicture);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvDescription = itemView.findViewById(R.id.tvDescription);
        }

        public void bind(Listing list) {
            //bind the post data to the view elements

            tvDescription.setText(list.getDescription());
            tvUsername.setText(list.getUser().getUsername());
            String price = Double.toString(list.getPrice());
            tvPrice.setText(price);
            ParseFile image = list.getImage();
            if(image != null){
                Glide.with(context).load(list.getImage().getUrl()).into(ivPicture);
            }
        }
    }
}
