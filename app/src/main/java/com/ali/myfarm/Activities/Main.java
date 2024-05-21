package com.ali.myfarm.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.ali.myfarm.Adapters.MainAdapter;
import com.ali.myfarm.Classes.Common;
import com.ali.myfarm.Classes.DateAndTime;
import com.ali.myfarm.Classes.FirstItemMarginDecoration;
import com.ali.myfarm.Data.Firebase;
import com.ali.myfarm.Dialogs.NewPeriod;
import com.ali.myfarm.Intenet.Internet;
import com.ali.myfarm.Interfaces.ViewOnClickListener;
import com.ali.myfarm.MVVM.YearsViewModel;
import com.ali.myfarm.Models.Period;
import com.ali.myfarm.R;

import java.util.List;

public class Main extends AppCompatActivity implements ViewOnClickListener {

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private MainAdapter mainAdapter;
    private ProgressBar progressBar;
    private ConstraintLayout alert;
    private YearsViewModel model;
    private ImageView imageView;
    private TextView textView;
    private EditText search;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bundle = new Bundle();
        initializeViews();
        initializeButtons();
        setupViewModel();
        setupView();
        setupSearch();
        setupSwipeRefreshLayout();
    }

    private void initializeViews() {
        alert = findViewById(R.id.alert);
        imageView = findViewById(R.id.alert_image);
        textView = findViewById(R.id.alert_text);
        progressBar = findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.main_recycler);
        recyclerView.addItemDecoration(new FirstItemMarginDecoration(getResources().getDimensionPixelSize(R.dimen.margin)));
    }

    private void initializeButtons() {
        ImageButton addPeriod = findViewById(R.id.add_period);
        addPeriod.setOnClickListener(view -> new Handler(Looper.getMainLooper()).post(() -> {
            NewPeriod period = new NewPeriod((count, price) -> Firebase.setPeriod(this, DateAndTime.getYear(), new Period(DateAndTime.getArabicNameOfMonth(), DateAndTime.getCurrentDateTime(), "", Integer.parseInt(count), 0, 0, 0, Double.parseDouble(price)), getSupportFragmentManager()));
            period.show(getSupportFragmentManager(), "");
        }));

        ImageButton menu = findViewById(R.id.menu);
        menu.setOnClickListener(view -> startActivity(new Intent(this, Menu.class)));
    }

    private void setupSwipeRefreshLayout() {
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);

        swipeRefreshLayout.setOnRefreshListener(() -> {
            setupView();
            swipeRefreshLayout.setRefreshing(false);
        });
    }

    private void setupView() {
        if (Internet.isConnectedWithoutMessage(this)) {
            alert.setVisibility(View.GONE);
            if (Internet.isNetworkLimited(this)) {
                setupWifi(getString(R.string.internet_limited));
            } else {
                setRecyclerView();
            }
        } else {
            setupWifi(getString(R.string.no_internet));
        }
    }

    private void setupWifi(String message) {
        alert.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
        textView.setText(message);
        recyclerView.setAdapter(null);
        imageView.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.wifi_off));
    }

    private void setupSearch() {
        search = findViewById(R.id.search_view);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (mainAdapter != null) {
                    mainAdapter.getFilter().filter(s);
                }
            }
        });
    }

    private void setRecyclerView() {
        model.getYears().observe(this, years -> {
            if (years != null) {
                if (!years.isEmpty()) {
                    search.setEnabled(true);
                    setupRecyclerViewData(years);
                    alert.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                }
            } else {
                search.setEnabled(false);
                alert.setVisibility(View.VISIBLE);
                textView.setText(getString(R.string.data_not_found));
                progressBar.setVisibility(View.GONE);
            }
        });

    }

    private void setupViewModel() {
        model = new ViewModelProvider(this).get(YearsViewModel.class);
        model.initialize(this);
    }

    private void setupRecyclerViewData(List<String> yearList) {
        mainAdapter = new MainAdapter(yearList, Main.this);
        recyclerView.setAdapter(mainAdapter);
    }

    @Override
    public void onClickListener(String id) {
        bundle.putString(Common.MAIN_ID, id);

        Intent intent = new Intent(this, Periods.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}