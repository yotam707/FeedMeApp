package com.example.yotam707.feedmeapp;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.yotam707.feedmeapp.DB.DatabaseHandler;
import com.example.yotam707.feedmeapp.data.DataManager;

import java.util.ArrayList;
import java.util.List;


public class IngredientFragment extends Fragment {

    ListView ingredientsList;
    Course course;
    IngredientsListAdapter adapter;
    Context thisContext;
    //DatabaseHandler db;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        thisContext = inflater.getContext();
        return inflater.inflate(R.layout.fragment_ingredient,container,false);


    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

}
