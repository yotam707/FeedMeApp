package com.example.yotam707.feedmeapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ExpandableListView;

public class FeedMeActivity extends AppCompatActivity {

    ExpandableListView expListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_me);

        expListView = (ExpandableListView)findViewById(R.id.feed_me_courses_list);
        //final FeedMeExpandableListViewAdapter expListViewAdapter
    }
}
