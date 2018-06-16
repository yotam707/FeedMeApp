package com.example.yotam707.feedmeapp.ui.ui.ListItems;

import com.example.yotam707.feedmeapp.ui.ui.interfaces.DetailedListItem;

public class StepItem implements DetailedListItem {
    private String description;
    private String time;

    public StepItem(String description, String time){
        this.description = description;
        this.time = time;
    }
    @Override
    public int getListItemType() {
        return DetailedListItem.TYPE_STEP;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
