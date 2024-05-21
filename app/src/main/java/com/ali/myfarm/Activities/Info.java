package com.ali.myfarm.Activities;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.ali.myfarm.Classes.Calculation;
import com.ali.myfarm.Classes.Common;
import com.ali.myfarm.R;

import java.util.Objects;

public class Info extends AppCompatActivity {

    String nDay;
    int numOfChicken;
    TextView day, water, feed, temp, darkness;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        setStatusBarColor();

        nDay = Objects.requireNonNull(getIntent().getExtras()).getString(Common.N_DAY);
        numOfChicken = Objects.requireNonNull(getIntent().getExtras()).getInt(Common.CHICKEN);

        initializeViews();
        setValues();

        ImageButton back = findViewById(R.id.back);
        back.setOnClickListener(view -> onBackPressed());
    }

    private void setStatusBarColor() {
        getWindow().setStatusBarColor(getTheme().getResources().getColor(R.color.stb));
    }

    private void initializeViews() {
        day = findViewById(R.id.period_day);
        water = findViewById(R.id.water_count);
        feed = findViewById(R.id.feed_count);
        temp = findViewById(R.id.temp);
        darkness = findViewById(R.id.dark);
    }

    private void setValues() {
        int index = Integer.parseInt(nDay);
        day.setText(nDay);
        water.setText(Calculation.needWater(numOfChicken, index));
        feed.setText(Calculation.needFeed(numOfChicken, index));
        temp.setText(Common.getTemperature(index - 1));
        darkness.setText(Common.getDurationOfDarkness(index - 1));
    }
}