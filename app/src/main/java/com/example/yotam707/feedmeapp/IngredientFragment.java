package com.example.yotam707.feedmeapp;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.yotam707.feedmeapp.DB.DatabaseHandler;

import java.util.ArrayList;
import java.util.List;


public class IngredientFragment extends Fragment {

    ListView ingredientsList;
    Course course;
    DatabaseHandler db;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_ingredient, container,
                false);
        db = new DatabaseHandler(getContext());
        ingredientsList = (ListView) rootView.findViewById(R.id.ingredientsList);
        DetailActivity detailActivity = (DetailActivity) getActivity();
        course = detailActivity.getSelectedCourse();
        ImageView bullet = (ImageView) rootView.findViewById(R.id.imageView2);
        TextView ingredientName = (TextView) rootView.findViewById(R.id.ingredient);
        TextView ingredientQuantity = (TextView) rootView.findViewById(R.id.quantity);
        List<Ingredient> ing = db.getAllCourses().get(0).getIngredientList();
        IngredientsListAdapter adapter = new IngredientsListAdapter(getContext(),ing);
        ingredientsList.setAdapter(adapter);

        return rootView;

    }
    public IngredientFragment(){
    }
}
