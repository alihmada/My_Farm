package com.ali.myfarm.Activities;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.ali.myfarm.Adapters.FragmentViewPagerAdapter;
import com.ali.myfarm.Classes.Common;
import com.ali.myfarm.Classes.DateAndTime;
import com.ali.myfarm.Data.Firebase;
import com.ali.myfarm.Dialogs.FeedStatus;
import com.ali.myfarm.Fragments.Beginning;
import com.ali.myfarm.Fragments.End;
import com.ali.myfarm.Fragments.Growing;
import com.ali.myfarm.MVVM.FeedViewModel;
import com.ali.myfarm.Models.Bag;
import com.ali.myfarm.R;
import com.google.android.material.tabs.TabLayout;

import java.util.Objects;

public class Feed extends AppCompatActivity {

    Bundle bundle;
    TextView header;
    String mainID, periodID;
    FeedViewModel model;
    TabLayout tabLayout;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        bundle = new Bundle();
        header = findViewById(R.id.feed_header);
        mainID = Objects.requireNonNull(getIntent().getExtras()).getString(Common.MAIN_ID);
        periodID = Objects.requireNonNull(getIntent().getExtras()).getString(Common.PERIOD_ID);
        header.setText(String.format("%s - %s", periodID, mainID));
        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);

        setupViewModel();
        setupViewPager();
        initializeCardsText();
        initializeButtons();
    }

    private void initializeCardsText() {
        TextView growingTxt = findViewById(R.id.growing_txt);
        TextView beginningTxt = findViewById(R.id.initialize_txt);
        TextView endTxt = findViewById(R.id.end_txt);

        model.getFeed().observe(this, feed -> {
            growingTxt.setText(String.valueOf(feed.getGrowing()));
            beginningTxt.setText(String.valueOf(feed.getBeginning()));
            endTxt.setText(String.valueOf(feed.getEnd()));
        });
    }

    private void initializeButtons() {
        ImageButton record = findViewById(R.id.record);
        record.setOnClickListener(view -> {
            FeedStatus status = new FeedStatus((type, operation, numberOfBags, priceOfTon) -> {
                if (type == com.ali.myfarm.Models.Feed.Type.GROWING) {
                    Firebase.setGrowing(Feed.this, mainID, periodID, new Bag(numberOfBags, priceOfTon, operation, DateAndTime.getCurrentDateTime()), getSupportFragmentManager());
                } else if (type == com.ali.myfarm.Models.Feed.Type.BEGGING) {
                    Firebase.setInitialize(Feed.this, mainID, periodID, new Bag(numberOfBags, priceOfTon, operation, DateAndTime.getCurrentDateTime()), getSupportFragmentManager());
                } else {
                    Firebase.setEnd(Feed.this, mainID, periodID, new Bag(numberOfBags, priceOfTon, operation, DateAndTime.getCurrentDateTime()), getSupportFragmentManager());
                }
            });
            status.show(getSupportFragmentManager(), "");
        });

        ImageButton back = findViewById(R.id.back);
        back.setOnClickListener(view -> onBackPressed());
    }

    private void setupViewModel() {
        model = new ViewModelProvider(this).get(FeedViewModel.class);
        model.initialize(this, mainID, periodID);
    }

    private void setupViewPager() {
        tabLayout.setupWithViewPager(viewPager);

        FragmentViewPagerAdapter ordersViewPagerAdapter = new FragmentViewPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        ordersViewPagerAdapter.addFragment(new End(mainID, periodID), getString(R.string.over));
        ordersViewPagerAdapter.addFragment(new Beginning(mainID, periodID), getString(R.string.initialize));
        ordersViewPagerAdapter.addFragment(new Growing(mainID, periodID), getString(R.string.growing));

        viewPager.setAdapter(ordersViewPagerAdapter);
        TabLayout.Tab growing = tabLayout.getTabAt(2);
        if (growing != null) growing.select();
    }
}