package com.example.yotam707.feedmeapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.graphics.Palette;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.yotam707.feedmeapp.data.DataManager;
import com.example.yotam707.feedmeapp.data.Firestore.FirestoreManager;
import com.example.yotam707.feedmeapp.data.Firestore.FirestoreStorageManager;
import com.example.yotam707.feedmeapp.domain.Recipe;
import com.google.android.gms.tasks.OnFailureListener;

import java.io.IOException;
import java.util.List;

/**
 * Created by yotam707 on 9/16/2016.
 */
public class EditListViewAdapter extends ArrayAdapter<Recipe> {
    List<Recipe> coursesAdded;
    Context activity;
    Recipe clickedCourse;
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
        Recipe r = coursesAdded.get(position);

       // try {
            Glide
                    .with(getContext())
                    .asBitmap()
                    .load(FirestoreStorageManager.storageReference.child(r.getImage()))
                    .into(holder.image);

        holder.name.setText(r.getTitle());
        holder.remove.setImageResource(android.R.drawable.ic_delete);
        holder.remove.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                clickedCourse = coursesAdded.get(position);
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle("Remove Course");
                builder.setMessage(clickedCourse.getTitle() + "\n" +clickedCourse.getType());
                builder.setCancelable(false);
                builder.setPositiveButton("Remove",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                DataManager.getInstance().removeRecipe(clickedCourse);
                                FirestoreManager.removeSelectedCourse(clickedCourse, new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(activity, "Error Removing Recipe", Toast.LENGTH_LONG).show();
                                    }
                                });
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



    public EditListViewAdapter(Context context, List<Recipe> list) {
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
            image = v.findViewById(R.id.lw_course_img_edit);
            name = v.findViewById(R.id.lw_course_name_edit);
            remove = v.findViewById(R.id.lw_delete);
        }
    }
}
