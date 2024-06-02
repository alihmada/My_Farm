package com.ali.myfarm.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.ali.myfarm.Adapters.FragmentViewPagerAdapter;
import com.ali.myfarm.Classes.Common;
import com.ali.myfarm.Data.Firebase;
import com.ali.myfarm.Dialogs.TransactionInfo;
import com.ali.myfarm.Fragments.Buyers;
import com.ali.myfarm.Fragments.Traders;
import com.ali.myfarm.MVVM.TransactionViewModel;
import com.ali.myfarm.R;
import com.google.android.material.tabs.TabLayout;

import java.util.Objects;

public class Transaction extends AppCompatActivity {

    private com.ali.myfarm.Models.Transaction transaction;
    private TransactionViewModel model;
    private String mainID, periodID;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);

        bundle = new Bundle();
        mainID = Objects.requireNonNull(getIntent().getExtras()).getString(Common.MAIN_ID);
        periodID = Objects.requireNonNull(getIntent().getExtras()).getString(Common.PERIOD_ID);

        initializeViews();
    }

    private void initializeViews() {
        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);
        initializeButtons();
        setupViewPager();
        setupViewModel();
        getTransaction();
    }

    private void initializeButtons() {
        findViewById(R.id.back).setOnClickListener(view -> onBackPressed());

        findViewById(R.id.add).setOnClickListener(view -> {
            Intent intent = new Intent(this, NewTransactionOperation.class);
            bundle.putString(Common.MAIN_ID, mainID);
            bundle.putString(Common.PERIOD_ID, periodID);
            intent.putExtras(bundle);
            startActivity(intent);
        });

        findViewById(R.id.info).setOnClickListener(v -> new Handler(Looper.getMainLooper()).post(() -> {
            TransactionInfo transactionInfo = new TransactionInfo(transaction);
            transactionInfo.show(getSupportFragmentManager(), "");
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

    private void setupViewModel() {
        model = new ViewModelProvider(this).get(TransactionViewModel.class);
        model.initialize(this, mainID, periodID);
    }

    private void getTransaction() {
        model.getTransaction().observe(this, transaction -> {
            if (transaction != null) {
                this.transaction = transaction;
            } else {
                Firebase.setTransactionBranchThatPeriodHave(Transaction.this,
                        mainID,
                        periodID,
                        new com.ali.myfarm.Models.Transaction(
                                0.0,
                                0.0,
                                0.0,
                                0.0
                        )
                );
            }
        });
    }
}