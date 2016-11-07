package com.example.yotam707.feedmeapp;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.support.v4.content.LocalBroadcastManager;

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

    public CoursesProgressService() {
        super("CoursesProgressService");
        broadcaster = LocalBroadcastManager.getInstance(this);
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionCoursesProgress(Context context, ArrayList<Course> courses) {
        Intent intent = new Intent(context, CoursesProgressService.class);
        intent.setAction(ACTION_START_COURSES_PROGRESS);
        intent.putExtra(EXTRA_COURSES_LIST, courses);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_START_COURSES_PROGRESS.equals(action)) {
                final ArrayList<Course> courses = (ArrayList) intent.getSerializableExtra(EXTRA_COURSES_LIST);
                handleActionCoursesProgress(courses);
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionCoursesProgress(ArrayList<Course> courses) {
        boolean allFinished = true;
        for (Course course : courses) {
            if (!course.isFinished()) {
                allFinished= false;
                Steps currentStep = course.getCurrentStep();
                if (!currentStep.isInProgress()) {
                    currentStep.startProgress();
                }
                this.sendCourseProgress(course.getId(), course.getCourseProgress());
                break;
            }
        }

        if (!allFinished) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) { }
            CoursesProgressService.startActionCoursesProgress(this.getApplicationContext(), courses);
        }
    }

    private void sendCourseProgress(int courseId, int progress) {
        Intent intent = new Intent(INTENT_COURSE_PROGRESS);
        intent.putExtra(INTENT_COURSE_ID, courseId);
        intent.putExtra(INTENT_COURSE_PROGRESS_VALUE, progress);
        broadcaster.sendBroadcast(intent);
    }
}
