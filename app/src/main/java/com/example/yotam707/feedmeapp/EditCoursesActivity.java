package com.example.yotam707.feedmeapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.yotam707.feedmeapp.data.DataManager;
import com.example.yotam707.feedmeapp.data.Firestore.FirestoreManager;
import com.example.yotam707.feedmeapp.domain.Recipe;
import com.google.android.gms.tasks.OnFailureListener;

import java.util.ArrayList;
import java.util.List;

import static com.example.yotam707.feedmeapp.R.id.toolbar;

public class EditCoursesActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    ListView listView;
    EditListViewAdapter adapter;
    List<Recipe> addedCourse;
    private DrawerLayout drawer;
    Button wantMoreBtn;
    Button feedMeBtn;
    Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mContext = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_courses);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawerLayout);
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //handling navigation view item event
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        DataManager.getInstance().createNavigationMenu(navigationView);
        DataManager.getInstance().setAddedCoursesToSubMenu();
        assert navigationView != null;
        navigationView.setNavigationItemSelectedListener(this);


        listView = (ListView)findViewById(R.id.edit_list_view);
        wantMoreBtn = (Button)findViewById(R.id.back_button_edit_course_activity);
        feedMeBtn = (Button)findViewById(R.id.next_button_edit_course_activity);

        addedCourse = DataManager.getInstance().getListAddedRecipes();

        adapter = new EditListViewAdapter(this, addedCourse);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new ListView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Course c = (Course)listView.getItemAtPosition(i);
                final String selected = c.getName();
                Intent intent = new Intent(EditCoursesActivity.this, DetailActivity.class);
                intent.putExtra("imagePath", c.getImage().toString());
                startActivity(intent);
            }
        });
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

        List<Recipe> list = DataManager.getInstance().getListAddedRecipes();
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
            FirestoreManager.createCookingRequest(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(mContext, "Error Creating Cooking Request", Toast.LENGTH_LONG).show();
                }
            });
            Intent intent =  new Intent(this, CookingActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.close) {
            finish();
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        assert drawer != null;
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
