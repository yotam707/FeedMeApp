package com.example.yotam707.feedmeapp;


import android.widget.ProgressBar;

import java.util.Date;

/**
 * Created by yotam707 on 9/17/2016.
 */
public class Steps {
    public enum State {
        NOT_STARTED,
        QUEUED,
        STARTING,
        COMPLETE
    }
    public int stepNum;
    public volatile int timeImSeconds;
    public String description;
    public volatile int mProgress;
    public volatile State state  = State.NOT_STARTED;
    public volatile ProgressBar progressBar;

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    public void setProgressBar(ProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    public Steps(int stepNum, int timeImSeconds, String description){
        this.stepNum = stepNum;
        this.timeImSeconds = timeImSeconds;
        this.description = description;
        this.mProgress = 0;
        this.progressBar = null;
    }

    public String getDescription() {
        return description;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getStepNum() {
        return stepNum;
    }

    public void setStepNum(int stepNum) {
        this.stepNum = stepNum;
    }

    public int getTimeImSeconds() {
        return timeImSeconds;
    }

    public void setTimeImSeconds(int timeImSeconds) {
        this.timeImSeconds = timeImSeconds;
    }

    public int getmProgress() {
        return mProgress;
    }

    public void setmProgress(int mProgress) {
        this.mProgress = mProgress;
    }


}
