package com.example.yotam707.feedmeapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.yotam707.feedmeapp.data.DataManager;
import com.firebase.ui.auth.AuthUI;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FeedMeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ListView coursesListView;
    private FeedMeListViewAdapter coursesListViewAdapter;
    List<Course> coursesList;
    BroadcastReceiver receiver;
    private DrawerLayout drawer;
    Button finishButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_me);
        finishButton = (Button)findViewById(R.id.finish_button_feed_me_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawerLayout);
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //handling navigation view item event
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        DataManager.getInstance(getApplicationContext()).createNavigationMenu(navigationView);
        DataManager.getInstance(getApplicationContext()).setAddedCoursesToSubMenu();
        assert navigationView != null;
        navigationView.setNavigationItemSelectedListener(this);
        coursesList = DataManager.getInstance(getApplicationContext()).getListAddedCourses();
        Log.e("FeedMeActivity", "onCreate: coursesList: "+coursesList.size());
        coursesListView = (ListView)findViewById(R.id.feed_me_courses_list);
        coursesListViewAdapter = new FeedMeListViewAdapter(this,R.layout.feedme_group_item ,coursesList);
        coursesListView.setAdapter(coursesListViewAdapter);

        coursesListView.setOnItemClickListener(new ListView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Course c = (Course)coursesListView.getItemAtPosition(i);
                final String selected = c.getName();
                Intent intent = new Intent(FeedMeActivity.this, DetailActivity.class);
                intent.putExtra("imagePath", c.getImage().toString());
                startActivity(intent);
            }
        });
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int courseId = intent.getIntExtra(CoursesProgressService.INTENT_COURSE_ID, -1);
                int courseProgress = intent.getIntExtra(CoursesProgressService.INTENT_COURSE_PROGRESS_VALUE, -1);
                Log.e("onReceive", "course id:" + courseId+ ", courseProgress:" +courseProgress);
                if(courseId == 0 && courseProgress == 0){
                    finishButton.setEnabled(true);
                    finishButton.setOnClickListener(new View.OnClickListener(){

                        @Override
                        public void onClick(View view) {
                            clearAllCourses();
                        }
                    });
                }
                coursesListViewAdapter.notifyDataSetChanged();

            }
        };


        CoursesProgressService.startActionCoursesProgress(this);
    }
    public void clearAllCourses(){
        DataManager.getInstance(getApplicationContext()).clearAddedCourse();
        Intent intent1 =  new Intent(FeedMeActivity.this, MainCourseActivity.class);
        startActivity(intent1);
    }

    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver((receiver),
                new IntentFilter(CoursesProgressService.INTENT_COURSE_PROGRESS)
        );
    }

    @Override
    protected void onStop() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
        super.onStop();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.close) {
            AuthUI.getInstance().signOut(this);
            finish();
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void onBackPressed() {
        assert drawer != null;
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
