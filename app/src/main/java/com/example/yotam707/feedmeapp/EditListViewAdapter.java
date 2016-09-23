package com.example.yotam707.feedmeapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yotam707.feedmeapp.data.DataManager;

import java.util.List;

/**
 * Created by yotam707 on 9/16/2016.
 */
public class EditListViewAdapter extends ArrayAdapter<Course> {
    List<Course> coursesAdded;
    Context activity;
    Course clickedCourse;
    final String TAG = EditListViewAdapter.class.getSimpleName();

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.edit_item_listview,parent,false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
        }
        Course c = coursesAdded.get(position);

        holder.image.setImageResource(c.getImageId());
        holder.name.setText(c.getName());
        holder.remove.setImageResource(android.R.drawable.ic_delete);
        holder.remove.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                clickedCourse = coursesAdded.get(position);
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle("Remove Course");
                builder.setMessage(clickedCourse.getName() + "\n" +clickedCourse.getDescription());
                builder.setCancelable(false);
                builder.setPositiveButton("Remove",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                DataManager.getInstance().removeCourse(clickedCourse);
                                notifyDataSetChanged();
                            }
                        });
                builder.setNegativeButton("Cancel",
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



    public EditListViewAdapter(Context context, List<Course> list) {
        super(context, R.layout.edit_item_listview,list);
        this.activity = context;
        this.coursesAdded = list;
        Log.i(TAG,"init adapter");
    }

    private class ViewHolder {
        private ImageView image;
        private TextView name;
        private ImageView remove;

        public ViewHolder(View v) {
            image = (ImageView) v.findViewById(R.id.lw_course_img_edit);
            name = (TextView) v.findViewById(R.id.lw_course_name_edit);
            remove = (ImageView) v.findViewById(R.id.lw_delete);
        }
    }
}
