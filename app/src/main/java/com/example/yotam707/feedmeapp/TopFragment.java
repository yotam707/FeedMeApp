package com.example.yotam707.feedmeapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Gallery;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.yotam707.feedmeapp.DB.DatabaseHandler;
import com.example.yotam707.feedmeapp.Utils.StringUtils;
import com.example.yotam707.feedmeapp.data.DataManager;
import com.example.yotam707.feedmeapp.data.Firestore.FirestoreManager;
import com.example.yotam707.feedmeapp.data.Firestore.FirestoreStorageManager;
import com.example.yotam707.feedmeapp.domain.Recipe;
import com.example.yotam707.feedmeapp.ui.ui.adapters.ImageScrollRecyclerViewAdapter;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yotam707 on 9/10/2016.
 */
public class TopFragment extends Fragment {

    Context thisContext;
    private final String TAG = this.getClass().getSimpleName();
    ImageScrollRecyclerViewAdapter mAdapterMain;
    ImageScrollRecyclerViewAdapter mAdapterSide;
    ImageScrollRecyclerViewAdapter mAdapterAppetizer;
    ImageScrollRecyclerViewAdapter mAdapterDessert;
    LinearLayoutManager layoutManagerMain;
    LinearLayoutManager layoutManagerSide;
    LinearLayoutManager layoutManagerDessert;
    LinearLayoutManager layoutManagerAppetizer;
    LinearLayout coursesDataLayout;
    ProgressBar mainProgressBar;
    boolean mainCourseFinish = false;
    boolean sideDishFinish = false;
    boolean appetizerFinish = false;
    boolean dessertFinish = false;
    RecyclerView topMainImageRecyclerView;
    RecyclerView topSideImageRecyclerView;
    RecyclerView topAppetizerImageRecyclerView;
    RecyclerView topDessertImageRecyclerView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        thisContext = inflater.getContext();
        View v =  inflater.inflate(R.layout.top_fragment_layout,container,false);
        initTopFragmentRecyclerView(v);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        createCollection();
    }

    private void createCollection(){
        mainProgressBar.setVisibility(View.VISIBLE);
        FirestoreManager.getAllAppetizerRecipes(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    List<Recipe> appetizerCourses = new ArrayList<>();
                    for(DocumentSnapshot document: task.getResult()){
                        appetizerCourses.add(document.toObject(Recipe.class));
                        Log.d(TAG, document.getId() + " => " + document.getData());
                    }
                    mAdapterAppetizer = new ImageScrollRecyclerViewAdapter(appetizerCourses.subList(0,5),CourseType.APPETIZER,thisContext);
                    topAppetizerImageRecyclerView.setAdapter(mAdapterAppetizer);
                    appetizerFinish = true;
                    checkProgress();
                }
            }
        }, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Error getting documents: "+ e.getMessage());
            }
        });

        FirestoreManager.getAllMainCourseRecipes(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    List<Recipe> mainCourses = new ArrayList<>();
                    for(DocumentSnapshot document: task.getResult()){
                        mainCourses.add(document.toObject(Recipe.class));
                        Log.d(TAG, document.getId() + " => " + document.getData());
                    }
                    mAdapterMain = new ImageScrollRecyclerViewAdapter(mainCourses.subList(0,5),CourseType.MAIN_COURSE,thisContext);
                    topMainImageRecyclerView.setAdapter(mAdapterMain);
                    mainCourseFinish = true;
                    checkProgress();
                }
            }
        }, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Error getting documents: "+ e.getMessage());
            }
        });

        FirestoreManager.getAllDessertRecipes(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    List<Recipe> desertCourses = new ArrayList<>();
                    for(DocumentSnapshot document: task.getResult()){
                        desertCourses.add(document.toObject(Recipe.class));
                        Log.d(TAG, document.getId() + " => " + document.getData());
                    }
                    mAdapterDessert = new ImageScrollRecyclerViewAdapter(desertCourses.subList(0,5),CourseType.DESSERT, thisContext);
                    topDessertImageRecyclerView.setAdapter(mAdapterDessert);
                    dessertFinish = true;
                    checkProgress();
                }
            }
        }, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Error getting documents: "+ e.getMessage());
            }
        });

        FirestoreManager.getAllSideDishRecipes(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    List<Recipe> sideDishCourses = new ArrayList<>();
                    for(DocumentSnapshot document: task.getResult()){
                        sideDishCourses.add(document.toObject(Recipe.class));
                        Log.d(TAG, document.getId() + " => " + document.getData());
                    }
                    mAdapterSide = new ImageScrollRecyclerViewAdapter(sideDishCourses.subList(0,5),CourseType.SIDE_DISH, thisContext);
                    topSideImageRecyclerView.setAdapter(mAdapterSide);
                    sideDishFinish = true;
                    checkProgress();
                }
            }
        }, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Error getting documents: "+ e.getMessage());
            }
        });

        checkProgress();
    }
    public void initTopFragmentRecyclerView(View v){
        layoutManagerMain = new LinearLayoutManager(thisContext,LinearLayoutManager.HORIZONTAL,false);
        layoutManagerSide = new LinearLayoutManager(thisContext,LinearLayoutManager.HORIZONTAL,false);
        layoutManagerDessert = new LinearLayoutManager(thisContext,LinearLayoutManager.HORIZONTAL,false);
        layoutManagerAppetizer = new LinearLayoutManager(thisContext,LinearLayoutManager.HORIZONTAL,false);
        coursesDataLayout = v.findViewById(R.id.courses_data_layout);
        topMainImageRecyclerView = v.findViewById(R.id.top_fragment_rv_main);
        topSideImageRecyclerView = v.findViewById(R.id.top_fragment_rv_side);
        topAppetizerImageRecyclerView = v.findViewById(R.id.top_fragment_rv_appetizer);
        topDessertImageRecyclerView = v.findViewById(R.id.top_fragment_rv_dessert);

        mainProgressBar = v.findViewById(R.id.main_progress);

        topMainImageRecyclerView.setLayoutManager(layoutManagerMain);
        topSideImageRecyclerView.setLayoutManager(layoutManagerSide);
        topAppetizerImageRecyclerView.setLayoutManager(layoutManagerAppetizer);
        topDessertImageRecyclerView.setLayoutManager(layoutManagerDessert);
    }

    public void checkProgress(){
        if(mainCourseFinish && sideDishFinish && appetizerFinish && dessertFinish){
            mainProgressBar.setVisibility(View.GONE);
            coursesDataLayout.setVisibility(View.VISIBLE);
        }
    }
}
