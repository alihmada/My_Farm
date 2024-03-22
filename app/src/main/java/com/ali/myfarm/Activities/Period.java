package com.ali.myfarm.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.ali.myfarm.Classes.Common;
import com.ali.myfarm.MVVM.PeriodViewMadel;
import com.ali.myfarm.Models.Medicine;
import com.ali.myfarm.R;
import com.google.android.material.card.MaterialCardView;

import java.util.Objects;

public class Period extends AppCompatActivity {

    Bundle bundle;
    PeriodViewMadel model;
    String mainID, periodID;
    TextView header, numberOfChickens, feedCount;
    MaterialCardView chicks, feed, medicine, more;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_period);

        initializeViews();
        initializeButtons();
        bundle = new Bundle();
        mainID = Objects.requireNonNull(getIntent().getExtras()).getString(Common.MAIN_ID);
        periodID = Objects.requireNonNull(getIntent().getExtras()).getString(Common.PERIOD_ID);
        header.setText(String.format("%s - %s", periodID, mainID));
        setupViewModel();
        setupChickens();
        setupFeed();
        //setupMedicine();
        setupMore();
        viewHandler();
    }

    private void initializeViews() {
        header = findViewById(R.id.period_head);
        chicks = findViewById(R.id.chicken);
        feed = findViewById(R.id.feed);
        medicine = findViewById(R.id.medicine);
        more = findViewById(R.id.more);
    }

    private void initializeButtons() {
        ImageButton back = findViewById(R.id.back);
        back.setOnClickListener(view -> {
            onBackPressed();
        });

        ImageButton info = findViewById(R.id.info);
        info.setOnClickListener(view -> {

        });
    }

    private void setupChickens() {
        chicks.setOnClickListener(view -> {
            Intent intent = new Intent(this, Chicken.class);
            bundle.putString(Common.MAIN_ID, mainID);
            bundle.putString(Common.PERIOD_ID, periodID);
            intent.putExtras(bundle);
            startActivity(intent);
        });

        numberOfChickens = findViewById(R.id.chicken_number);
    }

    private void setupFeed() {
        feed.setOnClickListener(view -> {
            Intent intent = new Intent(this, Feed.class);
            bundle.putString(Common.MAIN_ID, mainID);
            bundle.putString(Common.PERIOD_ID, periodID);
            intent.putExtras(bundle);
            startActivity(intent);
        });

        feedCount = findViewById(R.id.feed_count);
    }

    private void setupMedicine() {
        medicine.setOnClickListener(view -> {
            Intent intent = new Intent(this, Medicine.class);
            startActivity(intent);
        });
    }

    private void setupMore() {
        more.setOnClickListener(view -> {
            bundle.putString(Common.MAIN_ID, mainID);
            bundle.putString(Common.PERIOD_ID, periodID);

            Intent intent = new Intent(this, More.class);
            intent.putExtras(bundle);
            startActivity(intent);
        });
    }


    private void setupViewModel() {
        model = new ViewModelProvider(this).get(PeriodViewMadel.class);
        model.initialize(this, mainID, periodID);
    }

    private void viewHandler() {
        model.getPeriod().observe(this, period -> {
            numberOfChickens.setText(String.valueOf(period.getNumberOfAliveChickens()));
            feedCount.setText(String.valueOf(period.getNumberOfFeedBags()));
        });
    }
}