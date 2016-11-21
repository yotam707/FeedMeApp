package com.example.yotam707.feedmeapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Adir on 17/11/2016.
 */

public class IngredientsListAdapter extends ArrayAdapter<Ingredient> {
    private final Context context;
    private final List<Ingredient> values;

    public IngredientsListAdapter(Context context, List<Ingredient> values) {
        super(context, R.layout.ingredient_list_item,values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.ingredient_list_item, parent, false);
        ImageView bullet = (ImageView) rowView.findViewById(R.id.imageView2);
        TextView ingredientName = (TextView) rowView.findViewById(R.id.ingredient);
        TextView ingredientQuantity = (TextView) rowView.findViewById(R.id.quantity);
        ingredientName.setText(values.get(position).getName());
        ingredientQuantity.setText(String.valueOf(values.get(position).getQuantity()));
        // Change icon based on name
        String s = ingredientName.getText().toString();

        System.out.println(s);


        return rowView;
    }
}
