package com.example.yotam707.feedmeapp.domain;

import java.util.ArrayList;
import java.util.List;

public class FullRecipesCookingRequest {

    private ArrayList<FullRecipe> fullRecipes;
    public FullRecipesCookingRequest(){

    }

    public ArrayList<FullRecipe> getFullRecipes() {
        return fullRecipes;
    }

    public void setFullRecipes(ArrayList<FullRecipe> fullRecipes) {
        this.fullRecipes = fullRecipes;
    }
}
