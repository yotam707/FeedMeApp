package com.example.yotam707.feedmeapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v7.graphics.*;

import com.example.yotam707.feedmeapp.data.DataManager;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static java.security.AccessController.getContext;


/**
 * Created by yotam707 on 9/3/2016.
 */
public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Activity context;
    private Map<CourseType, List<Course>> coursesCollection;
    private List<CourseType> groupList;
    Course clickedCourse;
    public ExpandableListAdapter(Activity context,List<CourseType> groupList,
                                 Map<CourseType, List<Course>> coursesCollection) {
        this.context = context;
        this.groupList = groupList;
        this.coursesCollection = coursesCollection;
    }
    @Override
    public int getGroupCount() {
        return coursesCollection.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return coursesCollection.get(groupList.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groupList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
       return coursesCollection.get(groupList.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }


    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        CourseType courseName = (CourseType) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.group_item,
                    null);
        }
        TextView item = (TextView) convertView.findViewById(R.id.courses);
        item.setTypeface(null, Typeface.BOLD);
        item.setText(courseName.toString());
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        LayoutInflater inflater = context.getLayoutInflater();
        Course c = (Course) getChild(groupPosition, childPosition);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_listview, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.name.setText(c.getName());
        try {
            holder.image.setImageBitmap(MediaStore.Images.Media.getBitmap(context.getContentResolver(), c.getImage()));
        }
        catch (IOException e){
            e.printStackTrace();
        }
        holder.add.setImageResource(R.drawable.ic_add);
       // Bitmap icon = BitmapFactory.decodeResource(context.getResources(), c.getImage().getGenerationId());
        try {
            Bitmap icon = MediaStore.Images.Media.getBitmap(context.getContentResolver(), c.getImage());
            holder.name.setBackgroundColor(ExpandableListAdapter.getDominantColor(icon));
        }
        catch (IOException e){
            e.printStackTrace();
        }
        holder.add.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                clickedCourse = (Course) getChild(groupPosition, childPosition);
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Add Course");
                builder.setMessage(clickedCourse.getName() + "\n" +clickedCourse.getDescription());
                builder.setCancelable(false);
                builder.setPositiveButton("Add",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                DataManager.getInstance(context).addCourse(clickedCourse);
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

    public static int getDominantColor(Bitmap bitmap) {
        Bitmap newBitmap = Bitmap.createScaledBitmap(bitmap, 1, 1, true);
        final int color = newBitmap.getPixel(0, 0);
        newBitmap.recycle();
        return color;
    }




    public class ViewHolder {
        private ImageView image;
        private TextView name;
        private ImageView add;

        public ViewHolder(View v)  {
            image = (ImageView) v.findViewById(R.id.lw_course_img);
            name = (TextView) v.findViewById(R.id.lw_course_name);
            add = (ImageView) v.findViewById(R.id.lw_add);
        }
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
