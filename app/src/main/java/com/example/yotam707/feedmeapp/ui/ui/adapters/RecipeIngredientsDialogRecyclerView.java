package com.example.yotam707.feedmeapp.ui.ui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.yotam707.feedmeapp.GlideApp;
import com.example.yotam707.feedmeapp.R;
import com.example.yotam707.feedmeapp.data.Firestore.FirestoreStorageManager;
import com.example.yotam707.feedmeapp.domain.Ingredients;

import java.util.List;

public class RecipeIngredientsDialogRecyclerView extends RecyclerView.Adapter<RecipeIngredientsDialogRecyclerView.ViewHolder> {

    private final String TAG = this.getClass().getSimpleName();
    private List<Ingredients> extendedIngredients;
    private Context mContext;

    public RecipeIngredientsDialogRecyclerView(List<Ingredients> extendedIngredients, Context mContext) {
        this.extendedIngredients = extendedIngredients;
        this.mContext = mContext;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ingredient_dialog_list_item, parent, false);
        return new RecipeIngredientsDialogRecyclerView.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Ingredients ingredients = extendedIngredients.get(position);
        holder.amount.setText(Float.toString(ingredients.getAmount()) + ingredients.getUnit());
        holder.name.setText(ingredients.getName());
        GlideApp
                .with(mContext)
                .load(FirestoreStorageManager.storageReference.child(ingredients.getName()))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .optionalFitCenter()
                .into(holder.image);
    }

    @Override
    public int getItemCount() {
        return extendedIngredients.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView amount;
        ImageView image;
        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.ingredient_dialog_name);
            amount = itemView.findViewById(R.id.ingredient_dialog_quantity);
            image = itemView.findViewById(R.id.dialog_ingredient_image);
        }
    }
}
