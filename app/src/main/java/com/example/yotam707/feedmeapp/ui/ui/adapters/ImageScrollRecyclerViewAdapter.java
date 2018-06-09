package com.example.yotam707.feedmeapp.ui.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import com.example.yotam707.feedmeapp.DetailActivity;
import com.example.yotam707.feedmeapp.GlideApp;
import com.example.yotam707.feedmeapp.R;
import com.example.yotam707.feedmeapp.data.Firestore.FirestoreStorageManager;
import com.example.yotam707.feedmeapp.domain.Recipe;

import java.util.ArrayList;
import java.util.List;

public class ImageScrollRecyclerViewAdapter extends RecyclerView.Adapter<ImageScrollRecyclerViewAdapter.ViewHolder> {

    private final String TAG = this.getClass().getSimpleName();

    //vars
    private List<Recipe> recipeList;
    private Context mContext;

    public ImageScrollRecyclerViewAdapter(List<Recipe> recipeList, Context mContext) {
        this.recipeList = recipeList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.type_recipe_image_display, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Recipe currentRecipe = recipeList.get(position);
        final int recipeId = currentRecipe.getId();
        GlideApp
                .with(mContext)
                .load(FirestoreStorageManager.storageReference.child(currentRecipe.getImage()))
                .fitCenter()
                .into(holder.image);

        holder.name.setText(currentRecipe.getTitle());
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, DetailActivity.class);
                intent.putExtra("courseId", Integer.toString((recipeId)));
                mContext.startActivity(intent);
                Log.d(TAG, "OnClick: clicked on an image");
            }
        });
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView image;
        TextView name;

        public ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image_view);
            name = itemView.findViewById(R.id.name);
        }
    }
}
