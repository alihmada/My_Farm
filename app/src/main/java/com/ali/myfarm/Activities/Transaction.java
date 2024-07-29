package com.ali.myfarm.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.ali.myfarm.Adapters.FragmentViewPagerAdapter;
import com.ali.myfarm.Classes.Common;
import com.ali.myfarm.Dialogs.Alert;
import com.ali.myfarm.Fragments.Buyers;
import com.ali.myfarm.Fragments.Traders;
import com.ali.myfarm.R;
import com.google.android.material.tabs.TabLayout;

import java.util.Objects;

public class Transaction extends AppCompatActivity {

    private Handler handler;
    private String mainID, periodID;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);

        bundle = new Bundle();
        handler = new Handler(Looper.getMainLooper());
        mainID = Objects.requireNonNull(getIntent().getExtras()).getString(Common.YEAR);
        periodID = Objects.requireNonNull(getIntent().getExtras()).getString(Common.MONTH);

        initializeViews();
    }

    private void initializeViews() {
        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);
        initializeButtons();
        setupViewPager();
    }

    private void initializeButtons() {
        findViewById(R.id.back).setOnClickListener(view -> onBackPressed());

        findViewById(R.id.add).setOnClickListener(view -> {
            if (!Common.isFinished) {
                Intent intent = new Intent(this, NewTransactionOperation.class);
                bundle.putString(Common.YEAR, mainID);
                bundle.putString(Common.MONTH, periodID);
                intent.putExtras(bundle);
                startActivity(intent);
            } else {
                showAlert(getString(R.string.finished));
            }
        });

        findViewById(R.id.info).setOnClickListener(v -> new Handler(Looper.getMainLooper()).post(() -> {
            Intent intent = new Intent(this, TransactionInformation.class);
            bundle.putString(Common.YEAR, mainID);
            bundle.putString(Common.MONTH, periodID);
            intent.putExtras(bundle);
            startActivity(intent);
        }));
    }

    private void setupViewPager() {
        tabLayout.setupWithViewPager(viewPager);

        FragmentViewPagerAdapter ordersViewPagerAdapter = new FragmentViewPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        ordersViewPagerAdapter.addFragment(new Buyers(mainID, periodID), getString(R.string.buyers));
        ordersViewPagerAdapter.addFragment(new Traders(mainID, periodID), getString(R.string.traders));

        viewPager.setAdapter(ordersViewPagerAdapter);
        TabLayout.Tab die = tabLayout.getTabAt(1);
        if (die != null) die.select();
    }

    private void showAlert(String message) {
        handler.post(() -> new Alert(R.drawable.error, message).show(getSupportFragmentManager(), ""));
    }
}