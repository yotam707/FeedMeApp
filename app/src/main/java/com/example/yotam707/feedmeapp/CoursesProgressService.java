package com.example.yotam707.feedmeapp;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.example.yotam707.feedmeapp.data.DataManager;

import java.util.List;

public class CoursesProgressService extends IntentService {

    private static final String ACTION_START_COURSES_PROGRESS = "com.example.yotam707.feedmeapp.action.START_COURSES_PROGRESS";


    public static final String INTENT_COURSE_PROGRESS = "com.example.yotam707.feedmeapp.extra.INTENT_COURSE_PROGRESS";
    public static final String INTENT_COURSE_PROGRESS_VALUE = "com.example.yotam707.feedmeapp.extra.INTENT_COURSE_PROGRESS_VALUE";
    public static final String INTENT_COURSE_ID = "com.example.yotam707.feedmeapp.extra.INTENT_COURSE_ID";
    LocalBroadcastManager broadcaster;
    private GenQueue<Course> courses;
    private List<Course> coursesList;
    NotificationManager manager;

    public CoursesProgressService() {
        super("CoursesProgressService");
        broadcaster = LocalBroadcastManager.getInstance(this);
    }

    @Override
    public void onCreate() {
        courses = DataManager.getInstance().getQueueAddedCourses();
        coursesList = DataManager.getInstance().getListAddedCourses();
        manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        super.onCreate();

    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    }

    public static void startActionCoursesProgress(Context context) {
        Intent intent = new Intent(context, CoursesProgressService.class);
        intent.setAction(ACTION_START_COURSES_PROGRESS);
        context.startService(intent);
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_START_COURSES_PROGRESS.equals(action)) {
                handleActionCoursesProgress();
            }
        }
    }

    private void handleActionCoursesProgress(){
        try {
            boolean allFinished = true;
            Course currentCourse = courses.peek();
            if (currentCourse != null) {
                allFinished = false;
                Steps currentStep = currentCourse.getCurrentStep();
                if(currentStep != null){
                    if(currentStep.currentProgress == 0 && !currentStep.wasNotificationSent){
                        if(!currentStep.wasNotificationSent){
                            sendNotification(currentCourse.getName(), currentStep.getDescription());
                            currentStep.wasNotificationSent = true;
                        }
                    }
                    if(!currentCourse.isFinished(currentStep)) {
                        Log.e("current Course", "name:" + currentCourse.getName());
                        if (currentStep != null) {
                            Log.e("current Step", "number:" + currentStep.getStepNum());
                            currentCourse.getCourseProgress(currentStep);
                            int courseProgress = currentStep.currentProgress;
                            if (currentCourse.maxStepsTime == courseProgress) {
                                courses.remove(currentCourse);
                            }
                            Log.e("progress service data", "name:" + currentCourse.getName() + " progress:" + courseProgress);
                            this.sendCourseProgress(currentCourse.getId(), courseProgress);
                        }
                    }
                    else{
                        courses.remove(currentCourse);
                    }
                }
                else{
                    courses.remove(currentCourse);
                }
            }
            if (!allFinished) {
                Toast.makeText(this, "all finished is false", Toast.LENGTH_LONG)
                        .show();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                }
                CoursesProgressService.startActionCoursesProgress(this.getApplicationContext());
            }
            Toast.makeText(this, "all finished is true", Toast.LENGTH_LONG);
        }
        catch(Exception ex){
            Log.e("CourseProgressService", ex.getMessage());
        }
    }
    public void sendNotification(String title, String text){
        int uniqueId = (int)System.currentTimeMillis();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext())
                .setSmallIcon(R.drawable.feed_me_logo)
                .setContentTitle(title)
                .setContentText(text);
        Intent notificationIntent = new Intent(getApplicationContext(),CoursesProgressService.class);
        PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(),0,notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);
        manager.notify(uniqueId, builder.build());
    }

    private void sendCourseProgress(int courseId, int progress) {
        Intent intent = new Intent(INTENT_COURSE_PROGRESS);
        intent.putExtra(INTENT_COURSE_ID, courseId);
        intent.putExtra(INTENT_COURSE_PROGRESS_VALUE, progress);
        broadcaster.sendBroadcast(intent);
    }
}
