package com.example.yotam707.feedmeapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yotam707.feedmeapp.data.DataManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * Created by yotam707 on 9/5/2016.
 */
public class ListViewAdapter extends ArrayAdapter<Course> implements Filterable {
    Activity activity;
    List<Course> coursesFilterList;
    private final List<Course> coursesList;
    Course clickedCourse;
    CoursesFilter coursesFilter;
    final String TAG = ListViewAdapter.class.getSimpleName();

    @Override
    public int getCount() {
        return coursesFilterList.size();
    }

    @Override
    public Course getItem(int i) {
        return coursesFilterList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
       ViewHolder holder = null;
        LayoutInflater inflater = (LayoutInflater)activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if(convertView == null){
            convertView = inflater.inflate(R.layout.item_listview,parent,false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
        }
        Course c = coursesFilterList.get(position);

        holder.image.setImageResource(c.getImageId());
        holder.name.setText(c.getName());
        holder.add.setImageResource(R.drawable.ic_add);
        Bitmap icon = BitmapFactory.decodeResource(activity.getResources(), c.getImageId());
        holder.name.setBackgroundColor(ExpandableListAdapter.getDominantColor(icon));
        holder.add.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                clickedCourse = coursesFilterList.get(position);
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle("Add Course");
                builder.setMessage(clickedCourse.getName() + "\n" +clickedCourse.getDescription());
                builder.setCancelable(false);
                builder.setPositiveButton("Add",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                DataManager.getInstance().addCourse(clickedCourse);
                            }
                        });
                builder.setNegativeButton("Back",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
        return convertView;
    }


    public ListViewAdapter(Activity activity, int resource, List<Course> list){
    super(activity, resource);
       this.activity = activity;
        this.coursesList  = new ArrayList<Course>(list);
        this.coursesFilterList = new ArrayList<Course>(list);
       Log.i(TAG,"init adapter");
   }

    @Override
    public Filter getFilter() {
        if(coursesFilter == null)
            coursesFilter = new CoursesFilter();
        return coursesFilter;
    }

    private class ViewHolder {
        private ImageView image;
        private TextView name;
        private ImageView add;

        public ViewHolder(View v) {
            image = (ImageView) v.findViewById(R.id.lw_course_img);
            name = (TextView) v.findViewById(R.id.lw_course_name);
            add = (ImageView) v.findViewById(R.id.lw_add);
        }
    }


    private class CoursesFilter extends Filter
    {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

//below checks the match for the Category Id and adds to the filter list
            long catId= Long.parseLong(constraint.toString());
            FilterResults results = new FilterResults();

            if (catId > 0) {
                ArrayList<Course> filterList = new ArrayList<Course>();
                for (int i = 0; i < coursesList.size(); i++) {
                        Category cat = coursesList.get(i).getCategory();
                    if ( (cat.getId() )== catId) {
                        Course course = coursesList.get(i);
                        filterList.add(course);
                    }
                }

                results.count = filterList.size();
                results.values = filterList;

            } else {

                results.count = coursesList.size();
                results.values = coursesList;

            }
            return results;
        }

        //Publishes the matches found, i.e., the selected
        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            coursesFilterList = (ArrayList<Course>) results.values;
            notifyDataSetChanged();
            clear();
            notifyDataSetChanged();
            int count = coursesFilterList.size();
            for(int i=0; i< count; i++){
                add(coursesFilterList.get(i));
                notifyDataSetInvalidated();
            }
        }
    }

}
