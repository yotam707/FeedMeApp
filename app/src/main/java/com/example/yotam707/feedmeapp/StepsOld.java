package com.example.yotam707.feedmeapp;


import java.util.concurrent.TimeUnit;

/**
 * Created by yotam707 on 9/17/2016.
 */
public class StepsOld {
    public int stepNum;
    public  int timeInmSeconds;
    public String description;
    public  int mProgress;
    public  boolean currentStep;
    public boolean wasNotificationSent;
    private boolean finished;
    private int startTime;
    public int currentTime;
    public int calculatedTime;
    public int courseId;
    public int currentProgress;
    private static final int MAX = 100;


    public StepsOld(int stepNum, int timeImSeconds, String description){
        this.stepNum = stepNum;
        this.timeInmSeconds = timeImSeconds;
        this.description = description;
        this.mProgress = 0;
        this.finished = false;
        this.currentStep = false;
        this.startTime = -1;
        this.currentTime = 0;
        this.calculatedTime = 0;
        this.currentProgress = 0;
        this.wasNotificationSent = false;

    }

    public StepsOld(int courseId, int stepNum, int timeImSeconds, String description){
        this.courseId =courseId;
        this.stepNum = stepNum;
        this.timeInmSeconds = timeImSeconds;
        this.description = description;
        this.mProgress = 0;
        this.finished = false;
        this.currentStep = false;
        this.startTime = -1;
        this.currentTime = 0;
        this.calculatedTime = 0;
        this.currentProgress = 0;
        this.wasNotificationSent = false;

    }
    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
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

        this.currentStep = true;
        this.startTime = (int)TimeUnit.SECONDS.convert(System.currentTimeMillis(),TimeUnit.MILLISECONDS);
    }

    public boolean isCurrentStep() {
        isStepFinish();
        return this.currentStep;
    }

    public boolean isCurrentStepStarted(){
        if(this.startTime > 0)
            return true;
        return false;
    }

    public boolean checkStepComplete(){
        if(this.currentStep && !this.finished){
            if(this.isStepFinish()){
                this.currentStep = false;
                return true;
            }
            else{
                this.currentStep = true;
                return false;
            }
        }
        return true;
    }

    public boolean isStepFinish(){
        boolean temp = false;
        if(this.startTime > 0) {
            this.currentTime = (int)TimeUnit.SECONDS.convert(System.currentTimeMillis(),TimeUnit.MILLISECONDS);

            this.calculatedTime = (int)TimeUnit.SECONDS.toMillis(this.currentTime - this.startTime);
            if(this.calculatedTime < this.timeInmSeconds){
                this.finished = false;
                this.currentStep = true;
                temp = false;
            }
            else{
                this.currentProgress = MAX;
                this.currentStep = false;
                this.finished = true;
                temp = true;
            }
        }
        return temp;
    }


    public int getProgress() {
        int temp = 0;
        if (!this.isStepFinish()) {
            if (this.currentProgress < MAX) {
                temp = ((this.calculatedTime * 100) / this.timeInmSeconds);
                this.currentProgress = temp;
            }
            else {
                temp = MAX;
                this.currentProgress = temp;
            }
        }
        else if(this.currentProgress == MAX){
            temp = MAX;
        }
        return temp;
    }
}
