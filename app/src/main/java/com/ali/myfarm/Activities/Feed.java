package com.ali.myfarm.Activities;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.ali.myfarm.Adapters.FragmentViewPagerAdapter;
import com.ali.myfarm.Classes.Common;
import com.ali.myfarm.Classes.DateAndTime;
import com.ali.myfarm.Classes.Vibrate;
import com.ali.myfarm.Data.Firebase;
import com.ali.myfarm.Dialogs.FeedStatus;
import com.ali.myfarm.Dialogs.PopupFeed;
import com.ali.myfarm.Fragments.Beginning;
import com.ali.myfarm.Fragments.End;
import com.ali.myfarm.Fragments.Growing;
import com.ali.myfarm.MVVM.BeginningViewModel;
import com.ali.myfarm.MVVM.EndViewModel;
import com.ali.myfarm.MVVM.FeedViewModel;
import com.ali.myfarm.MVVM.GrowingViewModel;
import com.ali.myfarm.Models.Bag;
import com.ali.myfarm.R;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.tabs.TabLayout;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class Feed extends AppCompatActivity {

    Bundle bundle;
    TextView header;
    String mainID, periodID;
    FeedViewModel model;
    GrowingViewModel growingViewModel;
    BeginningViewModel beginningViewModel;
    EndViewModel endViewModel;
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
        setupGrowingViewModel();
        setupInitialViewModel();
        setupEndViewModel();
        setupViewPager();
        initializeCardsText();
        initializeButtons();
    }

    private void initializeCardsText() {
        AtomicInteger growing = new AtomicInteger();
        AtomicInteger initialize = new AtomicInteger();
        AtomicInteger end = new AtomicInteger();

        TextView growingTxt = findViewById(R.id.growing_txt);
        TextView beginningTxt = findViewById(R.id.initialize_txt);
        TextView endTxt = findViewById(R.id.end_txt);

        model.getFeed().observe(this, feed -> {
            growing.set(feed.getGrowing());
            growingTxt.setText(String.valueOf(growing.get()));
            initialize.set(feed.getBeginning());
            beginningTxt.setText(String.valueOf(initialize));
            end.set(feed.getEnd());
            endTxt.setText(String.valueOf(end.get()));
        });

        initializeCardsOnLongClick(growing, initialize, end);
    }

    private void initializeCardsOnLongClick(AtomicInteger growing, AtomicInteger initialize, AtomicInteger end) {
        MaterialCardView growingCard = findViewById(R.id.growing);
        MaterialCardView initializeCard = findViewById(R.id.initiator);
        MaterialCardView endCard = findViewById(R.id.end);

        View.OnLongClickListener onLongClickListener = v -> {
            if (v.getId() == R.id.growing) {
                growingViewModel.getGrowing().observe(Feed.this, bags -> handelFeedDetails(bags, growing, R.drawable.growing));
            } else if (v.getId() == R.id.initiator) {
                beginningViewModel.getBeginning().observe(Feed.this, bags -> handelFeedDetails(bags, initialize, R.drawable.initialize));
            } else {
                endViewModel.getEnd().observe(Feed.this, bags -> handelFeedDetails(bags, end, R.drawable.end));
            }
            return false;
        };

        growingCard.setOnLongClickListener(onLongClickListener);
        initializeCard.setOnLongClickListener(onLongClickListener);
        endCard.setOnLongClickListener(onLongClickListener);
    }

    private void handelFeedDetails(List<Bag> bags, AtomicInteger exist, int image) {
        AtomicInteger total = new AtomicInteger();

        if (bags != null) {
            for (Bag bag : bags)
                if (bag.getOperation() == Bag.Operation.ADD) total.addAndGet(bag.getNumber());
        }
        new Handler(Looper.getMainLooper()).post(() -> {
            PopupFeed popupFeed = new PopupFeed(image, String.valueOf(total.get()), String.valueOf(total.get() - exist.get()), String.valueOf(exist.get()));
            popupFeed.show(getSupportFragmentManager(), "");
            Vibrate.vibrate(Feed.this);
        });
    }

    private void initializeButtons() {
        ImageButton record = findViewById(R.id.record);
        record.setOnClickListener(view -> new Handler(Looper.getMainLooper()).post(() -> {
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
        }));

        ImageButton back = findViewById(R.id.back);
        back.setOnClickListener(view -> onBackPressed());
    }

    private void setupViewModel() {
        model = new ViewModelProvider(this).get(FeedViewModel.class);
        model.initialize(this, mainID, periodID);
    }

    private void setupGrowingViewModel() {
        growingViewModel = new ViewModelProvider(this).get(GrowingViewModel.class);
        growingViewModel.initialize(this, mainID, periodID);
    }

    private void setupInitialViewModel() {
        beginningViewModel = new ViewModelProvider(this).get(BeginningViewModel.class);
        beginningViewModel.initialize(this, mainID, periodID);
    }

    private void setupEndViewModel() {
        endViewModel = new ViewModelProvider(this).get(EndViewModel.class);
        endViewModel.initialize(this, mainID, periodID);
    }

    private void setupViewPager() {
        tabLayout.setupWithViewPager(viewPager);

        FragmentViewPagerAdapter ordersViewPagerAdapter = new FragmentViewPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        ordersViewPagerAdapter.addFragment(new End(mainID, periodID), getString(R.string.over));
        ordersViewPagerAdapter.addFragment(new Beginning(mainID, periodID), getString(R.string.growing));
        ordersViewPagerAdapter.addFragment(new Growing(mainID, periodID), getString(R.string.initialize));

        viewPager.setAdapter(ordersViewPagerAdapter);
        TabLayout.Tab growing = tabLayout.getTabAt(2);
        if (growing != null) growing.select();
    }
}