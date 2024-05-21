package com.ali.myfarm.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.ali.myfarm.Adapters.FragmentViewPagerAdapter;
import com.ali.myfarm.Classes.Common;
import com.ali.myfarm.Fragments.Buyers;
import com.ali.myfarm.Fragments.Traders;
import com.ali.myfarm.R;
import com.google.android.material.tabs.TabLayout;

import java.util.Objects;

public class Transaction extends AppCompatActivity {

    String mainID, periodID;
    TabLayout tabLayout;
    ViewPager viewPager;
    Bundle bundle;

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
    }

    private void initializeButtons() {
        ImageButton back = findViewById(R.id.back);
        back.setOnClickListener(view -> onBackPressed());

        ImageButton add = findViewById(R.id.add);
        add.setOnClickListener(view -> {
            Intent intent = new Intent(this, NewTransactionOperation.class);
            bundle.putString(Common.MAIN_ID, mainID);
            bundle.putString(Common.PERIOD_ID, periodID);
            intent.putExtras(bundle);
            startActivity(intent);
        });
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
}