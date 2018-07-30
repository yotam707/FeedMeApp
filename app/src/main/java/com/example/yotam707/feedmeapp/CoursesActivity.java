package com.example.yotam707.feedmeapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.Toast;

import com.example.yotam707.feedmeapp.data.DataManager;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CoursesActivity extends AppCompatActivity {

    List<String> groupList;
    ExpandableListView expListView;
    ListView listView;
    CourseListViewAdapter adapter;
    final String TAG = CoursesActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses);



        TabHost host = (TabHost)findViewById(R.id.tabHost);
        host.setup();

        //Tab 1
        TabHost.TabSpec spec = host.newTabSpec("TOP");
        spec.setContent(R.id.tab_1);
        spec.setIndicator("Top");
        host.addTab(spec);

        //Tab 2
        spec = host.newTabSpec("Categories");
        spec.setContent(R.id.tab_2);
        spec.setIndicator("Categories");
        host.addTab(spec);

        expListView = (ExpandableListView) findViewById(R.id.courses_list);
        listView = (ListView)findViewById(R.id.courses_list_view);
        setListViewAdapter();

    }



    private void setListViewAdapter(){
        adapter = new CourseListViewAdapter(this,R.layout.item_listview, DataManager.getInstance().getCourses());
        listView.setAdapter(adapter);
    }


}
