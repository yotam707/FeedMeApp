package com.example.yotam707.feedmeapp.domain;

import java.util.ArrayList;
import java.util.List;

public class FullRecipeList {
    private ArrayList<FullRecipe> recipes;

    public FullRecipeList() {
    }
    public List<FullRecipe> getRecipes() {
        return recipes;
    }

    public void setRecipes(ArrayList<FullRecipe> recipes) {
        this.recipes = recipes;
    }
}
