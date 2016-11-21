package com.example.yotam707.feedmeapp;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.yotam707.feedmeapp.data.DataManager;

import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by yotam707 on 11/11/2016.
 */

public class FeedMeListViewAdapter extends ArrayAdapter<Course> {
    private static final String TAG = "FeedMeListViewAdapter";
    Context ctx;
    List<Course> courseList;
    private static final int MAX = 100;

    public List<Course> getCourseList() {
        return courseList;
    }

    public Context getCtx() {
        return ctx;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.e(TAG, "getView");
        ViewHolder holder;
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

        Log.e("getViewData","progress:" + c.courseProgress+ "name:" + c.getName());
        holder.image.setImageURI(c.getImage());
        //holder.image.setImageResource(c.getImage());
        holder.name.setText(c.getName());
        holder.pBar.setIndeterminate(false);
        holder.pBar.setMax(MAX*c.getStepsList().size());
        holder.pBar.setProgress(c.courseProgress);
        return convertView;
    }

    public FeedMeListViewAdapter(Context context, int resource, List<Course> courses) {
        super(context, resource);
        this.ctx = context;
        this.courseList = courses;
        Log.i(TAG,"init feedmelistview adapter");

    }

    public void setCourseProgress(int courseId, int progress) {
        //notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return this.courseList.size();
    }

    @Nullable
    @Override
    public Course getItem(int position) {
        return this.courseList.get(position);
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
}
