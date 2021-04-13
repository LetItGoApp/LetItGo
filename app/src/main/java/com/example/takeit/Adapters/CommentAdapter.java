package com.example.takeit.Adapters;

import android.content.Context;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.takeit.Models.Comment;
import com.example.takeit.R;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder>{

    private static final Object TAG = "CommentAdapter";
    Context context;
    List<Comment> comments;



    public CommentAdapter(Context context, List<Comment> comments){
        this.context = context;
        this.comments = comments;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_comment,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Comment comment = comments.get(position);
        holder.bind(comment);
    }

    @Override
    public int getItemCount() {


      return comments.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
         TextView tvUserName;
         TextView tvContent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            tvContent = itemView.findViewById(R.id.tvContent);
        }

        public void bind(Comment comment) {

//           tvUser.setText(ParseUser.getQuery().whereEqualTo(comment.KEY_USER_COMMENTING,comment.KEY_COMMENT_PARENT).toString());
            tvUserName.setText(comment.getUserCommenting().getUsername());

            tvContent.setText(comment.getContent());
        }


    }

    // Clean all elements of the recycler
    public void clear(){
        comments.clear();
        notifyDataSetChanged();
    }
}
