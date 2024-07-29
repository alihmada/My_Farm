package com.ali.myfarm.Activities;

import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.ali.myfarm.Classes.Common;
import com.ali.myfarm.Fragments.TransactionInfo;
import com.ali.myfarm.MVVM.SoldViewModel;
import com.ali.myfarm.R;

import java.util.Objects;

public class TransactionInformation extends AppCompatActivity {

    private SoldViewModel model;
    private String year, month;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_information);

        getExtras();
        initializeViews();
        setupViewModel();
        getSold();
    }

    private void getExtras() {
        year = Objects.requireNonNull(getIntent().getExtras()).getString(Common.YEAR);
        month = getIntent().getExtras().getString(Common.MONTH);
    }

    private void initializeViews() {
        ImageButton back = findViewById(R.id.back);
        back.setOnClickListener(view -> onBackPressed());
    }

    private void setupViewModel() {
        model = new ViewModelProvider(this).get(SoldViewModel.class);
        model.initialize(this, year, month);
    }

    private void getSold() {
        model.getSold().observe(this, sold ->
                getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(
                                R.anim.slide_in_down,   // enter animation
                                R.anim.slide_out_down,  // exit animation
                                R.anim.slide_in_up,     // popEnter animation
                                R.anim.slide_out_up     // popExit animation
                        )
                        .replace(R.id.container,
                                new TransactionInfo(year, month, sold))
                        .commit());
    }
}