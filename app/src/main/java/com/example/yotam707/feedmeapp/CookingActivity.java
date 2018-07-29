 package com.example.yotam707.feedmeapp;

import android.app.FragmentManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.yotam707.feedmeapp.data.DataManager;
import com.example.yotam707.feedmeapp.data.Firestore.FirestoreManager;
import com.example.yotam707.feedmeapp.domain.Equipment;
import com.example.yotam707.feedmeapp.domain.FullRecipesCookingRequest;
import com.example.yotam707.feedmeapp.domain.Ingredients;
import com.example.yotam707.feedmeapp.domain.Step;
import com.example.yotam707.feedmeapp.domain.Steps;
import com.example.yotam707.feedmeapp.ui.ui.adapters.StepEquipmentRecyclerViewAdapter;
import com.example.yotam707.feedmeapp.ui.ui.adapters.StepIngredientRecyclerViewAdapter;
import com.example.yotam707.feedmeapp.ui.ui.fragments.IngredientsFragmentDialog;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.common.collect.Lists;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Stack;

import javax.annotation.Nullable;

import static java.util.concurrent.TimeUnit.MILLISECONDS;




 public class CookingActivity extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener{
     private static final String CHANNEL_ID = "cooking_channel";
     private DrawerLayout drawer;
     MaterialDialog dialog;
     CountDownTimer timer;
     Stack<Step> stepStack = new Stack<>();
     Stack<Step> stepAlertStack = new Stack();
     //Declare a variable to hold count down timer's paused status
     private boolean isPaused = false;
     //Declare a variable to hold count down timer's paused status
     private boolean isCanceled = false;
    HashMap<String, HashMap<Integer,Ingredients>> ingredientsHashMap;

     StepIngredientRecyclerViewAdapter ingredientAdapter;
     StepEquipmentRecyclerViewAdapter equipmentAdapter;

     LinearLayoutManager layoutManagerIng;
     LinearLayoutManager layoutManagerEquip;

     RecyclerView ingredientRecyclerView;
     RecyclerView equipmentRecyclerView;
    List<Ingredients> ingList;
    List<Equipment> eqiupList;
     //Declare a variable to hold CountDownTimer remaining time
     private long timeRemaining = 0;
     TextView tView;
     Button btnStart;
     Button btnPause;
     Button btnResume;
     Button btnAddOne;
     TextView stepTView;
     TextView passiveAlert;
     TextView stepDescTView;
     TextView equipmentListTitle;
     TextView ingredientListTitle;
     Button finishButton;
     FullRecipesCookingRequest req;
     private Context mContext;
     public static final String TAG = "";
     ListenerRegistration registration;
     ListenerRegistration registration2;
     boolean firstStepIng = true;
     boolean firstStepEquip = true;

     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cooking);

        mContext = this;
        ingredientsHashMap = new HashMap<>();
        ingList = new ArrayList<>();
        eqiupList = new ArrayList<>();
        drawer = findViewById(R.id.drawerLayout);
        finishButton = findViewById(R.id.finish_button_cooking_activity);
        equipmentListTitle = findViewById(R.id.cooking_equipment_title);
        ingredientListTitle = findViewById(R.id.cooking_ingredients_title);
        equipmentListTitle.setVisibility(View.INVISIBLE);
        ingredientListTitle.setVisibility(View.INVISIBLE);
        tView = findViewById(R.id.tv);
        passiveAlert = findViewById(R.id.passive_alert);
        passiveAlert.setVisibility(View.INVISIBLE);
        btnStart = findViewById(R.id.btn_start);
        btnPause = findViewById(R.id.btn_pause);
        btnResume = findViewById(R.id.btn_resume);
        btnAddOne = findViewById(R.id.btn_add_time);
        stepTView = findViewById(R.id.step_name);
        stepDescTView = findViewById(R.id.step_des);
         initRecyclerView();
         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
             createChannel();
         }
        btnPause.setEnabled(false);
        btnResume.setEnabled(false);
        btnStart.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                long millisInFuture = 60000;
                if(!stepStack.empty()) {
                    Step s = stepStack.pop();
                    if(s != null) {
                        if(s.getLength() != null) {
                            if(s.getLength().getNumber() > 0)
                                millisInFuture = s.getLength().getNumber() * 60000;
                        }
                        bigTextStyleNotification(s.getStep(), s.getFullRecipeName());
                        if(s.isPassive())
                            passiveAlert.setVisibility(View.VISIBLE);
                        else
                            passiveAlert.setVisibility(View.INVISIBLE);

                        if(ingList.size() > 0){
                            ingList.clear();
                            if(s.getIngredients() != null && s.getIngredients().size() > 0){
                                ingList = s.getIngredients();
                                //updateIngrdientList(s.getFullRecipeName());
                                ingredientAdapter.SetIngrdients(ingList);
                                ingredientListTitle.setVisibility(View.VISIBLE);
                            }
                            else{
                                ingredientListTitle.setVisibility(View.INVISIBLE);
                            }
                            ingredientAdapter.notifyDataSetChanged();
                        }
                        else{
                            if(s.getIngredients() != null && s.getIngredients().size() > 0){
                                ingList = s.getIngredients();
                                ingredientAdapter.SetIngrdients(ingList);
                                ingredientAdapter.notifyDataSetChanged();
                                ingredientListTitle.setVisibility(View.VISIBLE);
                            }
                            else{
                                ingredientListTitle.setVisibility(View.INVISIBLE);
                            }
                        }
                        if(eqiupList.size() > 0){
                            eqiupList.clear();
                            if(s.getEquipment() != null && s.getEquipment().size() > 0){
                                eqiupList = s.getEquipment();
                                equipmentAdapter.SetEquipment(eqiupList);
                                equipmentListTitle.setVisibility(View.VISIBLE);
                            }
                            else{
                                equipmentListTitle.setVisibility(View.INVISIBLE);
                            }
                            equipmentAdapter.notifyDataSetChanged();
                        }
                        else{
                            if(s.getEquipment() != null && s.getEquipment().size() > 0){
                                eqiupList = s.getEquipment();
                                equipmentAdapter.SetEquipment(eqiupList);
                                equipmentAdapter.notifyDataSetChanged();
                                equipmentListTitle.setVisibility(View.VISIBLE);
                            }
                            else{
                                equipmentListTitle.setVisibility(View.INVISIBLE);
                            }
                        }

                        stepDescTView.setText(s.getStep());
                        stepTView.setText(s.getFullRecipeName());
                        isPaused = false;
                        isCanceled = false;
                        //Disable the start and pause button
                        btnStart.setEnabled(true);
                        btnResume.setEnabled(false);
                        //Enabled the pause and cancel button
                        btnPause.setEnabled(true);
                        if(timer != null)
                            timer.cancel();
                        createCountDownTimer(millisInFuture);
                    }
                    else{
                        timer.cancel();
                    }
                }

            }
        });

        btnPause.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //When user request to pause the CountDownTimer
                isPaused = true;
                //Enable the resume and cancel button
                btnResume.setEnabled(true);
                //Disable the start and pause button
                btnStart.setEnabled(true);
                btnPause.setEnabled(false);
            }
        });

        //Set a Click Listener for resume button
        btnResume.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //Disable the start and resume button
                btnStart.setEnabled(true);
                btnResume.setEnabled(false);
                //Enable the pause and cancel button
                btnPause.setEnabled(true);


                //Specify the current state is not paused and canceled.
                isPaused = false;
                isCanceled = false;
                createCountDownTimer(timeRemaining);


                btnAddOne.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        isCanceled = true;
                        //When user request to cancel the CountDownTimer
                        timeRemaining += 60000;
                        timer.cancel();
                        createCountDownTimer(timeRemaining);
                        Toast.makeText(mContext, "+1 minute added", Toast.LENGTH_SHORT).show();
                        //Disable the cancel, pause and resume button
                        btnPause.setEnabled(true);
                        btnResume.setEnabled(false);
                        //Enable the start button
                        btnStart.setEnabled(true);
                    }
                });
            }
        });

        //Set a Click Listener for cancel/stop button
        btnAddOne.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //When user request to cancel the CountDownTimer
                isCanceled = false;
                isPaused = false;
                //When user request to cancel the CountDownTimer
                timeRemaining += 60000;
                timer.cancel();
                createCountDownTimer(timeRemaining);
                Toast.makeText(mContext, "+1 minute added", Toast.LENGTH_SHORT).show();
                //Disable the cancel, pause and resume button
                btnPause.setEnabled(true);
                btnResume.setEnabled(true);
                //Enable the start button
                btnStart.setEnabled(false);
            }
        });

        final FragmentManager fm = getFragmentManager();
        final IngredientsFragmentDialog idf = new IngredientsFragmentDialog();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.nav_view);
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
        registration = FirestoreManager.getCalculatedCookingData().addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "listen:error", e);
                    finish();
                    return;
                }

                if (queryDocumentSnapshots != null) {
                    for (DocumentChange dc : queryDocumentSnapshots.getDocumentChanges()) {
                        switch (dc.getType()) {
                            case ADDED:
                                Steps stepsList;
                                Steps reversedList = new Steps();
                                dialog.dismiss();
                                for(DocumentSnapshot documentSnapshot :  queryDocumentSnapshots.getDocuments()){
                                    if(documentSnapshot.getId().equals("Steps")){
                                        stepsList = documentSnapshot.toObject(Steps.class);
                                        if(stepsList != null) {
                                            reversedList.setSteps(Lists.reverse(stepsList.getSteps()));
                                            Iterator<Step> stepIterator = reversedList.getSteps().iterator();
                                            while(stepIterator.hasNext()){
                                                Step s = stepIterator.next();
                                                if(s != null) {
                                                    if(s.isPassive()){
                                                        stepAlertStack.push(s);
                                                        s.getLength().setNumber(2);
                                                    }
                                                    stepStack.push(s);
                                                }
                                            }
                                            getFullSelectedRecipes(fm, idf);
                                            registration.remove();
                                        }
                                    }
                                }

                                Log.d(TAG, "New city: " + dc.getDocument().getData());
                                break;
                        }
                    }
                }
            }
        });
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });
    }
     @RequiresApi(Build.VERSION_CODES.O)
     private void createChannel() {
         NotificationManager
                 mNotificationManager =
                 (NotificationManager) mContext
                         .getSystemService(Context.NOTIFICATION_SERVICE);
         // The id of the channel.
         String id = CHANNEL_ID;
         // The user-visible name of the channel.
         CharSequence name = "Media playback";
         // The user-visible description of the channel.
         String description = "Media playback controls";
         int importance = NotificationManager.IMPORTANCE_LOW;
         NotificationChannel mChannel = new NotificationChannel(id, name, importance);
         // Configure the notification channel.
         mChannel.setDescription(description);
         mChannel.setShowBadge(false);
         mChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
         mNotificationManager.createNotificationChannel(mChannel);
     }
    public void finish(){
        FirestoreManager.removeAllSelectedCourses(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "remove all selected courses failed.", e);
            }
        });
        DataManager.getInstance().clearAddedCourse();
        Intent intent1 =  new Intent(CookingActivity.this, MainCourseActivity.class);
        if(timer != null)
            timer.cancel();
        startActivity(intent1);
    }

     private void bigTextStyleNotification(String description, String recipeName) {
         int NOTIFICATION_ID = 1;
         PendingIntent launchIntent = getLaunchIntent(NOTIFICATION_ID, getBaseContext());
         Intent buttonIntent = new Intent(getBaseContext(), NotificationReceiver.class);
         buttonIntent.putExtra("notificationId", NOTIFICATION_ID);
         PendingIntent dismissIntent = PendingIntent.getBroadcast(getBaseContext(), 0, buttonIntent, 0);


         NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, CHANNEL_ID);
         builder.setSmallIcon(R.drawable.status_bar_logo);
         builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.feed_me));
         builder.setContentTitle(recipeName);
         builder.setStyle(new NotificationCompat.BigTextStyle().bigText(description));
         builder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
         builder.setAutoCancel(true);
         builder.setContentIntent(launchIntent);
         builder.addAction(android.R.drawable.ic_delete, "DISMISS", dismissIntent);
         builder.addAction(android.R.drawable.ic_menu_send, "OPEN APP", launchIntent);
         NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

         // Will display the notification in the notification bar
         notificationManager.notify(NOTIFICATION_ID, builder.build());
     }

     public PendingIntent getLaunchIntent(int notificationId, Context context) {

         Intent intent = new Intent(context, CookingActivity.class);
         intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
         intent.putExtra("notificationId", notificationId);
         return PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
     }
    public void getFullSelectedRecipes(final FragmentManager fm,final IngredientsFragmentDialog idf ){
        registration2 = FirestoreManager.getCalculatedFullRecipesData().addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "listen:error", e);
                    finish();
                    return;
                }
                if (queryDocumentSnapshots != null) {
                    for (DocumentChange dc : queryDocumentSnapshots.getDocumentChanges()) {
                        switch (dc.getType()) {
                            case ADDED:
                                if(stepStack.size() <= 0)
                                    finish();

                                for(DocumentSnapshot documentSnapshot :  queryDocumentSnapshots.getDocuments()){
                                    if(documentSnapshot.getId().equals("FullRecipes")){
                                        req = documentSnapshot.toObject(FullRecipesCookingRequest.class);
                                        if(req != null) {
                                            //createIngredientHashMap(req);
                                            idf.setExtendedIngredients(req.getFullRecipes().get(0).getExtendedIngredients());
                                            idf.show(fm, "ingredient_dialog");
                                            finishButton.setEnabled(true);
                                            registration2.remove();
                                        }
                                    }
                                }
                                Log.d(TAG, "New city: " + dc.getDocument().getData());
                                break;
                        }
                    }
                }
            }
        });
    }

     public void initRecyclerView(){
         layoutManagerIng = new LinearLayoutManager(mContext,LinearLayoutManager.HORIZONTAL,false);
         layoutManagerEquip = new LinearLayoutManager(mContext,LinearLayoutManager.HORIZONTAL,false);
         ingredientRecyclerView = findViewById(R.id.cooking_ingredients_rv);
         equipmentRecyclerView = findViewById(R.id.cooking_equipment_rv);
         ingredientRecyclerView.setLayoutManager(layoutManagerIng);
         equipmentRecyclerView.setLayoutManager(layoutManagerEquip);
         ingredientAdapter = new StepIngredientRecyclerViewAdapter(mContext);
         ingredientRecyclerView.setAdapter(ingredientAdapter);
         equipmentAdapter = new StepEquipmentRecyclerViewAdapter(mContext);
         equipmentRecyclerView.setAdapter(equipmentAdapter);


     }
    public void createCountDownTimer(long millisInFuture){
        timer = new CountDownTimer(millisInFuture, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if(isPaused || isCanceled)
                {
                    cancel();
                }
                else {
                    String text = String.format(Locale.getDefault(), "Time Remaining %02d min: %02d sec",
                            MILLISECONDS.toMinutes(millisUntilFinished) % 60,
                            MILLISECONDS.toSeconds(millisUntilFinished) % 60);
                    tView.setText(text);
                    //Put count down timer remaining time in a variable
                    timeRemaining = millisUntilFinished;
                }
            }

            @Override
            public void onFinish() {
                tView.setText("Done");

                //Enable the start button
                btnStart.setEnabled(true);
                //Disable the pause, resume and cancel button
                btnPause.setEnabled(false);
                btnResume.setEnabled(false);
                btnStart.callOnClick();
            }
        }.start();
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
