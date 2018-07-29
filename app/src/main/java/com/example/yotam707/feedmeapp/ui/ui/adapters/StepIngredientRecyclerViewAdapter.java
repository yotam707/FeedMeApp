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

public class StepIngredientRecyclerViewAdapter extends RecyclerView.Adapter<StepIngredientRecyclerViewAdapter.ViewHolder> {

    private final String TAG = this.getClass().getSimpleName();
    private List<Ingredients> ingredientsList;
    private Context mContext;

    public StepIngredientRecyclerViewAdapter(Context mContext){
        this.mContext = mContext;
    }

    public void SetIngrdients(List<Ingredients> ingredientsList){
        this.ingredientsList = ingredientsList;
    }

    @NonNull
    @Override
    public StepIngredientRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cooking_ingrid_item, parent, false);
        return new StepIngredientRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StepIngredientRecyclerViewAdapter.ViewHolder holder, int position) {
        if(ingredientsList != null) {
            final Ingredients ing = ingredientsList.get(position);
            GlideApp
                    .with(mContext)
                    .load(FirestoreStorageManager.storageReference.child(ing.getName()))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .fitCenter()
                    .into(holder.image);

            holder.ingredientName.setText(ing.getName());
            String amount = String.format("%.2f", ing.getAmount());
            holder.ingredientAmount.setText(amount + " " + ing.getUnit());
        }


    }

    @Override
    public int getItemCount() {
        if(ingredientsList != null)
            return this.ingredientsList.size();
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView ingredientName;
        TextView ingredientAmount;
        public ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.cooking_ingredient_image);
            ingredientName = itemView.findViewById(R.id.cooking_ingredient_title);
            ingredientAmount = itemView.findViewById(R.id.cooking_ingredient_quantity);
        }
    }
}
