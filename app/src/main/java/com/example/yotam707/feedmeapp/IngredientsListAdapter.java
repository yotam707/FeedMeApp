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
    //private final List<Ingredient> values;
    Course clickedCourse;

    public IngredientsListAdapter(Context context, Course c) {
        super(context, R.layout.ingredient_list_item);
        this.clickedCourse = c;
        this.context = context;
    }
    @Override
    public int getCount() {
        return  clickedCourse.getIngredientList().size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.ingredient_list_item,parent,false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder)convertView.getTag();
        }

        Ingredient ing = clickedCourse.getIngredientList().get(position);
        System.out.println(ing.getName());

        holder.name.setText(ing.getName());
        holder.quantity.setText(String.valueOf(ing.getQuantity()));
        return convertView;


    }

    public class ViewHolder {
        private TextView name;
        private TextView quantity;

        public ViewHolder(View v)  {
            name = (TextView) v.findViewById(R.id.ingredient_frag_item);
            quantity = (TextView) v.findViewById(R.id.quantity);
        }
    }
}
