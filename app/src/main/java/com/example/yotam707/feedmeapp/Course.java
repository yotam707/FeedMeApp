package com.example.yotam707.feedmeapp;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

/**
 * Created by yotam707 on 9/5/2016.
 */
public class Course {
    public int id;
    public int imageId;
    public String name;
    public Category category;
    public String description;
    public GenQueue<Steps> stepsGenQueue;
    public List<Steps> stepsList;
    public Queue<Steps> temp;
    public int courseProgress;
    public boolean isCurrentCourse;
    public int stepsTotalTime;
    private static final int MAX = 100;
    public int stepsCount;
    public int maxStepsTime;
    public List<Ingredient> ingredientList;
    public int getId(){return this.id;}
    public int getImageId() {
        return imageId;
    }
    public void setImageId(int id) {
        this.imageId = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Category getCategory() {
        return category;
    }
    public String getDescription() {
        return description;
    }



    public int getStepsTotalTime(){
        int count = 0;
        for(int i=0; i<stepsGenQueue.getSize(); i++){
            count+=stepsGenQueue.getByIndex(i).getTimeImSeconds();
        }
        return count;
    }

    public Course(int id, int imageId, String name, Category category, String description, List<Steps> stepsList, List<Ingredient> ingredientList){
        this.id = id;
        this.category = category;
        this.imageId = imageId;
        this.name = name;
        this.description = description;
        this.stepsList = stepsList;
        getStepsToQueue();
        this.ingredientList = ingredientList;
        this.courseProgress = 0;
        this.isCurrentCourse = false;
        this.stepsTotalTime = getStepsTotalTime();
        this.stepsCount = stepsList.size();
        this.maxStepsTime = this.stepsCount*MAX;
    }

    public void getStepsToQueue(){
        this.stepsGenQueue = new GenQueue<Steps>();
        for(Steps s: stepsList){
            s.currentStep = false;
            Steps temp = new Steps(s.stepNum,s.timeInmSeconds,s.description);
            this.stepsGenQueue.enqueue(temp);
        }
    }
    public List<Steps> getStepsList(){
        return stepsList;
    }


    public boolean isFinished(Steps currentStep ){
        //Steps currentStep = this.getCurrentStep();
        if(this.courseProgress < MAX*stepsCount) {
            if(this.stepsGenQueue.getSize() > 0){
                if(currentStep.isCurrentStepStarted()) {
                    if (currentStep.isStepFinish()) {
                        this.stepsGenQueue.remove(currentStep);
                    }
                    return false;
                }
                else{
                    currentStep.startProgress();
                    return false;
                }
            }
        }
        return true;
    }


    public int getCourseProgress(Steps currentStep){
        //Steps currentStep = this.getCurrentStep();
        if(currentStep != null){
            if(this.courseProgress < this.MAX*stepsCount){
                currentStep.getProgress();
                if(currentStep.currentProgress == MAX){
                    this.courseProgress = this.courseProgress + MAX;
                    stepsGenQueue.remove(currentStep);
                }
            }
        }
        else{
            this.courseProgress = MAX*stepsCount;
        }
        return this.courseProgress;
    }


    public Steps getCurrentStep() {
        if(this.courseProgress < MAX*stepsCount) {
            Steps currentStep = this.stepsGenQueue.peek();
            if(currentStep != null){
                return currentStep;
            }
            return null;
        }
        else {
            this.courseProgress = MAX*stepsCount;
        }
        return null;
    }
}
