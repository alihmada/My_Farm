package com.ali.myfarm.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.ali.myfarm.Classes.Common;
import com.ali.myfarm.R;
import com.google.android.material.card.MaterialCardView;

import java.util.Objects;

public class More extends AppCompatActivity {
    Bundle bundle;
    String mainID, periodID, nDay;
    int numOfChickens;
    TextView header, day;
    MaterialCardView transactions, heating, electricity, report;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);

        initializeViews();
        initializeButtons();
        bundle = new Bundle();
        mainID = Objects.requireNonNull(getIntent().getExtras()).getString(Common.MAIN_ID);
        periodID = Objects.requireNonNull(getIntent().getExtras()).getString(Common.PERIOD_ID);
        nDay = Objects.requireNonNull(getIntent().getExtras()).getString(Common.N_DAY);
        numOfChickens = Objects.requireNonNull(getIntent().getExtras()).getInt(Common.CHICKEN);
        header.setText(String.format("%s - %s", periodID, mainID));
        day.setText(nDay);
        setupHeating();
        setupElectricity();
        setupTransaction();
    }

    private void initializeViews() {
        header = findViewById(R.id.period_head);
        day = findViewById(R.id.period_day);
        heating = findViewById(R.id.fire);
        electricity = findViewById(R.id.energy);
        transactions = findViewById(R.id.transaction);
    }

    private void initializeButtons() {
        ImageButton back = findViewById(R.id.back);
        back.setOnClickListener(view -> onBackPressed());

        ImageButton info = findViewById(R.id.info);
        info.setOnClickListener(view -> {
            Intent intent = new Intent(this, Info.class);
            bundle.putString(Common.N_DAY, Objects.equals(nDay, "0") ? "1" : nDay);
            bundle.putInt(Common.CHICKEN, numOfChickens);
            intent.putExtras(bundle);
            startActivity(intent);
        });
    }

    private void setupHeating() {
        heating.setOnClickListener(view -> {
            Intent intent = new Intent(this, Heating.class);
            bundle.putString(Common.MAIN_ID, mainID);
            bundle.putString(Common.PERIOD_ID, periodID);
            intent.putExtras(bundle);
            startActivity(intent);
        });
    }

    private void setupElectricity() {
        electricity.setOnClickListener(view -> {
            Intent intent = new Intent(this, Electricity.class);
            bundle.putString(Common.MAIN_ID, mainID);
            bundle.putString(Common.PERIOD_ID, periodID);
            intent.putExtras(bundle);
            startActivity(intent);
        });
    }

    private void setupTransaction() {
        transactions.setOnClickListener(view -> {
            Intent intent = new Intent(this, Transaction.class);
            bundle.putString(Common.MAIN_ID, mainID);
            bundle.putString(Common.PERIOD_ID, periodID);
            intent.putExtras(bundle);
            startActivity(intent);
        });
    }
}