package com.ali.myfarm.Activities;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.ali.myfarm.Adapters.FragmentViewPagerAdapter;
import com.ali.myfarm.Classes.Common;
import com.ali.myfarm.Classes.DateAndTime;
import com.ali.myfarm.Data.Firebase;
import com.ali.myfarm.Dialogs.Alert;
import com.ali.myfarm.Dialogs.ChickenStatus;
import com.ali.myfarm.Fragments.Die;
import com.ali.myfarm.Fragments.Sales;
import com.ali.myfarm.MVVM.PeriodViewModel;
import com.ali.myfarm.Models.Period;
import com.ali.myfarm.R;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class Chicken extends AppCompatActivity {

    private Period period;
    private Handler handler;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private PeriodViewModel model;
    private String mainID, periodID;
    private TextView all;
    private TextView alive;
    private TextView dead;
    private TextView sold;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chicken);

        getBundleData();
        initializeViews();
    }

    private void getBundleData() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mainID = extras.getString(Common.MAIN_ID);
            periodID = extras.getString(Common.PERIOD_ID);
        }
    }

    private void initializeViews() {
        handler = new Handler(Looper.getMainLooper());
        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);
        initializeTextViews();
        initializeButtons();
        setupViewPager();
        setupViewModel();
        viewHandler();
    }

    private void initializeTextViews() {
        TextView header = findViewById(R.id.period_head);
        header.setText(String.format("%s - %s", periodID, mainID));
        all = findViewById(R.id.total_number);
        alive = findViewById(R.id.alive_txt);
        dead = findViewById(R.id.dead);
        sold = findViewById(R.id.sold);
    }

    private void initializeButtons() {
        ImageButton back = findViewById(R.id.back);
        back.setOnClickListener(view -> onBackPressed());

        ImageButton record = findViewById(R.id.record);
        record.setOnClickListener(view -> handler.post(this::handleRecordClick));
    }

    private void handleRecordClick() {
        ChickenStatus status = new ChickenStatus(dead -> {
            int existing = period.getNumberOfAliveChickens() - period.getNumberOfSold() - dead;
            if (existing >= 0) {
                saveChickenData(period.getNumberOfAliveChickens(), dead);
                updateNumberOfDead(dead);
            } else {
                showAlert(getString(R.string.no_enough_chicken));
            }
        });
        status.show(getSupportFragmentManager(), "");
    }

    private void saveChickenData(int aliveChickens, int dead) {
        Firebase.getChicken(this, mainID, periodID).push().setValue(new com.ali.myfarm.Models.Chicken(aliveChickens, dead, DateAndTime.getCurrentDateTime()));
    }

    private void updateNumberOfDead(int dead) {
        Firebase.getSpecificPeriod(this, mainID, periodID).child(Common.NUMBER_OF_DEAD).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Integer currentDead = snapshot.getValue(Integer.class);
                if (currentDead != null) {
                    Firebase.getSpecificPeriod(Chicken.this, mainID, periodID).child(Common.NUMBER_OF_DEAD).setValue(currentDead + dead);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("updateNumberOfDead", "Database operation cancelled", error.toException());
            }
        });
    }

    private void showAlert(String message) {
        handler.post(() -> new Alert(R.drawable.error, message).show(getSupportFragmentManager(), ""));
    }

    private void setupViewPager() {
        tabLayout.setupWithViewPager(viewPager);

        FragmentViewPagerAdapter ordersViewPagerAdapter = new FragmentViewPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        ordersViewPagerAdapter.addFragment(new Sales(mainID, periodID), getString(R.string.sold));
        ordersViewPagerAdapter.addFragment(new Die(mainID, periodID), getString(R.string.die));

        viewPager.setAdapter(ordersViewPagerAdapter);
        TabLayout.Tab die = tabLayout.getTabAt(1);
        if (die != null) die.select();
    }

    private void setupViewModel() {
        model = new ViewModelProvider(this).get(PeriodViewModel.class);
        model.initialize(this, mainID, periodID);
    }

    private void viewHandler() {
        model.getPeriod().observe(this, period -> {
            this.period = period;
            all.setText(String.valueOf(period.getNumberOfChicken()));
            alive.setText(String.valueOf(period.getNumberOfAliveChickens()));
            dead.setText(String.valueOf(period.getNumberOfDead()));
            sold.setText(String.valueOf(period.getNumberOfSold()));
        });
    }
}