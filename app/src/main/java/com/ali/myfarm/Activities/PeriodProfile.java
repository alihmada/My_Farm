package com.ali.myfarm.Activities;

import static com.ali.myfarm.Classes.Common.DECIMAL_REGEX;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.ali.myfarm.Classes.Calculation;
import com.ali.myfarm.Classes.Common;
import com.ali.myfarm.Classes.DateAndTime;
import com.ali.myfarm.Data.Firebase;
import com.ali.myfarm.Dialogs.Alert;
import com.ali.myfarm.Dialogs.Confirmation;
import com.ali.myfarm.Dialogs.ValueDialog;
import com.ali.myfarm.Models.Period;
import com.ali.myfarm.R;
import com.google.gson.Gson;

import java.util.Objects;

public class PeriodProfile extends AppCompatActivity {

    private Handler handler;
    private TextView end;
    private Period period;
    private String pastDays, year;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_period_profile);

        getExtrasData();
        initializeViews();
        initializeButtons();
        initializePrice();
    }

    private void getExtrasData() {
        period = new Gson().fromJson(Objects.requireNonNull(getIntent().getExtras()).getString(Common.PERIOD), Period.class);
        pastDays = Objects.requireNonNull(getIntent().getExtras()).getString(Common.N_DAY);
        year = Objects.requireNonNull(getIntent().getExtras()).getString(Common.YEAR);
    }

    private void initializeViews() {
        handler = new Handler(Looper.getMainLooper());

        TextView number = findViewById(R.id.period_number);
        number.setText(String.valueOf(period.getNumber()));

        TextView name = findViewById(R.id.period_name);
        name.setText(period.getName());

        TextView beginning = findViewById(R.id.beginning_date);
        beginning.setText(DateAndTime.extractDateOnly(period.getBeginningDate()));

        end = findViewById(R.id.end_date);
        if (!period.getEndDate().isEmpty())
            end.setText(DateAndTime.extractDateOnly(period.getEndDate()));
        else
            end.setText(getString(R.string.running));

        TextView days = findViewById(R.id.number_of_period_days);
        days.setText(pastDays);

        TextView chicken = findViewById(R.id.chicken_number);
        chicken.setText(String.valueOf(period.getNumberOfChicken()));

        TextView chickenPrice = findViewById(R.id.price_of_chicken);
        chickenPrice.setText(Calculation.getNumber(period.getChickenPrice()));
    }

    private void initializeButtons() {
        ImageButton back = findViewById(R.id.back);
        back.setOnClickListener(view -> onBackPressed());

        ImageButton report = findViewById(R.id.report);
        report.setOnClickListener(view -> {

        });

        Button endPeriod = findViewById(R.id.end_period);
        if (!Common.isFinished) endPeriod.setOnClickListener(view -> {
            Confirmation confirmation = new Confirmation(() -> {
                String end = DateAndTime.getCurrentDateTime();
                Firebase.getSpecificPeriod(PeriodProfile.this, year, period.getNumber()).child("endDate").setValue(end);

                Firebase.setRunningItemValue(PeriodProfile.this, false);

                this.end.setText(DateAndTime.extractDateOnly(end));
                endPeriod.setVisibility(View.GONE);
                Common.isFinished = true;
            });
            confirmation.show(getSupportFragmentManager(), "");
        });
        else endPeriod.setVisibility(View.GONE);
    }

    private void initializePrice() {
        ConstraintLayout price = findViewById(R.id.edit_chicken_price);
        TextView chickenPrice = findViewById(R.id.price_of_chicken);
        price.setOnClickListener(v -> {
            if (!Common.isFinished) {
                ValueDialog dialog = new ValueDialog(R.string.price_of_chicken, ValueDialog.inputType.DECIMAL, DECIMAL_REGEX, text -> {
                    chickenPrice.setText(text);
                    Firebase.getSpecificPeriod(PeriodProfile.this, year, period.getNumber()).child("chickenPrice").setValue(Double.parseDouble(text));
                });
                dialog.show(getSupportFragmentManager(), "");
            } else showAlert(getString(R.string.finished));
        });
    }

    private void showAlert(String message) {
        handler.post(() -> new Alert(R.drawable.error, message).show(getSupportFragmentManager(), ""));
    }
}