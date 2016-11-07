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
    private boolean inProgress;
    private boolean finished;
    private long startTime = -1;


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

    public void startProgress() {
        this.inProgress = true;
        this.startTime = System.currentTimeMillis();
    }

    public boolean isInProgress() {
        return this.inProgress;
    }

    public boolean isFinished() {
        if (!this.finished && this.inProgress) {
            this.finished = (System.currentTimeMillis() - (this.startTime + ((long) this.timeImSeconds * 1000))) > 0;
            return this.finished;
        }
        this.inProgress = false;
        return this.finished;
    }

    public int getProgress() {
        if (this.isFinished())
            return this.getTimeImSeconds();

        // TODO - Finish this function. returns the seconds passed in this setp
        return 0;
    }
}
