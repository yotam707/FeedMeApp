package com.example.yotam707.feedmeapp;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.support.v4.content.LocalBroadcastManager;

import com.example.yotam707.feedmeapp.data.DataManager;

import java.util.ArrayList;
import java.util.List;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class CoursesProgressService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_START_COURSES_PROGRESS = "com.example.yotam707.feedmeapp.action.START_COURSES_PROGRESS";

    // TODO: Rename parameters
    private static final String EXTRA_COURSES_LIST = "com.example.yotam707.feedmeapp.extra.COURSES_LIST";

    public static final String INTENT_COURSE_PROGRESS = "com.example.yotam707.feedmeapp.extra.INTENT_COURSE_PROGRESS";
    public static final String INTENT_COURSE_PROGRESS_VALUE = "com.example.yotam707.feedmeapp.extra.INTENT_COURSE_PROGRESS_VALUE";
    public static final String INTENT_COURSE_ID = "com.example.yotam707.feedmeapp.extra.INTENT_COURSE_ID";

    LocalBroadcastManager broadcaster;
    private ArrayList<Course> courses;

    public CoursesProgressService() {
        super("CoursesProgressService");
        broadcaster = LocalBroadcastManager.getInstance(this);
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        courses = new ArrayList<>(DataManager.getInstance().getAddedCourses());
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionCoursesProgress(Context context) {
        Intent intent = new Intent(context, CoursesProgressService.class);
        intent.setAction(ACTION_START_COURSES_PROGRESS);
        //intent.putExtra(EXTRA_COURSES_LIST, coursesIndexes);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_START_COURSES_PROGRESS.equals(action)) {
                //final ArrayList<Integer> coursesIndexes = (ArrayList) intent.getSerializableExtra(EXTRA_COURSES_LIST);
                handleActionCoursesProgress();
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionCoursesProgress() {
        boolean allFinished = true;
        for (Course course : courses) {
            if (!course.isFinished()) {
                allFinished= false;
                Steps currentStep = course.getCurrentStep();
                if(currentStep != null) {
                    if (!currentStep.isInProgress()) {
                        currentStep.startProgress();
                    }
                    long courseProg = course.getCourseProgress();
                    this.sendCourseProgress(course.getId(), courseProg);
                    break;
                }
            }
        }

        if (!allFinished) {
            try {
                Thread.sleep(1000);
            }
            catch (InterruptedException e) { }
            CoursesProgressService.startActionCoursesProgress(this.getApplicationContext());
        }
    }

    private void sendCourseProgress(int courseId, long progress) {
        Intent intent = new Intent(INTENT_COURSE_PROGRESS);
        intent.putExtra(INTENT_COURSE_ID, courseId);
        intent.putExtra(INTENT_COURSE_PROGRESS_VALUE, progress);
        broadcaster.sendBroadcast(intent);
    }
}
