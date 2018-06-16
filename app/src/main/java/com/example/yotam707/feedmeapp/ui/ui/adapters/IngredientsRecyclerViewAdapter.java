package com.example.yotam707.feedmeapp.ui.ui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.yotam707.feedmeapp.R;
import com.example.yotam707.feedmeapp.domain.Ingredients;

import java.util.List;

public class IngredientsRecyclerViewAdapter extends RecyclerView.Adapter<IngredientsRecyclerViewAdapter.ViewHolder> {

    private final String TAG = this.getClass().getSimpleName();
    private List<Ingredients> ingredientsList;
    private Context mContext;

    public IngredientsRecyclerViewAdapter(List<Ingredients> ingredientsList, Context mContext) {
        this.ingredientsList = ingredientsList;
        this.mContext = mContext;
    }


    @NonNull
    @Override
    public IngredientsRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ingredient_list_item, parent, false);
        return new IngredientsRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientsRecyclerViewAdapter.ViewHolder holder, int position) {
        Ingredients ingredients = ingredientsList.get(position);
        holder.amount.setText(Float.toString(ingredients.getAmount()));
        holder.description.setText(ingredients.getName());
    }

    @Override
    public int getItemCount() {
        return ingredientsList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView description;
        TextView amount;

        public ViewHolder(View view){
            super(view);
            description = view.findViewById(R.id.ingredient_frag_item);
            amount = view.findViewById(R.id.quantity);
        }
    }
}
