package com.example.yotam707.feedmeapp.data.Firestore;

import com.example.yotam707.feedmeapp.domain.AuthUser;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirestoreManager {
    private static final String USERS = "USERS";
    private static final String RECIPES = "RECIPES";
    private static FirebaseFirestore fireStore = FirebaseFirestore.getInstance();

    public static void addNewUser(AuthUser user, OnSuccessListener onSuccessListener, OnFailureListener onFailureListener){
        fireStore.collection(USERS).document(user.getUid()).set(user).addOnSuccessListener(onSuccessListener).addOnFailureListener(onFailureListener);
    }

    public static void addNewRecipe(){

    }




}
