package com.example.yotam707.feedmeapp.data.remote;

import android.app.Activity;
import android.media.MediaPlayer;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class FirebaseAuthHandler {

    private static FirebaseAuth auth = FirebaseAuth.getInstance();

    public static void SignUpWithEmailAndPassword(String email, String password, final Activity activity, OnCompleteListener<AuthResult> onCompleteListener){
        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(activity, onCompleteListener);
    }


}
