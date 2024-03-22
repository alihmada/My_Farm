package com.ali.myfarm.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.ali.myfarm.Classes.Common;
import com.ali.myfarm.Data.Firebase;
import com.ali.myfarm.Dialogs.HeatingDialog;
import com.ali.myfarm.R;
import com.google.android.material.card.MaterialCardView;

import java.util.Objects;

public class More extends AppCompatActivity {
    Bundle bundle;
    String mainID, periodID;
    TextView header;
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
        header.setText(String.format("%s - %s", periodID, mainID));
        setupHeating();
    }

    private void initializeViews() {
        header = findViewById(R.id.period_head);
        heating = findViewById(R.id.fire);

    }

    private void initializeButtons() {
        ImageButton back = findViewById(R.id.back);
        back.setOnClickListener(view -> onBackPressed());
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
}