package com.example.foodzilla;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RatingAdapter extends RecyclerView.Adapter<RatingAdapter.MyRatingViewHolder> {
    List<Rating> ratingList = new ArrayList<>();

    @NonNull
    @Override
    public MyRatingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_card, parent, false);
        return new MyRatingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyRatingViewHolder holder, int position) {
        holder.textEmail.setText(ratingList.get(position).getEmail());
        holder.textReview.setText(ratingList.get(position).getReview());
        holder.textStar.setText(ratingList.get(position).getStar() + "");
    }

    @Override
    public int getItemCount() {
        return ratingList == null ? 0 : ratingList.size();
    }

    public void setRatingList(List<Rating> ratingList) {
        this.ratingList = ratingList;
        notifyDataSetChanged();
    }

    public static class MyRatingViewHolder extends RecyclerView.ViewHolder{
        public final TextView textEmail;
        public final TextView textStar;
        public final TextView textReview;

        public MyRatingViewHolder(@NonNull View itemView) {
            super(itemView);
            textEmail = itemView.findViewById(R.id.textEmail);
            textStar = itemView.findViewById(R.id.textStar);
            textReview = itemView.findViewById(R.id.textReview);
        }
    }
}
