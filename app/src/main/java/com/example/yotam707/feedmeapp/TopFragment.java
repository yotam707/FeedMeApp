package com.example.yotam707.feedmeapp;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.example.yotam707.feedmeapp.data.DataManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yotam707 on 9/10/2016.
 */
public class TopFragment extends Fragment {
    ExpandableListView expListView;
    Context thisContext;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        thisContext = inflater.getContext();
        return inflater.inflate(R.layout.top_fragment_layout,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        expListView = (ExpandableListView) view.findViewById(R.id.courses_list);
        Activity host = (Activity)thisContext;
        final ExpandableListAdapter expListAdapter = new ExpandableListAdapter(host , DataManager.getInstance().getGroupList(), DataManager.getInstance().getCoursesCollection());
        expListView.setAdapter(expListAdapter);

        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                Course co = (Course) expListAdapter.getChild(
                        groupPosition, childPosition);
                final String selected = co.getName();
                Toast.makeText(thisContext, selected, Toast.LENGTH_LONG)
                        .show();

                return true;
            }
        });
    }
}
