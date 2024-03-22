package com.ali.myfarm.Activities;

import android.os.Bundle;
import android.view.View;
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

import com.ali.myfarm.Adapters.HeatingAdapter;
import com.ali.myfarm.Classes.Common;
import com.ali.myfarm.Classes.FirstItemMarginDecoration;
import com.ali.myfarm.Data.Firebase;
import com.ali.myfarm.Dialogs.HeatingDialog;
import com.ali.myfarm.Intenet.Internet;
import com.ali.myfarm.MVVM.HeatingViewModel;
import com.ali.myfarm.R;

import java.util.List;
import java.util.Objects;

public class Heating extends AppCompatActivity {

    String mainID, periodID;
    HeatingViewModel model;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private ConstraintLayout alert;
    private ImageView imageView;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heating);

        initializeViews();
        initializeButtons();
        mainID = Objects.requireNonNull(getIntent().getExtras()).getString(Common.MAIN_ID);
        periodID = Objects.requireNonNull(getIntent().getExtras()).getString(Common.PERIOD_ID);
        setupSwipeRefreshLayout();
        setupViewModel();
        setupView();
    }

    private void initializeViews() {
        alert = findViewById(R.id.alert);
        imageView = findViewById(R.id.alert_image);
        textView = findViewById(R.id.alert_text);
        progressBar = findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.heating_recycler);
        recyclerView.addItemDecoration(new FirstItemMarginDecoration(getResources().getDimensionPixelSize(R.dimen.margin)));
    }

    private void initializeButtons() {
        ImageButton add = findViewById(R.id.add);
        add.setOnClickListener(view -> {
            HeatingDialog heatingDialog = new HeatingDialog((type, number, date, price) -> Firebase.setHeating(this, mainID, periodID, new com.ali.myfarm.Models.Heating(type, number, date, price)));
            heatingDialog.show(getSupportFragmentManager(), "");
        });

        ImageButton back = findViewById(R.id.back);
        back.setOnClickListener(view -> onBackPressed());
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
        imageView.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.wifi_off));
        recyclerView.setAdapter(null);
        textView.setText(message);
    }

    private void setupSwipeRefreshLayout() {
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout_for_heating);

        swipeRefreshLayout.setOnRefreshListener(() -> {
            setupView();
            swipeRefreshLayout.setRefreshing(false);
        });
    }

    private void setRecyclerView() {
        model.getHeating().observe(this, heatingList -> {
            if (heatingList != null) {
                if (!heatingList.isEmpty()) {
                    setupRecyclerViewData(heatingList);
                    alert.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                }
            } else {
                alert.setVisibility(View.VISIBLE);
                textView.setText(getString(R.string.data_not_found));
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void setupRecyclerViewData(List<com.ali.myfarm.Models.Heating> heatingList) {
        HeatingAdapter adapter = new HeatingAdapter(heatingList);
        recyclerView.setAdapter(adapter);
    }

    private void setupViewModel() {
        model = new ViewModelProvider(this).get(HeatingViewModel.class);
        model.initialize(this, mainID, periodID);
    }
}