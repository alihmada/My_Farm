package com.ali.myfarm.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.ali.myfarm.Classes.Ciphering;
import com.ali.myfarm.Classes.Common;
import com.ali.myfarm.Data.Firebase;
import com.ali.myfarm.Models.Farm;
import com.ali.myfarm.Models.User;
import com.ali.myfarm.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class Splash extends AppCompatActivity {

    private static final long SPLASH_DELAY_MS = 250;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        try {
            sharedPreferences = getSharedPreferences(Ciphering.decrypt(Common.SHARED_PREFERENCE_NAME), MODE_PRIVATE);
        } catch (Exception ignored) {
        }

        redirectToAppropriateScreenWithDelay();
    }

    private void redirectToAppropriateScreenWithDelay() {
        new Handler().postDelayed(this::redirectToAppropriateScreen, SPLASH_DELAY_MS);
    }

    private void redirectToAppropriateScreen() {
        String user = sharedPreferences.getString(Common.USER_DATA, "");
        String root = sharedPreferences.getString(Common.FARM_ID, "");

        if (!user.isEmpty() && !root.isEmpty()) {
            Common.setROOT(root);
            updateUserData();
            redirectToMainScreen();
            setFirebaseKeepSynced();
        } else {
            redirectToLoginScreen();
        }
    }

    private void redirectToMainScreen() {
        Intent intent = new Intent(this, Main.class);
        startActivity(intent);
        finish();
    }

    private void redirectToLoginScreen() {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
        finish();
    }

    private void setFirebaseKeepSynced() {
        Firebase.getRoot(this).keepSynced(true);
    }

    private void updateUserData() {
        Query query = Firebase.getUsers(this).orderByChild("phoneNumber").equalTo(Firebase.getPhoneNumber());

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        sharedPreferences.edit().putString(Common.USER_DATA, new Gson().toJson(dataSnapshot.getValue(User.class))).apply();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}