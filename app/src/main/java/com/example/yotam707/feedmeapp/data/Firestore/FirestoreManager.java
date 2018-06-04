package com.example.yotam707.feedmeapp.data.Firestore;

import com.example.yotam707.feedmeapp.domain.AuthUser;
import com.example.yotam707.feedmeapp.domain.Equipment;
import com.example.yotam707.feedmeapp.domain.FullRecipe;
import com.example.yotam707.feedmeapp.domain.Ingredients;
import com.example.yotam707.feedmeapp.domain.Recipe;
import com.example.yotam707.feedmeapp.domain.UploadIngredient;
import com.example.yotam707.feedmeapp.domain.UploadEquipment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Source;

import java.util.List;

public class FirestoreManager {


    private static FirebaseFirestore fireStore = FirebaseFirestore.getInstance();

    public static void addNewUser(AuthUser user, OnSuccessListener onSuccessListener, OnFailureListener onFailureListener){
        fireStore.collection(FirestoreEnum.Users.USERS).document(user.getUid()).set(user).addOnSuccessListener(onSuccessListener).addOnFailureListener(onFailureListener);
    }

    public static void addNewRecipeByCuisine(String cuisine, List<Recipe> recipes, OnSuccessListener onSuccessListener, OnFailureListener onFailureListener){
        for(Recipe recipe: recipes) {
            fireStore.collection(cuisine+FirestoreEnum.Categories.CATEGORY).document().set(recipe).addOnSuccessListener(onSuccessListener).addOnFailureListener(onFailureListener);
        }
    }

    public static void addNewRecipe(String type, List<Recipe> recipes, OnSuccessListener onSuccessListener, OnFailureListener onFailureListener){
        for(Recipe recipe: recipes) {
            recipe.setType(type);
            fireStore.collection(type + FirestoreEnum.Recipes.RECIPES).document().set(recipe).addOnSuccessListener(onSuccessListener).addOnFailureListener(onFailureListener);
        }
    }

    public static void addNewFullRecipe(List<FullRecipe> fullRecipes, OnSuccessListener onSuccessListener, OnFailureListener onFailureListener){
        for(FullRecipe recipe: fullRecipes) {
            fireStore.collection(FirestoreEnum.FullRecipes.FULL_RECIPES).document(Integer.toString(recipe.getId())).set(recipe).addOnSuccessListener(onSuccessListener).addOnFailureListener(onFailureListener);
        }
    }

    public static void addNewIngredient(List<Ingredients> list, OnSuccessListener onSuccessListener, OnFailureListener onFailureListener){
        if(list.size() > 0) {
            for (Ingredients ing : list) {
                if (ing != null) {
                    UploadIngredient uploadIngredient = new UploadIngredient(ing.getId(),ing.getName(), ing.getImage());
                    fireStore.collection(FirestoreEnum.Ingredients.INGREDIENTS).document(Integer.toString(ing.getId())).set(uploadIngredient).addOnSuccessListener(onSuccessListener).addOnFailureListener(onFailureListener);
                }
            }
        }
    }

    public static void addNewEquipment(Equipment list, OnSuccessListener onSuccessListener, OnFailureListener onFailureListener){
        if (list != null) {
            UploadEquipment uploadEquipment = new UploadEquipment(list.getId(), list.getName(), list.getImage());
            fireStore.collection(FirestoreEnum.Equipment.EQUIPMENT).document(Integer.toString(list.getId())).set(uploadEquipment).addOnSuccessListener(onSuccessListener).addOnFailureListener(onFailureListener);
        }
    }

    public static void getEquipment(String equipmentId, OnCompleteListener onCompleteListener, OnFailureListener onFailureListener){
        fireStore.collection(FirestoreEnum.Equipment.EQUIPMENT).document(equipmentId).get().addOnFailureListener(onFailureListener).addOnCompleteListener(onCompleteListener);
    }

    public static void getAllMainCourseRecipes(OnCompleteListener onCompleteListener, OnFailureListener onFailureListener){
        fireStore.collection(FirestoreEnum.Recipes.MAIN_COURSE_RECIPES).get().addOnCompleteListener(onCompleteListener).addOnFailureListener(onFailureListener);
    }
    public static void getAllSideDishRecipes(OnCompleteListener onCompleteListener, OnFailureListener onFailureListener){
        fireStore.collection(FirestoreEnum.Recipes.SIDE_DISH_RECIPES).get().addOnCompleteListener(onCompleteListener).addOnFailureListener(onFailureListener);
    }

    public static void getAllAppetizerRecipes(OnCompleteListener onCompleteListener, OnFailureListener onFailureListener){
        fireStore.collection(FirestoreEnum.Recipes.APPETIZER_RECIPES).get().addOnCompleteListener(onCompleteListener).addOnFailureListener(onFailureListener);
    }

