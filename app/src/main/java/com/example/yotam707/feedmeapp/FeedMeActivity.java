package com.example.yotam707.feedmeapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import com.example.yotam707.feedmeapp.data.DataManager;

import java.util.List;

public class FeedMeActivity extends AppCompatActivity {

    private ListView coursesListView;
    private FeedMeListViewAdapter coursesListViewAdapter;
    List<Course> coursesList;
    BroadcastReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_me);
        coursesList = DataManager.getInstance().getAddedCourses();
        Log.e("FeedMeActivity", "onCreate: coursesList: "+coursesList.size());
        coursesListView = (ListView)findViewById(R.id.feed_me_courses_list);
        coursesListViewAdapter = new FeedMeListViewAdapter(this,R.layout.feedme_group_item ,coursesList);
        coursesListView.setAdapter(coursesListViewAdapter);
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int courseId = intent.getIntExtra(CoursesProgressService.INTENT_COURSE_ID, -1);
                long courseProgress = intent.getLongExtra(  CoursesProgressService.INTENT_COURSE_PROGRESS_VALUE, -1);
//                coursesListView.invalidate();
                coursesListViewAdapter.notifyDataSetChanged();
            }
        };
        // TODO - Start global timer!
        CoursesProgressService.startActionCoursesProgress(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver((receiver),
                new IntentFilter(CoursesProgressService.INTENT_COURSE_PROGRESS)
        );
    }

    @Override
    protected void onStop() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
        super.onStop();
    }

}
