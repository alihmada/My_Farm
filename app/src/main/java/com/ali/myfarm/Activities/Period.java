package com.ali.myfarm.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.ali.myfarm.Classes.Common;
import com.ali.myfarm.Classes.DateAndTime;
import com.ali.myfarm.Fragments.First;
import com.ali.myfarm.Fragments.Second;
import com.ali.myfarm.Interfaces.OnMoreClickedListener;
import com.ali.myfarm.MVVM.PeriodViewModel;
import com.ali.myfarm.R;
import com.google.gson.Gson;

import java.util.Objects;

public class Period extends AppCompatActivity implements OnMoreClickedListener {

    private Bundle bundle;
    private PeriodViewModel model;
    private com.ali.myfarm.Models.Period period;
    private String year, month, pastDay;
    private TextView header, day;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_period);

        bundle = new Bundle();
        getExtras();
        setupFragments();
        initializeViews();
        setHead();
        setupOpenProfile();
        initializeButtons();
        setupViewModel();
        setupHeader();
    }

    private void getExtras() {
        year = Objects.requireNonNull(getIntent().getExtras()).getString(Common.YEAR);
        month = Objects.requireNonNull(getIntent().getExtras()).getString(Common.MONTH);
    }

    private void setHead() {
        header.setText(String.format("%s - %s", DateAndTime.getArabicNameOfMonth(month), year));
    }

    private void setupFragments() {
        First first = new First(year, month, this);

        replaceFragment(first);
    }

    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_down,   // enter animation
                R.anim.slide_out_down,  // exit animation
                R.anim.slide_in_up,     // popEnter animation
                R.anim.slide_out_up     // popExit animation
        ).replace(R.id.container, fragment).addToBackStack(null).commit();
    }

    private void initializeViews() {
        header = findViewById(R.id.period_head);
        day = findViewById(R.id.period_day);
    }

    private void initializeButtons() {
        ImageButton back = findViewById(R.id.back);
        back.setOnClickListener(view -> onBackPressed());

        ImageButton info = findViewById(R.id.info);
        info.setOnClickListener(view -> {
            Intent intent = new Intent(this, Info.class);
            bundle.putString(Common.N_DAY, pastDay);
            bundle.putInt(Common.CHICKEN, period.getNumberOfAliveChickens() - period.getNumberOfSold());
            intent.putExtras(bundle);
            startActivity(intent);
        });
    }

    private void setupOpenProfile() {
        TextView head = findViewById(R.id.period_head);
        TextView day = findViewById(R.id.period_day);

        View.OnClickListener listener = view -> {
            Intent intent = new Intent(this, PeriodProfile.class);
            bundle.putString(Common.PERIOD, new Gson().toJson(period));
            bundle.putString(Common.N_DAY, pastDay);
            bundle.putString(Common.YEAR, year);
            intent.putExtras(bundle);
            startActivity(intent);
        };

        head.setOnClickListener(listener);
        day.setOnClickListener(listener);
    }

    private void setupViewModel() {
        model = new ViewModelProvider(this).get(PeriodViewModel.class);
        model.initialize(this, year, month);
    }

    private void setupHeader() {
        model.getPeriod().observe(this, period -> {
            this.period = period;

            String endDate = period.getEndDate();
            String beginningDate = period.getBeginningDate();

            if (endDate != null && !endDate.isEmpty()) {
                pastDay = DateAndTime.getPastDays(endDate, beginningDate);
                Common.isFinished = true;
            } else {
                pastDay = DateAndTime.getPastDays(DateAndTime.getCurrentDateTime(), beginningDate);
                Common.isFinished = false;
            }

            day.setText(pastDay);
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 1) {
            fragmentManager.popBackStack();
        } else if (fragmentManager.getBackStackEntryCount() == 0) {
            finish();
        }
    }

    @Override
    public void onMoreClicked() {
        Second second = new Second(year, month);
        replaceFragment(second);
    }
}