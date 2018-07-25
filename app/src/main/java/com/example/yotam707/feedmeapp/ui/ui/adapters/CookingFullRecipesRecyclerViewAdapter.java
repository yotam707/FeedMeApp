package com.example.yotam707.feedmeapp.ui.ui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yotam707.feedmeapp.R;
import com.example.yotam707.feedmeapp.domain.FullRecipe;

import java.util.List;

public class CookingFullRecipesRecyclerViewAdapter  extends RecyclerView.Adapter<CookingFullRecipesRecyclerViewAdapter.ViewHolder> {
    private final String TAG = this.getClass().getSimpleName();
    //vars
    private List<FullRecipe> recipeList;
    private Context mContext;

    public CookingFullRecipesRecyclerViewAdapter(List<FullRecipe> recipeList, Context mContext){
        this.recipeList = recipeList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView image;
        TextView name;
        ImageView add;
        public ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image_view);
            name = itemView.findViewById(R.id.name);
            add = itemView.findViewById(R.id.lw_add);
        }
    }
}
