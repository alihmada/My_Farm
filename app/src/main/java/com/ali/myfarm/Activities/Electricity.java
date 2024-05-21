package com.ali.myfarm.Activities;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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

import com.ali.myfarm.Adapters.ElectricityAdapter;
import com.ali.myfarm.Classes.Calculation;
import com.ali.myfarm.Classes.Common;
import com.ali.myfarm.Classes.DateAndTime;
import com.ali.myfarm.Classes.FirstItemMarginDecoration;
import com.ali.myfarm.Data.Firebase;
import com.ali.myfarm.Dialogs.ValueDialog;
import com.ali.myfarm.Intenet.Internet;
import com.ali.myfarm.MVVM.ElectricityViewModel;
import com.ali.myfarm.R;

import java.util.List;
import java.util.Objects;

public class Electricity extends AppCompatActivity {
    String mainID, periodID;
    ElectricityViewModel model;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private ConstraintLayout alert;
    private ImageView imageView;
    private TextView textView, price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_electricity);

        initializeViews();
        initializeButtons();
        mainID = Objects.requireNonNull(getIntent().getExtras()).getString(Common.MAIN_ID);
        periodID = Objects.requireNonNull(getIntent().getExtras()).getString(Common.PERIOD_ID);
        setupSwipeRefreshLayout();
        setupViewModel();
        setupView();
    }

    private void initializeViews() {
        price = findViewById(R.id.price);
        alert = findViewById(R.id.alert);
        imageView = findViewById(R.id.alert_image);
        textView = findViewById(R.id.alert_text);
        progressBar = findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.electricity_recycler);
        recyclerView.addItemDecoration(new FirstItemMarginDecoration(getResources().getDimensionPixelSize(R.dimen.margin)));
    }

    private void initializeButtons() {
        ImageButton add = findViewById(R.id.add);
        add.setOnClickListener(view -> new Handler(Looper.getMainLooper()).post(() -> {
            ValueDialog valueDialog = new ValueDialog(R.string.price, ValueDialog.inputType.DECIMAL, Common.DECIMAL_REGEX, price -> Firebase.setElectricity(Electricity.this, mainID, periodID, new com.ali.myfarm.Models.Electricity(DateAndTime.getCurrentDateTime(), Double.parseDouble(price))));
            valueDialog.show(getSupportFragmentManager(), "");
        }));

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
        model.getElectricity().observe(this, electricityList -> {
            if (electricityList != null) {
                if (!electricityList.isEmpty()) {
                    setupRecyclerViewData(electricityList);
                    setupCards(electricityList);
                    alert.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                }
            } else {
                alert.setVisibility(View.VISIBLE);
                textView.setText(getString(R.string.data_not_found));
                progressBar.setVisibility(View.GONE);
                price.setText("0");
            }
        });
    }

    private void setupCards(List<com.ali.myfarm.Models.Electricity> electricityList) {
        double sum = 0;
        for (com.ali.myfarm.Models.Electricity electricity : electricityList) {
            sum += electricity.getPrice();
        }

        price.setText(Calculation.getNumber(sum));
    }

    private void setupRecyclerViewData(List<com.ali.myfarm.Models.Electricity> electricityList) {
        ElectricityAdapter adapter = new ElectricityAdapter(electricityList);
        recyclerView.setAdapter(adapter);
    }

    private void setupViewModel() {
        model = new ViewModelProvider(this).get(ElectricityViewModel.class);
        model.initialize(this, mainID, periodID);
    }
}