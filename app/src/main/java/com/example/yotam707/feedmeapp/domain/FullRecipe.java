package com.example.yotam707.feedmeapp.domain;
import java.util.List;

public class FullRecipe {
    private int id;
    private String title;
    private List<Ingredients> extendedIngredients;
    private String instructions;
    private List<AnalyzedInstructions> analyzedInstructions;
    private int readyInMinutes;



    public FullRecipe() {
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getReadyInMinutes() {
        return readyInMinutes;
    }

    public void setReadyInMinutes(int readyInMinutes) {
        this.readyInMinutes = readyInMinutes;
    }

    public List<Ingredients> getExtendedIngredients() {
        return extendedIngredients;
    }

    public void setExtendedIngredients(List<Ingredients> extendedIngredients) {
        this.extendedIngredients = extendedIngredients;
    }

    public List<AnalyzedInstructions> getAnalyzedInstructions() {
        return analyzedInstructions;
    }

    public void setAnalyzedInstructions(List<AnalyzedInstructions> analyzedInstructions) {
        this.analyzedInstructions = analyzedInstructions;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }
}
