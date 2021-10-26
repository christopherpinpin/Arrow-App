package com.example.arrow;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.*;

public class CollegeProfAdapter extends RecyclerView.Adapter<CollegeProfAdapter.FeaturedViewHolder>{
    ArrayList<RecommendedHelperClass> collProfs;
    public static final String KEY_NAME = "KEY_NAME";
    public static final String KEY_IMG = "KEY_IMG";
    public static final String KEY_DESC = "KEY_DESC";
    public static final String KEY_RATING = "KEY_RATING";

    public CollegeProfAdapter(ArrayList<RecommendedHelperClass> collProfs) {
        this.collProfs = collProfs;
    }

    public static class FeaturedViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView name, desc;
        RelativeLayout rl_card;
        RatingBar rbRating;

        public FeaturedViewHolder(@NonNull View itemView) {
            super(itemView);

            //Hooks
            image = itemView.findViewById(R.id.mv_image);
            name = itemView.findViewById(R.id.mv_title);
            desc = itemView.findViewById(R.id.mv_desc);
            rl_card = itemView.findViewById(R.id.rl_coll_card);
            rbRating = itemView.findViewById(R.id.overall_rating);


        }
    }

    @NonNull
    @Override
    public FeaturedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.collegeprofs_card_view, parent, false);
        FeaturedViewHolder featuredViewHolder = new FeaturedViewHolder(view);
        return featuredViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FeaturedViewHolder holder, int position) {

        RecommendedHelperClass recommendedHelperClass = collProfs.get(position);

        holder.image.setImageResource(recommendedHelperClass.getImage());
        holder.name.setText(recommendedHelperClass.getName());
        holder.desc.setText(recommendedHelperClass.getDescription());
        holder.rbRating.setRating(recommendedHelperClass.getRating());
        holder.rl_card.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ProfessorProfile.class);
                intent.putExtra(KEY_NAME, collProfs.get(position).getName());
                intent.putExtra(KEY_IMG, collProfs.get(position).getImage());
                intent.putExtra(KEY_DESC, collProfs.get(position).getDescription());
                intent.putExtra(KEY_RATING, collProfs.get(position).getRating());
                v.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return collProfs.size();
    }

}
