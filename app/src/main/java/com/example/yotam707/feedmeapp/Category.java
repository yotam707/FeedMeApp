package com.example.yotam707.feedmeapp;

/**
 * Created by yotam707 on 9/10/2016.
 */
public class Category implements Comparable<Category> {
    private int id;
    private String name;


    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Category(int id, String name) {

        this.id = id;
        this.name = name;
    }

    @Override
    public int compareTo(Category category) {
        return (this).compareTo(category);
    }
}
