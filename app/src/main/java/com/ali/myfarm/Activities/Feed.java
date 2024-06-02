package com.ali.myfarm.Activities;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.ali.myfarm.Adapters.FragmentViewPagerAdapter;
import com.ali.myfarm.Classes.Common;
import com.ali.myfarm.Classes.DateAndTime;
import com.ali.myfarm.Classes.Vibrate;
import com.ali.myfarm.Data.Firebase;
import com.ali.myfarm.Dialogs.Alert;
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

    private TextView header;
    private Handler handler;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private FeedViewModel feedViewModel;
    private String mainID, periodID;
    private EndViewModel endViewModel;
    private GrowingViewModel growingViewModel;
    private BeginningViewModel beginningViewModel;
    private com.ali.myfarm.Models.Feed feed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        initializeFields();
        initializeViewModels();
        setupHeader();
        setupViewPager();
        initializeCardsText();
        initializeButtons();
    }

    private void initializeFields() {
        handler = new Handler(Looper.getMainLooper());
        header = findViewById(R.id.feed_header);
        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);

        Bundle extras = Objects.requireNonNull(getIntent().getExtras());
        mainID = extras.getString(Common.MAIN_ID);
        periodID = extras.getString(Common.PERIOD_ID);
    }

    private void initializeViewModels() {
        feedViewModel = new ViewModelProvider(this).get(FeedViewModel.class);
        feedViewModel.initialize(this, mainID, periodID);

        growingViewModel = new ViewModelProvider(this).get(GrowingViewModel.class);
        growingViewModel.initialize(this, mainID, periodID);

        beginningViewModel = new ViewModelProvider(this).get(BeginningViewModel.class);
        beginningViewModel.initialize(this, mainID, periodID);

        endViewModel = new ViewModelProvider(this).get(EndViewModel.class);
        endViewModel.initialize(this, mainID, periodID);
    }

    private void setupHeader() {
        header.setText(String.format("%s - %s", periodID, mainID));
    }

    private void setupViewPager() {
        FragmentViewPagerAdapter adapter = new FragmentViewPagerAdapter(
                getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        adapter.addFragment(new End(mainID, periodID), getString(R.string.over));
        adapter.addFragment(new Beginning(mainID, periodID), getString(R.string.initialize));
        adapter.addFragment(new Growing(mainID, periodID), getString(R.string.growing));

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        TabLayout.Tab growingTab = tabLayout.getTabAt(2);
        if (growingTab != null) growingTab.select();
    }

    private void initializeCardsText() {
        AtomicInteger growing = new AtomicInteger();
        AtomicInteger initialize = new AtomicInteger();
        AtomicInteger end = new AtomicInteger();

        TextView growingTxt = findViewById(R.id.growing_txt);
        TextView beginningTxt = findViewById(R.id.initialize_txt);
        TextView endTxt = findViewById(R.id.end_txt);

        feedViewModel.getFeed().observe(this, feed -> {
            this.feed = feed;
            updateCardText(growing, growingTxt, feed.getGrowing());
            updateCardText(initialize, beginningTxt, feed.getBeginning());
            updateCardText(end, endTxt, feed.getEnd());
        });

        setupCardsOnLongClick(growing, initialize, end);
    }

    private void updateCardText(AtomicInteger value, TextView textView, int newValue) {
        value.set(newValue);
        textView.setText(String.valueOf(value.get()));
    }

    private void setupCardsOnLongClick(AtomicInteger growing, AtomicInteger initialize, AtomicInteger end) {
        MaterialCardView growingCard = findViewById(R.id.growing);
        MaterialCardView initializeCard = findViewById(R.id.initiator);
        MaterialCardView endCard = findViewById(R.id.end);

        View.OnLongClickListener onLongClickListener = v -> {
            int id = v.getId();
            if (id == R.id.growing) {
                observeViewModel(growingViewModel.getGrowing(), growing, R.drawable.growing);
            } else if (id == R.id.initiator) {
                observeViewModel(beginningViewModel.getBeginning(), initialize, R.drawable.initialize);
            } else if (id == R.id.end) {
                observeViewModel(endViewModel.getEnd(), end, R.drawable.end);
            }
            return false;
        };

        growingCard.setOnLongClickListener(onLongClickListener);
        initializeCard.setOnLongClickListener(onLongClickListener);
        endCard.setOnLongClickListener(onLongClickListener);
    }

    private void observeViewModel(LiveData<List<Bag>> liveData, AtomicInteger currentCount, int imageResource) {
        liveData.observe(this, bags -> handleFeedDetails(bags, currentCount, imageResource));
    }

    private void handleFeedDetails(List<Bag> bags, AtomicInteger currentCount, int imageResource) {
        AtomicInteger total = new AtomicInteger();
        if (bags != null) {
            for (Bag bag : bags) {
                if (bag.getOperation() == Bag.Operation.ADD) {
                    total.addAndGet(bag.getNumber());
                }
            }
        }

        new Handler(Looper.getMainLooper()).post(() -> {
            PopupFeed popupFeed = new PopupFeed(
                    imageResource,
                    String.valueOf(total.get()),
                    String.valueOf(total.get() - currentCount.get()),
                    String.valueOf(currentCount.get())
            );
            popupFeed.show(getSupportFragmentManager(), "");
            Vibrate.vibrate(Feed.this);
        });
    }

    private void initializeButtons() {
        findViewById(R.id.record).setOnClickListener(view -> handler.post(this::showFeedStatusDialog));
        findViewById(R.id.back).setOnClickListener(view -> onBackPressed());
    }

    private void showFeedStatusDialog() {
        FeedStatus status = new FeedStatus((type, operation, numberOfBags, priceOfTon) -> {
            if (isValidOperation(type, numberOfBags)) {
                executeFirebaseOperation(type, numberOfBags, priceOfTon, operation);
            } else {
                showAlert(R.drawable.error, getString(R.string.bags_num_out));
            }
        });
        status.show(getSupportFragmentManager(), "");
    }

    private boolean isValidOperation(com.ali.myfarm.Models.Feed.Type type, int numberOfBags) {
        switch (type) {
            case GROWING:
                return feed.getGrowing() - numberOfBags >= 0;
            case BEGGING:
                return feed.getBeginning() - numberOfBags >= 0;
            case END:
                return feed.getEnd() - numberOfBags >= 0;
            default:
                return false;
        }
    }

    private void executeFirebaseOperation(com.ali.myfarm.Models.Feed.Type type, int numberOfBags, double priceOfTon, Bag.Operation operation) {
        Bag bag = new Bag(numberOfBags, priceOfTon, operation, DateAndTime.getCurrentDateTime());
        switch (type) {
            case GROWING:
                Firebase.setGrowing(this, mainID, periodID, bag);
                break;
            case BEGGING:
                Firebase.setInitialize(this, mainID, periodID, bag);
                break;
            case END:
                Firebase.setEnd(this, mainID, periodID, bag);
                break;
        }
    }

    private void showAlert(int icon, String message) {
        handler.post(() -> new Alert(icon, message).show(getSupportFragmentManager(), ""));
    }
}