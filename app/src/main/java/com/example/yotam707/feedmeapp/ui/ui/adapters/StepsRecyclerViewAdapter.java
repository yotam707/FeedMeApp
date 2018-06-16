package com.example.yotam707.feedmeapp.ui.ui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.yotam707.feedmeapp.R;
import com.example.yotam707.feedmeapp.domain.Step;

import java.util.List;

public class StepsRecyclerViewAdapter extends RecyclerView.Adapter<StepsRecyclerViewAdapter.ViewHolder> {

    private List<Step> stepList;
    private Context mContext;
    private int totalStepsTime;

    public StepsRecyclerViewAdapter(List<Step> stepList,int totalStepsTime, Context mContext) {
        this.stepList = stepList;
        this.mContext = mContext;
        this.totalStepsTime = totalStepsTime;
    }


    public int getTotalStepsTime(){
        if(this.totalStepsTime > 0)
            return this.totalStepsTime;
        return stepList.size();
    }
    @NonNull
    @Override
    public StepsRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.step_list_item, parent, false);
        return new StepsRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StepsRecyclerViewAdapter.ViewHolder holder, int position) {
        Step step = stepList.get(position);
        holder.description.setText(step.getStep());
        if(step.getLength() != null){
            if(step.getLength().getNumber() > 0){
                holder.length.setText(Integer.toString(step.getLength().getNumber()));
            }
            else{
                holder.length.setText(Integer.toString(getTotalStepsTime()/stepList.size()));
            }
        }
        else{
            holder.length.setText(Integer.toString(getTotalStepsTime()/stepList.size()));
        }
    }

    @Override
    public int getItemCount() {
        return stepList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView description;
        TextView length;

        public ViewHolder(View itemView) {
            super(itemView);
            description = itemView.findViewById(R.id.step_desc_item);
            length = itemView.findViewById(R.id.step_time_item);
        }
    }
}
