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
import android.widget.ImageView;
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
    private TabLayout tabs;
    private String[] pageTitle = {"Ingredients", "Steps"};
    private ViewPager viewPager;
    Course selectedCourse;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        db = new DatabaseHandler(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawerLayout);
        setSupportActionBar(toolbar);
        viewPager = (ViewPager)findViewById(R.id.view_pager);
        //create default navigation drawer toggle
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        mHeaderImageView = (ImageView) findViewById(R.id.imgHeader);
        mHeaderTitle = (TextView) findViewById(R.id.courseNameHeader);
        tabs = (TabLayout) findViewById(R.id.tabs);
        for (int i = 0; i < 2; i++) {
            tabs.addTab(tabs.newTab().setText(pageTitle[i]));
        }
        tabs.setTabGravity(TabLayout.GRAVITY_FILL);

        String image_path = getIntent().getStringExtra("imagePath");
        selectedCourse = db.getCourseByImage(Uri.parse(image_path));
        mHeaderTitle.setText(selectedCourse.getName());
        Bitmap icon = null;
        try {
            icon = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), Uri.parse(image_path));
            mHeaderImageView.setImageBitmap(icon);
            //tabs.setBackgroundColor(getDominantColor2(icon));

        } catch (IOException e) {
            e.printStackTrace();
        }
        mHeaderTitle.setBackgroundColor(ExpandableListAdapter.getDominantColor(icon));
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        DataManager.getInstance(getApplicationContext()).createNavigationMenu(navigationView);
        assert navigationView != null;
        navigationView.setNavigationItemSelectedListener(this);

        //set viewpager adapter
        ViewPagerAdapterCourse pagerAdapter = new ViewPagerAdapterCourse(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);

        //change Tab selection when swipe ViewPager
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs));

        //change ViewPager page when tab selected
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

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
