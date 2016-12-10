package com.example.yotam707.feedmeapp;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;
import java.util.ArrayList;

/**
 * Created by yotam707 on 9/10/2016.
 */
public class CategoryAdapter extends ArrayAdapter<Category> {
     Context context;
     List<Category> catList;
    public CategoryAdapter(Context context, int resource, List<Category> catList) {
        super(context, resource, catList);
        this.context = context;
        this.catList = catList;
    }

    public int getCount(){
        return catList.size();
    }

    public Category getItem(int pos){
        return catList.get(pos);
    }
    public long getItemId(int i){
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView view = new TextView(context);
        view.setTextColor(Color.BLACK);
        view.setGravity(Gravity.CENTER);
        view.setText(catList.get(position).getName());
        view.setTextSize(25);

        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
            TextView view = new TextView(context);
            view.setTextColor(Color.BLACK);
            view.setText(catList.get(position).getName());
            view.setHeight(100);
            view.setTextSize(25);

            return view;
    }
}

