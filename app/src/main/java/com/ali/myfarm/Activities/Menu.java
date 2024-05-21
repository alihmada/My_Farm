package com.ali.myfarm.Activities;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.ali.myfarm.Classes.Ciphering;
import com.ali.myfarm.Classes.Common;
import com.ali.myfarm.Classes.PhoneNumber;
import com.ali.myfarm.Classes.QRConstructor;
import com.ali.myfarm.Dialogs.Qr;
import com.ali.myfarm.Models.User;
import com.ali.myfarm.R;
import com.google.gson.Gson;

public class Menu extends AppCompatActivity {
    private Bitmap qr;
    private Handler handler;
    private SharedPreferences sharedPreferences;
    private ConstraintLayout traders, buyers, debts, edit, logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        handler = new Handler(Looper.getMainLooper());

        createQr();
        initializeViews();
        initializeButtons();
        setupClickListeners();
        initializeSharedPreferences();

        User user = getUserFromPreferences();

        if (user != null) {
            setupUserInfo(user);
        }
    }

    private void createQr() {
        handler.post(() -> qr = QRConstructor.createQR(Common.getROOT()));
    }

    private void initializeSharedPreferences() {
        try {
            sharedPreferences = getSharedPreferences(Ciphering.decrypt(Common.SHARED_PREFERENCE_NAME), MODE_PRIVATE);
        } catch (Exception ignored) {
        }
    }

    private void initializeViews() {
        traders = findViewById(R.id.traders);
        buyers = findViewById(R.id.buyers);
        debts = findViewById(R.id.debts);
        edit = findViewById(R.id.edit);
        logout = findViewById(R.id.logout);
    }

    private void initializeButtons() {
        ImageButton back = findViewById(R.id.back);
        back.setOnClickListener(view -> onBackPressed());

        ImageButton qrCode = findViewById(R.id.qr_code);
        qrCode.setOnClickListener(view -> handler.post(() -> {
            Qr qr = new Qr(this.qr);
            qr.show(getSupportFragmentManager(), "");
        }));
    }

    private User getUserFromPreferences() {
        Gson gson = new Gson();
        String userData = sharedPreferences.getString(Common.USER_DATA, "");
        return gson.fromJson(userData, User.class);
    }

    private void setupUserInfo(User user) {
        TextView userName = findViewById(R.id.user_name);
        userName.setText(String.format("%s %s", user.getFirstName(), user.getLastName()));

        TextView phoneNumber = findViewById(R.id.user_phone);
        phoneNumber.setText(PhoneNumber.formatPhoneNumber(user.getPhoneNumber()));
    }

    private void setupClickListeners() {
        traders.setOnClickListener(v -> {
            Toast.makeText(Menu.this, "Traders", Toast.LENGTH_SHORT).show();
        });

        buyers.setOnClickListener(v -> {
            Toast.makeText(Menu.this, "Buyers", Toast.LENGTH_SHORT).show();
        });

        debts.setOnClickListener(v -> {
            Toast.makeText(Menu.this, "Debts", Toast.LENGTH_SHORT).show();
        });

        edit.setOnClickListener(v -> {
            Toast.makeText(Menu.this, "Edit", Toast.LENGTH_SHORT).show();
        });

        logout.setOnClickListener(v -> {
            Toast.makeText(Menu.this, "Logout", Toast.LENGTH_SHORT).show();
        });
    }
}