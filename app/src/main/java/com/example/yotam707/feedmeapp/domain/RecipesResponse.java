package com.example.yotam707.feedmeapp.domain;

import java.util.ArrayList;
import java.util.List;

public class RecipesResponse {
    List<Recipe> recipeList;

    public RecipesResponse(){
        recipeList = new ArrayList<Recipe>();
    }
}
