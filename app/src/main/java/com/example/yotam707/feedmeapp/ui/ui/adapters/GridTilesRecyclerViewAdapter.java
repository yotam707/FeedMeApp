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

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.yotam707.feedmeapp.DetailActivity;
import com.example.yotam707.feedmeapp.GlideApp;
import com.example.yotam707.feedmeapp.R;
import com.example.yotam707.feedmeapp.data.Firestore.FirestoreStorageManager;
import com.example.yotam707.feedmeapp.domain.CategoryTypeEnum;
import com.example.yotam707.feedmeapp.domain.Recipe;

import java.util.List;

public class GridTilesRecyclerViewAdapter extends RecyclerView.Adapter<GridTilesRecyclerViewAdapter.ViewHolder> {

    private List<Recipe> recipeList;
    private Context mContext;
    private CategoryTypeEnum type;

    private final String TAG = this.getClass().getSimpleName();

    public void SetRecipeList(List<Recipe> recipeList){
        this.recipeList = recipeList;
    }

    public GridTilesRecyclerViewAdapter(List<Recipe> recipeList, CategoryTypeEnum type, Context mContext) {
        this.recipeList = recipeList;
        this.mContext = mContext;
        this.type = type;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_tile_item, parent, false);
        return new GridTilesRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Recipe currentRecipe = recipeList.get(position);

        final int recipeId = currentRecipe.getId();
        if(currentRecipe.getImage() != null) {
            GlideApp
                    .with(mContext)
                    .load(FirestoreStorageManager.storageReference.child(currentRecipe.getImage()))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .fitCenter()
                    .into(holder.image);

        }
        holder.name.setText(currentRecipe.getTitle());
//        holder.image.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Intent intent = new Intent(mContext, DetailActivity.class);
//                intent.putExtra("courseId", Integer.toString((recipeId)));
//                intent.putExtra("type", type.name());
//                mContext.startActivity(intent);
//                Log.d(TAG, "OnClick: clicked on an image");
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.category_item_text);
            image = itemView.findViewById(R.id.category_item_image);
        }


    }
}
