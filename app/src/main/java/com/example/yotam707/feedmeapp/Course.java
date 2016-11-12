package com.example.yotam707.feedmeapp;

import java.util.List;

/**
 * Created by yotam707 on 9/5/2016.
 */
public class Course {
    public int id;
    public int imageId;
    public String name;
    public Category category;
    public String description;
    public List<Steps> stepsList;
    public long courseProgress;
    public boolean isCurrentCourse;

    public List<Ingredient> getIngredientList() {
        return ingredientList;
    }

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

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Steps> getStepsList() {
        return stepsList;
    }

    public long getStepsTotalTime(){
        int count = 0;
        for(int i=0; i<stepsList.size(); i++){
            count+=stepsList.get(i).getTimeImSeconds();
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
        this.ingredientList = ingredientList;
        this.courseProgress = 0;
        this.isCurrentCourse = false;
    }

    public boolean isFinished() {
        for (Steps step : this.getStepsList()) {
            if (!step.isFinished()) {
                return false;
            }
        }
        return true;
    }

    public long getCourseProgress() {
        //int progress = 0;
        Steps currentStep = this.getCurrentStep();
        if(currentStep != null) {
            this.isCurrentCourse = true;
            for (Steps step : this.getStepsList()) {
                if (step.getStepNum() <= currentStep.getStepNum()) {
                    this.courseProgress += step.getProgress();
                } else {
                    break;
                }
            }
        }
        else{
            this.isCurrentCourse = false;
        }
        return this.courseProgress;
    }

    public Steps getCurrentStep() {
        for (Steps step : this.getStepsList()) {
            if (!step.isFinished()) {
                return step;
            }
        }
        return null;
    }
}
