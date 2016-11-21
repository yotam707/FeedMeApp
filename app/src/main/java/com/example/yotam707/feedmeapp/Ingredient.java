package com.example.yotam707.feedmeapp;

/**
 * Created by yotam707 on 9/17/2016.
 */
public class Ingredient {
    public int courseId;
    public int ingredientId;
    public String name;
    public int quantity;

    public Ingredient(int courseId, int ingredientId, String name, int quantity) {
        this.courseId = courseId;
        this.ingredientId = ingredientId;
        this.name = name;
        this.quantity = quantity;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public int getId() {
        return ingredientId;
    }

    public void setId(int ingredientId) {
        this.ingredientId = ingredientId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
