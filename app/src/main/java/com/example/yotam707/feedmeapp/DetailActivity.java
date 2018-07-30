package com.example.yotam707.feedmeapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.yotam707.feedmeapp.data.Firestore.FirestoreManager;
import com.example.yotam707.feedmeapp.data.Firestore.FirestoreStorageManager;
import com.example.yotam707.feedmeapp.domain.FullRecipe;
import com.example.yotam707.feedmeapp.domain.Recipe;
import com.example.yotam707.feedmeapp.ui.ui.adapters.IngredientsRecyclerViewAdapter;
import com.example.yotam707.feedmeapp.ui.ui.adapters.StepsRecyclerViewAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class DetailActivity extends AppCompatActivity {
    public static final String EXTRA_PARAM_ID = "detail:_id";
    // View name of the header image. Used for activity scene transitions
    public static final String VIEW_NAME_HEADER_IMAGE = "detail:header:image";

    // View name of the header title. Used for activity scene transitions
    public static final String VIEW_NAME_HEADER_TITLE = "detail:header:title";
    private final String TAG = this.getClass().getSimpleName();

    RecyclerView ingredientsRecipeRv;
    RecyclerView stepsRecipeRv;
    ProgressBar ingredientsRecipePb;
    ImageView image;
    //ProgressBar stepsRecipePb;
    private AppBarLayout appBarLayout;
    private boolean appBarExpanded = true;
    IngredientsRecyclerViewAdapter ingredientsRecyclerViewAdapter;
    StepsRecyclerViewAdapter stepsRecyclerViewAdapter;
    Context mContext;
    //private DrawerLayout drawer;
    private ViewPager viewPager;
    FullRecipe fullRecipe;
    Course selectedCourse;
    private CollapsingToolbarLayout collapsingToolbar;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_detailed_recipe);
        toolbar = findViewById(R.id.toolbar);
        ingredientsRecipeRv = findViewById(R.id.ingredient_rv);
        image = findViewById(R.id.ivParallax);
        stepsRecipeRv = findViewById(R.id.steps_rv);
        ingredientsRecipePb = findViewById(R.id.ingredient_progress);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        collapsingToolbar =  findViewById(R.id.collapsingToolbarLayout);
        appBarLayout = findViewById(R.id.appBarLayout);
        viewPager = findViewById(R.id.view_pager1);

        String courseId = getIntent().getStringExtra("courseId");
        CourseType type = CourseType.valueOf(getIntent().getStringExtra("type"));
        FirestoreManager.getRecipeByIdAndType(courseId, type, new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    Recipe recipe = null;
                    for(DocumentSnapshot document :  task.getResult()) {
                        recipe = document.toObject(Recipe.class);
                        Log.d(TAG, document.getId() + " => " + document.getData());
                    }
                    if(recipe != null) {
                        Glide
                            .with(mContext)
                            .asBitmap()
                            .load(FirestoreStorageManager.storageReference.child(recipe.getImage()))
                            .into(new SimpleTarget<Bitmap>(image.getWidth(), image.getHeight()) {
                                @Override
                                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                    image.setImageBitmap(resource);
                                    Palette.from(resource).generate(new Palette.PaletteAsyncListener() {
                                        @SuppressWarnings("ResourceType")
                                        @Override
                                        public void onGenerated(Palette palette) {
                                            int vibrantColor = palette.getVibrantColor(R.color.colorPrimary);
                                            collapsingToolbar.setContentScrimColor(vibrantColor);
                                            collapsingToolbar.setStatusBarScrimColor(R.color.colorPrimaryDark);
                                        }
                                    });
                                }
                            });
                    }

                }
            }
        }, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
        ingredientsRecipePb.setVisibility(View.VISIBLE);

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                Log.d(TAG, "onOffsetChanged: verticalOffset: " + verticalOffset);

                //  Vertical offset == 0 indicates appBar is fully expanded.
                if (Math.abs(verticalOffset) > 200) {
                    appBarExpanded = false;
                    invalidateOptionsMenu();
                } else {
                    appBarExpanded = true;
                    invalidateOptionsMenu();
                }
            }
        });
        FirestoreManager.getFullRecipe(courseId, new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    fullRecipe = new FullRecipe();
                    DocumentSnapshot document = task.getResult();
                    fullRecipe = document.toObject(FullRecipe.class);
                    Log.d(TAG, document.getId() + " => " + document.getData());

                    toolbar.setTitle(fullRecipe.getTitle());
                    collapsingToolbar.setTitle(fullRecipe.getTitle());
                    ingredientsRecyclerViewAdapter = new IngredientsRecyclerViewAdapter(fullRecipe.getExtendedIngredients(), mContext);
                    ingredientsRecipeRv.setAdapter(ingredientsRecyclerViewAdapter);
                    ingredientsRecipeRv.setHasFixedSize(true);
                    ingredientsRecipeRv.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
                    ingredientsRecipePb.setVisibility(View.GONE);
                    ingredientsRecipePb.setNestedScrollingEnabled(true);
                    if(fullRecipe.getAnalyzedInstructions() != null) {
                        if(fullRecipe.getAnalyzedInstructions().get(0).getSteps() != null) {
                            stepsRecyclerViewAdapter = new StepsRecyclerViewAdapter(fullRecipe.getAnalyzedInstructions().get(0).getSteps(),fullRecipe.getReadyInMinutes() ,mContext);
                            stepsRecipeRv.setAdapter(stepsRecyclerViewAdapter);
                            stepsRecipeRv.setHasFixedSize(true);
                            stepsRecipeRv.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
                            stepsRecipeRv.setNestedScrollingEnabled(true);
                        }
                    }
                }
            }
        }, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Error fetching full recipe " + e.getMessage());
            }
        });

    }

    public static int getDominantColor(Bitmap bitmap) {
        Bitmap newBitmap = Bitmap.createScaledBitmap(bitmap, 1, 1, true);
        final int color = newBitmap.getPixel(0, 0);
        newBitmap.recycle();
        return color;
    }

    public int getDominantColor2(Bitmap bitmap) {
        if (bitmap == null)
            throw new NullPointerException();

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int size = width * height;
        int pixels[] = new int[size];

        Bitmap bitmap2 = bitmap.copy(Bitmap.Config.ARGB_4444, false);

        bitmap2.getPixels(pixels, 0, width, 0, 0, width, height);

        HashMap<Integer, Integer> colorMap = new HashMap<Integer, Integer>();

        int color = 0;
        Integer count = 0;
        for (int i = 0; i < pixels.length; i++) {
            color = pixels[i];
            count = colorMap.get(color);
            if (count == null)
                count = 0;
            colorMap.put(color, ++count);
        }

        int dominantColor = 0;
        int max = 0;
        for (Map.Entry<Integer, Integer> entry : colorMap.entrySet()) {
            if (entry.getValue() > max) {
                max = entry.getValue();
                dominantColor = entry.getKey();
            }
        }
        return dominantColor;
    }

    public Course getSelectedCourse(){
        return selectedCourse;
    }
}

 class ListUtils {
    public static void setDynamicHeight(ListView mListView) {
        ListAdapter mListAdapter = mListView.getAdapter();
        if (mListAdapter == null) {
            // when adapter is null
            return;
        }
        int height = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(mListView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        for (int i = 0; i < mListAdapter.getCount(); i++) {
            View listItem = mListAdapter.getView(i, null, mListView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            height += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = mListView.getLayoutParams();
        params.height = height + (mListView.getDividerHeight() * (mListAdapter.getCount() - 1));
        mListView.setLayoutParams(params);
        mListView.requestLayout();
    }
}
