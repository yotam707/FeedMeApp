package com.example.yotam707.feedmeapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.example.yotam707.feedmeapp.data.DataManager;

import java.util.List;

public class EditCoursesActivity extends Activity {

    ListView listView;
    EditListViewAdapter adapter;
    List<Course> addedCourse;
    Button wantMoreBtn;
    Button feedMeBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_courses);
        listView = (ListView)findViewById(R.id.edit_list_view);
        wantMoreBtn = (Button)findViewById(R.id.back_button_edit_course_activity);
        feedMeBtn = (Button)findViewById(R.id.next_button_edit_course_activity);
        addedCourse = DataManager.getInstance().getListAddedCourses();
        adapter = new EditListViewAdapter(this, addedCourse);
        listView.setAdapter(adapter);

        wantMoreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wantMoreClick();
            }
        });

        feedMeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                feedMeClick();
            }
        });
    }

    private void wantMoreClick(){
        super.onBackPressed();
    }

    private void feedMeClick(){
        List<Course> list = DataManager.getInstance().getListAddedCourses();
        final Activity thisContext = this;
        if(list.size() <= 0 || list== null){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Notification");
            builder.setMessage("No Courses was Chosen,\nPlease Add a course to continue");
            builder.setCancelable(false);
            builder.setNegativeButton("OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            onBackPressed();
                        }
                    });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
        else{
            Intent intent =  new Intent(this, FeedMeActivity.class);
            startActivity(intent);
        }
    }
}
