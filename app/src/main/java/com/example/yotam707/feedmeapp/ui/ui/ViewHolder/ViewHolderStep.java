package com.example.yotam707.feedmeapp.ui.ui.ViewHolder;

import android.view.View;
import android.widget.TextView;

import com.example.yotam707.feedmeapp.R;
import com.example.yotam707.feedmeapp.domain.Step;
import com.example.yotam707.feedmeapp.ui.ui.interfaces.DetailedListItem;

public class ViewHolderStep extends ViewHolder {
    private TextView step;
    private TextView time;
    public ViewHolderStep(View view) {
        super(view);
        step = view.findViewById(R.id.step_desc_item);
        time = view.findViewById(R.id.step_time_item);
    }

    @Override
    public void bindType(DetailedListItem item) {
        step.setText(((Step)item).getStep());
        time.setText(((Step)item).getLength().getNumber());
    }
}
