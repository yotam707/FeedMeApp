package com.example.yotam707.feedmeapp;

import com.example.yotam707.feedmeapp.DB.DatabaseHandler;
import com.example.yotam707.feedmeapp.data.DataManager;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DetailActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public static final String EXTRA_PARAM_ID = "detail:_id";
    // View name of the header image. Used for activity scene transitions
    public static final String VIEW_NAME_HEADER_IMAGE = "detail:header:image";

    // View name of the header title. Used for activity scene transitions
    public static final String VIEW_NAME_HEADER_TITLE = "detail:header:title";

    private ImageView mHeaderImageView;
    private TextView mHeaderTitle;
    private DatabaseHandler db;
    private DrawerLayout drawer;
    private ListView ingredientList;
    private ListView stepsList;
    private ViewPager viewPager;
    Course selectedCourse;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        db = new DatabaseHandler(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawerLayout);
        setSupportActionBar(toolbar);
        viewPager = (ViewPager)findViewById(R.id.view_pager1);
        //create default navigation drawer toggle
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        mHeaderImageView = (ImageView) findViewById(R.id.imgHeader);
        mHeaderTitle = (TextView) findViewById(R.id.courseNameHeader);

        String image_path = getIntent().getStringExtra("imagePath");
        selectedCourse = db.getCourseByImage(Uri.parse(image_path));
        mHeaderTitle.setText(selectedCourse.getName());
        Bitmap icon = null;
        try {
            icon = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), Uri.parse(image_path));
            mHeaderImageView.setImageBitmap(icon);

        } catch (IOException e) {
            e.printStackTrace();
        }
        mHeaderTitle.setBackgroundColor(ExpandableListAdapter.getDominantColor(icon));

        ingredientList = (ListView) findViewById(R.id.ingredient_list);
        ingredientList.setAdapter(new IngredientsListAdapter(getApplicationContext(),selectedCourse));

        stepsList = (ListView) findViewById(R.id.steps_list);
        stepsList.setAdapter(new StepsListAdapter(getApplicationContext(),selectedCourse));

        ListUtils.setDynamicHeight(ingredientList);
        ListUtils.setDynamicHeight(stepsList);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        DataManager.getInstance(getApplicationContext()).createNavigationMenu(navigationView);
        assert navigationView != null;
        navigationView.setNavigationItemSelectedListener(this);



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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.top) {
            viewPager.setCurrentItem(0);
        } else if (id == R.id.cat) {
            viewPager.setCurrentItem(1);
        } else if (id == R.id.close) {
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
