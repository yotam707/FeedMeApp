 package com.example.yotam707.feedmeapp;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.yotam707.feedmeapp.data.DataManager;
import com.example.yotam707.feedmeapp.data.Firestore.FirestoreManager;
import com.example.yotam707.feedmeapp.domain.FullRecipe;
import com.example.yotam707.feedmeapp.domain.FullRecipeList;
import com.example.yotam707.feedmeapp.domain.Recipe;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

 public class CookingActivity extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener{

     private DrawerLayout drawer;
     private List<FullRecipe> recipeList;
     MaterialDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cooking);
        drawer = (DrawerLayout) findViewById(R.id.drawerLayout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        DataManager.getInstance().createNavigationMenu(navigationView);
        DataManager.getInstance().setAddedCoursesToSubMenu();
        assert navigationView != null;
        navigationView.setNavigationItemSelectedListener(this);
        MaterialDialog.Builder builder = new MaterialDialog.Builder(this)
                .title(R.string.cooking_progress_dialog)
                .content(R.string.cooking_progress_content_dialog)
                .progress(true, 0).autoDismiss(false);

        dialog = builder.build();
        dialog.show();

        FirestoreManager.getCalculatedCookingData(new EventListener<DocumentSnapshot>() {
            public static final String TAG = "";

            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                String source = documentSnapshot != null && documentSnapshot.getMetadata().hasPendingWrites()
                        ? "Local" : "Server";

                if (documentSnapshot != null && documentSnapshot.exists()) {
                    dialog.dismiss();
                    FullRecipeList recipeList = documentSnapshot.toObject(FullRecipeList.class);
                    FirestoreManager.removeAllSelectedCourses(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "remove all selected courses failed.", e);
                        }
                    });
                } else {
                    Log.d(TAG, source + " data: null");
                }
            }
        });






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
