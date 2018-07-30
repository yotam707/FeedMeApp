package com.example.yotam707.feedmeapp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.example.yotam707.feedmeapp.Utils.GridAutoFitLayoutManager;
import com.example.yotam707.feedmeapp.data.DataManager;
import com.example.yotam707.feedmeapp.data.Firestore.FirestoreManager;
import com.example.yotam707.feedmeapp.domain.CategoryTypeEnum;
import com.example.yotam707.feedmeapp.domain.Recipe;
import com.example.yotam707.feedmeapp.ui.ui.adapters.GridTilesRecyclerViewAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by yotam707 on 9/10/2016.
 */
public class CategoriesFragment extends Fragment{

    Context thisContext;
    CategoryAdapter categoryAdapter;
    Spinner categorySpinner;
    private final String TAG = this.getClass().getSimpleName();
    GridTilesRecyclerViewAdapter gridAdapter;
    GridLayoutManager layoutManager;
    RecyclerView gridRecyclerView;
    HashMap<String, List<Recipe>> recipeMap;
    List<Recipe> allCategoriesRecipes;
    ProgressBar mainProgressBar;
    Category currentCategory;
    List<Recipe> currentRecipes;
    boolean finishAfrican;
    boolean finishAmerican;
    boolean finishChinese;
    boolean finishGreek;
    boolean finishIndian;
    boolean finishItalian;
    boolean finishMexican;
    boolean finishMiddleEastern;
    boolean finishThai;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        categoryAdapter = new CategoryAdapter(thisContext,android.R.layout.simple_spinner_dropdown_item, DataManager.getInstance().getCategoriesList());
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoryAdapter);
        getRecipesCategories();
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Category cat  = categoryAdapter.getItem(i);
                if(allCategoriesRecipes != null && allCategoriesRecipes.size() > 0 && recipeMap != null && recipeMap.size() > 0) {
                    if (!cat.equals(currentCategory)) {
                        currentRecipes.clear();
                        if (cat.getName() == CategoryTypeEnum.All.name()) {
                            currentRecipes = allCategoriesRecipes.subList(0,30);
                        } else {
                            currentRecipes = recipeMap.get(cat.getName());
                        }
                        gridAdapter.SetRecipeList(currentRecipes);
                        gridAdapter.notifyDataSetChanged();
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        thisContext = inflater.getContext();
        View v = inflater.inflate(R.layout.categories_fragment_layout,container,false);
        init(v);
        return v;
    }

    public void init(View view){
        mainProgressBar = view.findViewById(R.id.main_progress);
        categorySpinner = view.findViewById(R.id.cat_spinner);
        gridRecyclerView = view.findViewById(R.id.categories_rv);
        layoutManager = new GridLayoutManager(thisContext, 2);
        gridRecyclerView.setHasFixedSize(true);
        gridRecyclerView.setNestedScrollingEnabled(true);
        gridRecyclerView.setLayoutManager(layoutManager);

    }

    public void getRecipesCategories(){
        recipeMap = new HashMap<>();
        allCategoriesRecipes = new ArrayList<>();
        currentRecipes = new ArrayList<>();
        FirestoreManager.getAfricanCategory(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    List<Recipe> africanCourses = new ArrayList<>();
                    for(DocumentSnapshot document: task.getResult()){
                        africanCourses.add(document.toObject(Recipe.class));
                        Log.d(TAG, document.getId() + " => " + document.getData());
                    }
                    recipeMap.put(CategoryTypeEnum.African.name().toString(), africanCourses);
                    allCategoriesRecipes.addAll(africanCourses);
                    finishAfrican = true;
                    checkProgress();
                }
            }
        }, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Error getting documents: "+ e.getMessage());
            }
        });


        FirestoreManager.getAmericanCategory(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    List<Recipe> americanCourses = new ArrayList<>();
                    for(DocumentSnapshot document: task.getResult()){
                        americanCourses.add(document.toObject(Recipe.class));
                        Log.d(TAG, document.getId() + " => " + document.getData());
                    }
                    recipeMap.put(CategoryTypeEnum.American.name().toString(), americanCourses);
                    allCategoriesRecipes.addAll(americanCourses);
                    finishAmerican = true;
                    checkProgress();
                }
            }
        }, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Error getting documents: "+ e.getMessage());
            }
        });

        FirestoreManager.getChineseategory(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    List<Recipe> chineseCourses = new ArrayList<>();
                    for(DocumentSnapshot document: task.getResult()){
                        chineseCourses.add(document.toObject(Recipe.class));
                        Log.d(TAG, document.getId() + " => " + document.getData());
                    }
                    recipeMap.put(CategoryTypeEnum.Chinese.name().toString(), chineseCourses);
                    allCategoriesRecipes.addAll(chineseCourses);
                    finishChinese = true;
                    checkProgress();
                }
            }
        }, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Error getting documents: "+ e.getMessage());
            }
        });

        FirestoreManager.getGreekCategory(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    List<Recipe> greekCourses = new ArrayList<>();
                    for(DocumentSnapshot document: task.getResult()){
                        greekCourses.add(document.toObject(Recipe.class));
                        Log.d(TAG, document.getId() + " => " + document.getData());
                    }
                    recipeMap.put(CategoryTypeEnum.Greek.name().toString(), greekCourses);
                    allCategoriesRecipes.addAll(greekCourses);
                    finishGreek = true;
                    checkProgress();
                }
            }
        }, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Error getting documents: "+ e.getMessage());
            }
        });

        FirestoreManager.getIndianCategory(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    List<Recipe> indianCourses = new ArrayList<>();
                    for(DocumentSnapshot document: task.getResult()){
                        indianCourses.add(document.toObject(Recipe.class));
                        Log.d(TAG, document.getId() + " => " + document.getData());
                    }
                    recipeMap.put(CategoryTypeEnum.Indian.name().toString(), indianCourses);
                    allCategoriesRecipes.addAll(indianCourses);
                    finishIndian = true;
                    checkProgress();
                }
            }
        }, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Error getting documents: "+ e.getMessage());
            }
        });

        FirestoreManager.getItalianCategory(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    List<Recipe> italianCourses = new ArrayList<>();
                    for(DocumentSnapshot document: task.getResult()){
                        italianCourses.add(document.toObject(Recipe.class));
                        Log.d(TAG, document.getId() + " => " + document.getData());
                    }
                    recipeMap.put(CategoryTypeEnum.Italian.name().toString(), italianCourses);
                    allCategoriesRecipes.addAll(italianCourses);
                    finishItalian = true;
                    checkProgress();
                }
            }
        }, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Error getting documents: "+ e.getMessage());
            }
        });

        FirestoreManager.getMexicanCategory(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    List<Recipe> mexicanCourses = new ArrayList<>();
                    for(DocumentSnapshot document: task.getResult()){
                        mexicanCourses.add(document.toObject(Recipe.class));
                        Log.d(TAG, document.getId() + " => " + document.getData());
                    }
                    recipeMap.put(CategoryTypeEnum.Mexican.name().toString(), mexicanCourses);
                    allCategoriesRecipes.addAll(mexicanCourses);
                    finishMexican = true;
                    checkProgress();
                }
            }
        }, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Error getting documents: "+ e.getMessage());
            }
        });

        FirestoreManager.getMiddleEastrenCategory(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    List<Recipe> middleEasternCourses = new ArrayList<>();
                    for(DocumentSnapshot document: task.getResult()){
                        middleEasternCourses.add(document.toObject(Recipe.class));
                        Log.d(TAG, document.getId() + " => " + document.getData());
                    }
                    recipeMap.put(CategoryTypeEnum.Middle_Eastern.name().toString(), middleEasternCourses);
                    allCategoriesRecipes.addAll(middleEasternCourses);
                    finishMiddleEastern = true;
                    checkProgress();
                }
            }
        }, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Error getting documents: "+ e.getMessage());
            }
        });

        FirestoreManager.getThaiCategory(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    List<Recipe> thaiCourses = new ArrayList<>();
                    for(DocumentSnapshot document: task.getResult()){
                        thaiCourses.add(document.toObject(Recipe.class));
                        Log.d(TAG, document.getId() + " => " + document.getData());
                    }
                    recipeMap.put(CategoryTypeEnum.Thai.name().toString(), thaiCourses);
                    allCategoriesRecipes.addAll(thaiCourses);
                    finishThai = true;
                    checkProgress();
                }
            }
        }, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Error getting documents: "+ e.getMessage());
            }
        });
    }

    public void checkProgress(){
        if(finishAfrican &&
        finishAmerican &&
        finishChinese &&
        finishGreek &&
        finishIndian &&
        finishItalian &&
        finishMexican &&
        finishMiddleEastern &&
        finishThai){
            mainProgressBar.setVisibility(View.GONE);
            gridRecyclerView.setVisibility(View.VISIBLE);
            currentCategory = new Category(1, "All");
            currentRecipes = allCategoriesRecipes.subList(0,30);
            gridAdapter = new GridTilesRecyclerViewAdapter(currentRecipes, CategoryTypeEnum.All, thisContext);
            gridRecyclerView.setAdapter(gridAdapter);

        }
    }
}
