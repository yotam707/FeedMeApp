package com.example.yotam707.feedmeapp;


import android.graphics.Bitmap;
import android.net.Uri;

import java.net.URI;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yotam707 on 9/5/2016.
 */
public class Course {
    private Uri image;
	private String categoryName;
    private int categoryId;
    public int id;
    public String name;
    public Category category;
    public String description;
    public GenQueue<Steps> stepsGenQueue;
    public List<Steps> stepsList;
    public int courseProgress;
    public boolean isCurrentCourse;
    public int stepsTotalTime;
    private static final int MAX = 100;
    public int stepsCount;
    public int maxStepsTime;
    private CourseType courseType;
    public int imageId;

    public Course(){

    }

    public void setId(int id) {
        this.id = id;
    }

    public void setImage(Uri image) {
        this.image = image;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public void setStepsList(List<Steps> stepsList) {
        this.stepsList = stepsList;
    }

    public void setIngredientList(List<Ingredient> ingredientList) {
        this.ingredientList = ingredientList;
    }


    public void setCategory(Category category) {
        this.category = category;
    }


    public List<Ingredient> getIngredientList() {
        return ingredientList;
    }

    public List<Ingredient> ingredientList;
    public int getId(){return this.id;}

    public CourseType getCourseType() {
        return courseType;
    }

    public void setCourseType(CourseType courseType) {
        this.courseType = courseType;
    }


    public Uri getImage() {
        return image;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String category) {
        this.categoryName = category;
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

    public Course(int id,CourseType courseType,int imageId ,Uri image, String name, int categoryId, String categoryName, String description, List<Steps> stepsList, List<Ingredient> ingredientList){
        this.id = id;
        this.courseType = courseType;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.image = image;
        this.name = name;
        this.description = description;
        this.stepsList = stepsList;
     //   getStepsToQueue();
        this.ingredientList = ingredientList;
        this.category = new Category(categoryId,categoryName);
        this.courseProgress = 0;
        this.isCurrentCourse = false;
//        this.stepsTotalTime = getStepsTotalTime();
        this.stepsTotalTime = 0;
        this.stepsCount = stepsList.size();
        this.maxStepsTime = this.stepsCount*MAX;
        this.imageId = imageId;

    }

    public void getStepsToQueue(){
        this.stepsGenQueue = new GenQueue<Steps>();
        for(Steps s: stepsList){
            s.currentStep = false;
                Steps temp = new Steps(s.stepNum,s.timeInmSeconds,s.description);
            this.stepsGenQueue.enqueue(temp);
        }
        this.stepsTotalTime = getStepsTotalTime();
        this.stepsCount = stepsList.size();
        this.maxStepsTime = this.stepsCount*MAX;
    }
    public List<Steps> getStepsList(){
        return stepsList;
    }

    public int getImageId(){
        return this.imageId;
    }
    public boolean isFinished(Steps currentStep ){
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
        if(currentStep != null){
            if(this.courseProgress < MAX*stepsCount){
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
