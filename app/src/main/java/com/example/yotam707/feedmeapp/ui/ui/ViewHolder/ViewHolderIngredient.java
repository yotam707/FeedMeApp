package com.example.yotam707.feedmeapp.ui.ui.ViewHolder;

import android.view.View;
import android.widget.TextView;

import com.example.yotam707.feedmeapp.R;
import com.example.yotam707.feedmeapp.domain.Ingredients;
import com.example.yotam707.feedmeapp.ui.ui.interfaces.DetailedListItem;

public class ViewHolderIngredient extends ViewHolder {
    private TextView description;
    private TextView quantity;

    public ViewHolderIngredient(View view) {
        super(view);
        description = view.findViewById(R.id.ingredient_frag_item);
        quantity = view.findViewById(R.id.quantity);
    }

    @Override
    public void bindType(DetailedListItem item) {
        description.setText(((Ingredients)item).getName());
        quantity.setText(Float.toString(((Ingredients)item).getAmount()));
    }
}
