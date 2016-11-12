package com.example.yotam707.feedmeapp;


import android.os.CountDownTimer;
import android.widget.ProgressBar;

import java.util.Date;

/**
 * Created by yotam707 on 9/17/2016.
 */
public class Steps {
    public int stepNum;
    public volatile int timeInmSeconds;
    public String description;
    public volatile int mProgress;
    public  boolean inProgress;
    private boolean finished;
    private long startTime = -1;
    //private CountDownTimer stepTimer;
    private final int interval = 1000;


    public Steps(int stepNum, int timeImSeconds, String description){
        this.stepNum = stepNum;
        this.timeInmSeconds = timeImSeconds;
        this.description = description;
        this.mProgress = 0;
        this.finished = false;
        this.inProgress = false;
    }

    public String getDescription() {
        return description;
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
        return timeInmSeconds;
    }

    public void setTimeImSeconds(int timeImSeconds) {
        this.timeInmSeconds = timeImSeconds;
    }

    public void startProgress() {
        this.inProgress = true;
        this.startTime = System.currentTimeMillis();
    }

    public boolean isInProgress() {
        return this.inProgress;
    }

    public boolean isFinished() {
            if (!this.finished || this.inProgress) {
                long timeVal = (System.currentTimeMillis() - (this.startTime + ((long) this.timeInmSeconds * 2)));
                this.finished = timeVal > 0;
                if (this.finished == true) {
                    this.inProgress = false;
                }
                this.inProgress = true;
                return this.finished;
            }
        return this.finished;
    }

    public int getProgress() {
        if (this.isFinished())
            return 0;
        else
            return this.interval;

    }

}
