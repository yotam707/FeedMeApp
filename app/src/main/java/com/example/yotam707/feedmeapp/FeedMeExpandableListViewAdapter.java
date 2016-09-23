package com.example.yotam707.feedmeapp;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

/**
 * Created by yotam707 on 9/17/2016.
 */
public class FeedMeExpandableListViewAdapter extends BaseExpandableListAdapter {

    private Activity context;
    private List<Course> addedCoursesCollection;
    private List<String> groupList;
    Course clickedCourse;
    GroupViewHolder holder;

    public FeedMeExpandableListViewAdapter(Activity context, List<String> groupList,List<Course> addedCoursesCollection){
        this.context = context;
        this.groupList = groupList;
        this.addedCoursesCollection = addedCoursesCollection;

    }


    @Override
    public int getGroupCount() {
        return addedCoursesCollection.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return addedCoursesCollection.get(i).getStepsList().size();
    }

    @Override
    public Object getGroup(int i) {
        return addedCoursesCollection.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return addedCoursesCollection.get(i).getStepsList().get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return addedCoursesCollection.get(i).getId();
    }

    @Override
    public long getChildId(int i, int i1) {
        return addedCoursesCollection.get(i).getStepsList().get(i1).getStepNum();
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        holder = null;
        Course course = (Course)getGroup(i);
        if(view == null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.feedme_group_item, null);
            holder = new GroupViewHolder(view);
            view.setTag(holder);
        }
        else{
            holder = (GroupViewHolder)view.getTag();
        }

        holder.name.setText(course.getName());
        holder.name.setTypeface(null, Typeface.BOLD);
        holder.image.setImageResource(course.getImageId());
        //holder.progressBar.setProgress(course.);




        return view;

    }

    private class GroupViewHolder {
        private ImageView image;
        private TextView name;
        private ImageView compleate;
        private ProgressBar progressBar;
        private Steps step;

        public GroupViewHolder(View v) {
            image = (ImageView) v.findViewById(R.id.feed_me_courses_image);
            name = (TextView) v.findViewById(R.id.feed_me_courses);
            compleate = (ImageView) v.findViewById(R.id.main_progress_bar_image);
            progressBar = (ProgressBar)v.findViewById(R.id.main_progress_bar);
        }
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        return null;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }
}
