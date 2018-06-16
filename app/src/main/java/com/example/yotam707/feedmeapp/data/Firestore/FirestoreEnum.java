package com.example.yotam707.feedmeapp.data.Firestore;

public class FirestoreEnum {
    public static class Users{
        public static final String USERS = "USERS";
    }

    public static class Recipes {
        public static final String RECIPES = "Recipes";
        public static final String APPETIZER_RECIPES = "AppetizerRecipes";
        public static final String DESSERT_RECIPES = "DessertRecipes";
        public static final String MAIN_COURSE_RECIPES = "MainCourseRecipes";
        public static final String SIDE_DISH_RECIPES = "SideDishRecipes";
    }

    public static class Ingredients{
        public static final String INGREDIENTS = "Ingredients";
    }

    public static class Equipment{
        public static final String EQUIPMENT = "Equipment";
    }

    public static class FullRecipes{
        public static final String FULL_RECIPES = "FullRecipes";
    }

    public static class SelectedRecipes {
        public static final String SELECTED_RECIPES = "SelectedRecipes";
        public static final String RECIPES = "Recipes";
    }

    public static class CookingRequests {
        public static final String COOKING_REQUESTS = "CookingRequests";
        public static final String RECIPES = "Recipes";
        public static final String SELECTED_RECIPES = "SelectedRecipes";
    }

    public static class Categories{
        public static  final String CATEGORY = "Category";
        public static final String CATEGORIES_CUISINE = "CategoriesCuisine";
        public static final String AFRICAN = "African"+CATEGORY;
        public static final String AMERICAN = "American"+CATEGORY;
        public static final String CHINESE = "Chinese"+CATEGORY;
        public static final String GREEK = "Greek" +CATEGORY;
        public static final String INDIAN = "Indian" + CATEGORY;
        public static final String ITALIAN = "Italian" + CATEGORY;
        public static final String MEXICAN = "Mexican" + CATEGORY;
        public static final String MIDDLE_EASTERN = "MiddleEastern"+CATEGORY;
        public static final String THAI = "Thai"+CATEGORY;
    }
}
