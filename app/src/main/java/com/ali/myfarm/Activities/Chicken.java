package com.ali.myfarm.Activities;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
import com.ali.myfarm.Dialogs.ChickenStatus;
import com.ali.myfarm.Fragments.Die;
import com.ali.myfarm.Fragments.Sales;
import com.ali.myfarm.MVVM.PeriodViewMadel;
import com.ali.myfarm.Models.Period;
import com.ali.myfarm.R;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class Chicken extends AppCompatActivity {
    Period period;
    TabLayout tabLayout;
    ViewPager viewPager;
    PeriodViewMadel model;
    String mainID, periodID;
    TextView header, all, alive, dead, sold;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chicken);

        getBundleData();
        initializeViews();
    }

    private void getBundleData() {
        mainID = Objects.requireNonNull(getIntent().getExtras()).getString(Common.MAIN_ID);
        periodID = Objects.requireNonNull(getIntent().getExtras()).getString(Common.PERIOD_ID);
    }

    private void initializeViews() {
        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);
        initializeTextViews();
        initializeButtons();
        setupViewPager();
        setupViewModel();
        viewHandler();
    }

    private void initializeTextViews() {
        header = findViewById(R.id.period_head);
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
        record.setOnClickListener(view -> new Handler(Looper.getMainLooper()).post(() -> {
            ChickenStatus status = new ChickenStatus(dead -> {
                Firebase
                        .getChicken(
                                Chicken.this,
                                mainID,
                                periodID
                        )
                        .push()
                        .setValue(
                                new com.ali.myfarm.Models.Chicken(
                                        period.getNumberOfAliveChickens(),
                                        dead,
                                        DateAndTime.getCurrentDateTime()
                                )
                        );

                Firebase.getSpecificPeriod(Chicken.this, mainID, periodID).child(Common.NUMBER_OF_DEAD).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Firebase.getSpecificPeriod(Chicken.this, mainID, periodID).child(Common.NUMBER_OF_DEAD).setValue(snapshot.getValue(int.class) + dead);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            });
            status.show(getSupportFragmentManager(), "");
        }));
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
        model = new ViewModelProvider(this).get(PeriodViewMadel.class);
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