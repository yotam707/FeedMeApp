package com.example.yotam707.feedmeapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by Adir on 10/12/2016.
 */

public class StepsListAdapter extends ArrayAdapter<Steps> {

    private final Context context;
    Course clickedCourse;

    public StepsListAdapter(Context context, Course c) {
        super(context, R.layout.step_list_item);
        this.clickedCourse = c;
        this.context = context;
    }
    @Override
    public int getCount() {
        return  clickedCourse.getStepsList().size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        StepsListAdapter.ViewHolder holder;
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.step_list_item,parent,false);
            holder = new StepsListAdapter.ViewHolder(convertView);
            convertView.setTag(holder);
        }
        else{
            holder = (StepsListAdapter.ViewHolder)convertView.getTag();
        }

        Steps step = clickedCourse.getStepsList().get(position);
        System.out.println(step.getDescription().toString());

        holder.stepNumber.setText(String.valueOf(step.getStepNum()));
        holder.description.setText(step.getDescription());
        holder.timeInSeconds.setText(String.valueOf(step.getTimeImSeconds()));
        return convertView;


    }

    public class ViewHolder {
        private TextView stepNumber;
        private TextView description;
        private TextView timeInSeconds;

        public ViewHolder(View v)  {
            stepNumber = (TextView) v.findViewById(R.id.step_num_item);
            description = (TextView) v.findViewById(R.id.step_desc_item);
            timeInSeconds = (TextView) v.findViewById(R.id.step_time_item);
        }
    }
}
