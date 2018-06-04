package com.example.yotam707.feedmeapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.example.yotam707.feedmeapp.data.Firestore.FirestoreManager;
import com.example.yotam707.feedmeapp.domain.AuthUser;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.AuthUI.IdpConfig;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.protobuf.Api;

import java.util.Arrays;
import java.util.List;

public class AuthUiActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 123;
    List<IdpConfig> providers = Arrays.asList(
            new AuthUI.IdpConfig.EmailBuilder().build(),
            new AuthUI.IdpConfig.FacebookBuilder().build(),
            new AuthUI.IdpConfig.GoogleBuilder().build());


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_ui);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        if(auth.getCurrentUser() != null){
            startActivity(new Intent(this, ApiActivity.class));
            finish();
        }
        else{
            showSignInScreen();
        }
    }

    private void showSignInScreen() {
        startActivityForResult(
                AuthUI.getInstance().
                createSignInIntentBuilder()
                .setTheme(R.style.AppTheme)
                        .setLogo(R.drawable.feed_me_title)
                        .setAvailableProviders(providers)
                        .build(), RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_SIGN_IN) {
            final IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                AuthUser authUser = new AuthUser();
                authUser.setUid(user.getUid());
                authUser.setEmail(user.getEmail());
                authUser.setDisplayName(user.getDisplayName());

                //add user to db
                FirestoreManager.addNewUser(authUser, new OnSuccessListener() {
                    @Override
                    public void onSuccess(Object o) {
                        startActivity(new Intent(AuthUiActivity.this, ApiActivity.class));
                    }
                }, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AuthUiActivity.this, "ERROR" +e.toString() +"error response: "+ response.getError(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }
}
