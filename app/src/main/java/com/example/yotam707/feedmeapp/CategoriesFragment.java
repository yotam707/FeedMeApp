package com.example.yotam707.feedmeapp;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Filter;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.yotam707.feedmeapp.data.DataManager;

import java.util.ArrayList;

/**
 * Created by yotam707 on 9/10/2016.
 */
public class CategoriesFragment extends Fragment{
    ListView listView;
    ListViewAdapter adapter;
    Context thisContext;

    CategoryAdapter categoryAdapter;
    Spinner categorySpinner;


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Activity host = (Activity)thisContext;
        listView = (ListView)view.findViewById(R.id.courses_list_view);
        categorySpinner = (Spinner)view.findViewById(R.id.cat_spinner);
        categoryAdapter = new CategoryAdapter(thisContext,android.R.layout.simple_spinner_dropdown_item, DataManager.getInstance().getCategoriesList());
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoryAdapter);
        setListViewAdapter(host);

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Category cat  = categoryAdapter.getItem(i);
                adapter.getFilter().filter(Long.toString(cat.getId()), new Filter.FilterListener() {
                    @Override
                    public void onFilterComplete(int i) {
                       listView.setAdapter(adapter);

                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        thisContext = inflater.getContext();
        return inflater.inflate(R.layout.categories_fragment_layout,container,false);
    }




    private void setListViewAdapter(Activity host){
        adapter = new ListViewAdapter(host,R.layout.item_listview, DataManager.getInstance().getCourses());
        listView.setAdapter(adapter);
    }
}
