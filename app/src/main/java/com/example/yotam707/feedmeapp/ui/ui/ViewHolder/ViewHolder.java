package com.example.yotam707.feedmeapp.ui.ui.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.yotam707.feedmeapp.ui.ui.interfaces.DetailedListItem;

public abstract class ViewHolder extends RecyclerView.ViewHolder{
    public ViewHolder(View itemView) {
        super(itemView);
    }

    public abstract void bindType(DetailedListItem item);
}
