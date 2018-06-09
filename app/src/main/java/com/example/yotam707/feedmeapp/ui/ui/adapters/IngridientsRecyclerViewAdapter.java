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

public class IngridientsRecyclerViewAdapter extends RecyclerView.Adapter<IngridientsRecyclerViewAdapter.ViewHolder> {

    private List<Ingredients> extendedIngredients;
    private Context mContext;
    private final String TAG = this.getClass().getSimpleName();


    public IngridientsRecyclerViewAdapter(List<Ingredients> extendedIngredients, Context mContext) {
        this.extendedIngredients = extendedIngredients;
        this.mContext = mContext;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ingredient_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Ingredients ingredient = extendedIngredients.get(position);
        holder.name.setText(ingredient.getName());
        holder.quantity.setText(ingredient.getUnit() + " " + ingredient.getAmount());
    }

    @Override
    public int getItemCount() {
        return extendedIngredients.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView quantity;
        TextView name;
        public ViewHolder(View itemView) {
            super(itemView);
            quantity = itemView.findViewById(R.id.quantity);
            name = itemView.findViewById(R.id.ingredient_frag_item);
        }
    }
}