    public static void getAllDessertRecipes(OnCompleteListener onCompleteListener, OnFailureListener onFailureListener){
        fireStore.collection(FirestoreEnum.Recipes.DESSERT_RECIPES).get().addOnCompleteListener(onCompleteListener).addOnFailureListener(onFailureListener);
    }

    public static void getAllFullRecipes(OnCompleteListener onCompleteListener, OnFailureListener onFailureListener){
        fireStore.collection(FirestoreEnum.FullRecipes.FULL_RECIPES).get().addOnCompleteListener(onCompleteListener).addOnFailureListener(onFailureListener);
    }

    public static void getAfricanCategory(OnCompleteListener onCompleteListener, OnFailureListener onFailureListener){
        fireStore.collection(FirestoreEnum.Categories.AFRICAN).get().addOnCompleteListener(onCompleteListener).addOnFailureListener(onFailureListener);
    }
    public static void getAmericanCategory(OnCompleteListener onCompleteListener, OnFailureListener onFailureListener){
        fireStore.collection(FirestoreEnum.Categories.AMERICAN).get().addOnCompleteListener(onCompleteListener).addOnFailureListener(onFailureListener);
    }
    public static void getChineseategory(OnCompleteListener onCompleteListener, OnFailureListener onFailureListener){
        fireStore.collection(FirestoreEnum.Categories.CHINESE).get().addOnCompleteListener(onCompleteListener).addOnFailureListener(onFailureListener);
    }
    public static void getGreekCategory(OnCompleteListener onCompleteListener, OnFailureListener onFailureListener){
        fireStore.collection(FirestoreEnum.Categories.GREEK).get().addOnCompleteListener(onCompleteListener).addOnFailureListener(onFailureListener);
    }
    public static void getIndianCategory(OnCompleteListener onCompleteListener, OnFailureListener onFailureListener){
        fireStore.collection(FirestoreEnum.Categories.INDIAN).get().addOnCompleteListener(onCompleteListener).addOnFailureListener(onFailureListener);
    }
    public static void getItalianCategory(OnCompleteListener onCompleteListener, OnFailureListener onFailureListener){
        fireStore.collection(FirestoreEnum.Categories.ITALIAN).get().addOnCompleteListener(onCompleteListener).addOnFailureListener(onFailureListener);
    }
    public static void getMexicanCategory(OnCompleteListener onCompleteListener, OnFailureListener onFailureListener){
        fireStore.collection(FirestoreEnum.Categories.MEXICAN).get().addOnCompleteListener(onCompleteListener).addOnFailureListener(onFailureListener);
    }
    public static void getMiddleEastrenCategory(OnCompleteListener onCompleteListener, OnFailureListener onFailureListener){
        fireStore.collection(FirestoreEnum.Categories.MIDDLE_EASTERN).get().addOnCompleteListener(onCompleteListener).addOnFailureListener(onFailureListener);
    }
    public static void getThaiCategory(OnCompleteListener onCompleteListener, OnFailureListener onFailureListener){
        fireStore.collection(FirestoreEnum.Categories.THAI).get().addOnCompleteListener(onCompleteListener).addOnFailureListener(onFailureListener);
    }

//    public static void getAllCategories(OnCompleteListener onCompleteListener, OnFailureListener onFailureListener){
//        fireStore.collection(FirestoreEnum.Categories.CATEGORIES).document(FirestoreEnum.Categories.CATEGORIES_CUISINE).get().addOnCompleteListener(onCompleteListener).addOnFailureListener(onFailureListener);
//    }
//    public static void getAllCategoryRecipes(String category, OnCompleteListener onCompleteListener, OnFailureListener onFailureListener){
//        fireStore.collection(FirestoreEnum.Categories.CATEGORIES).document(FirestoreEnum.Categories.CATEGORIES_CUISINE).collection(category).get().addOnCompleteListener(onCompleteListener).addOnFailureListener(onFailureListener);
//    }

//    public static void addCategoryRecipe(String category, List<Recipe> recipes,OnCompleteListener onCompleteListener, OnFailureListener onFailureListener){
//        for(Recipe recipe: recipes) {
//            fireStore.collection(category + FirestoreEnum.Categories.CATEGORIES).document().set(recipe).addOnCompleteListener(onCompleteListener).addOnFailureListener(onFailureListener);
//        }
//    }


    public static void getCategoryList(OnCompleteListener onCompleteListener, OnFailureListener onFailureListener) {
        fireStore.collection("CATEGORIES").document("CategoryList").get().addOnCompleteListener(onCompleteListener).addOnFailureListener(onFailureListener);
    }
}
