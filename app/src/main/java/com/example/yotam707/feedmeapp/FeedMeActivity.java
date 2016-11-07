package com.example.yotam707.feedmeapp;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.yotam707.feedmeapp.data.DataManager;

import java.util.ArrayList;
import java.util.List;

public class FeedMeActivity extends AppCompatActivity {

    private ListView coursesListView;
    private MyListViewAdapter coursesListViewAdapter;
    List<Course> coursesList;
    BroadcastReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_me);
        coursesList = DataManager.getInstance().getAddedCourses();
        coursesListView = (ListView)findViewById(R.id.feed_me_courses_list);
        coursesListViewAdapter = new MyListViewAdapter(this, coursesList);
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int courseId = intent.getIntExtra(CoursesProgressService.INTENT_COURSE_ID, -1);
                int courseProgress = intent.getIntExtra(CoursesProgressService.INTENT_COURSE_PROGRESS_VALUE, -1);

                coursesListViewAdapter.setCourseProgress(courseId, courseProgress);
                coursesListView.invalidate();
            }
        };
        // TODO - Start timer!
        CoursesProgressService.startActionCoursesProgress(this, new ArrayList<Course>(coursesList));
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

    public static class MyListViewAdapter extends ArrayAdapter<Course> {

        Context ctx;
        List<Course> courseList;
        public MyListViewAdapter(Context ctx, List<Course> courses) {
            super(ctx,R.layout.item_listview, courses);
            this.ctx = ctx;
            this.courseList = courses;
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            MyListViewAdapter.ViewHolder holder = null;
            LayoutInflater inflater = (LayoutInflater)ctx.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            if(convertView == null){
                convertView = inflater.inflate(R.layout.feedme_group_item,parent,false);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            }
            else{
                holder = (ViewHolder)convertView.getTag();
            }
            Course c = courseList.get(position);
            holder.image.setImageResource(c.getImageId());
            holder.name.setText(c.getName());
            holder.pBar.setIndeterminate(false);
            holder.pBar.setMax(100);
            holder.pBar.setProgress(0);

            return convertView;
        }

        private class ViewHolder {
            private ImageView image;
            private TextView name;
            private ProgressBar pBar;

            public ViewHolder(View v) {
                image = (ImageView) v.findViewById(R.id.feed_me_courses_image);
                name = (TextView) v.findViewById(R.id.feed_me_courses);
                pBar = (ProgressBar) v.findViewById(R.id.main_progress_bar);

            }
        }
        public void setCourseProgress(int courseId, int progress) {

        }
    }
}
