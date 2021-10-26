package com.example.arrow;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.*;

public class AddFeaturedProfAdapter extends RecyclerView.Adapter<AddFeaturedProfAdapter.FeaturedViewHolder>{
    ArrayList<RecommendedHelperClass> cardProfs;
    public static final String KEY_NAME = "KEY_NAME";
    public static final String KEY_IMG = "KEY_IMG";
    public static final String KEY_DESC = "KEY_DESC";
    public static final String KEY_UID = "KEY_UID";

    public AddFeaturedProfAdapter(ArrayList<RecommendedHelperClass> cardProfs) {
        this.cardProfs = cardProfs;
    }

    public static class FeaturedViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView name, desc;
        LinearLayout ll_card;
        public FeaturedViewHolder(@NonNull View itemView) {
            super(itemView);

            //Hooks
            image = itemView.findViewById(R.id.featured_image);
            name = itemView.findViewById(R.id.featured_name);
            desc = itemView.findViewById(R.id.featured_desc);
            ll_card = itemView.findViewById(R.id.ll_allcard);

        }
    }

    @NonNull
    @Override
    public FeaturedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_featured_card, parent, false);
        FeaturedViewHolder featuredViewHolder = new FeaturedViewHolder(view);
        return featuredViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FeaturedViewHolder holder, int position) {

        RecommendedHelperClass recommendedHelperClass = cardProfs.get(position);

        holder.image.setImageResource(recommendedHelperClass.getImage());
        holder.name.setText(recommendedHelperClass.getName());
        holder.desc.setText(recommendedHelperClass.getDescription());
        holder.ll_card.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ProfessorProfile.class);
                intent.putExtra(KEY_NAME, cardProfs.get(position).getName());
                intent.putExtra(KEY_IMG, cardProfs.get(position).getImage());
                intent.putExtra(KEY_DESC, cardProfs.get(position).getDescription());
                //intent.putExtra(KEY_UID, cardProfs.get(position).get)
                v.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return cardProfs.size();
    }

}
