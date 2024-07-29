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
import com.ali.myfarm.Dialogs.Alert;
import com.ali.myfarm.Dialogs.ElectricityDialog;
import com.ali.myfarm.Intenet.Internet;
import com.ali.myfarm.MVVM.ElectricityViewModel;
import com.ali.myfarm.R;

import java.util.List;
import java.util.Objects;

public class Electricity extends AppCompatActivity {

    private Handler handler;
    private String year, month;
    private ElectricityViewModel model;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private ConstraintLayout alert;
    private ImageView imageView;
    private TextView textView, numberOfReceipts, priceOfReceipts, numberOfLiters, priceOfLiters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_electricity);

        initializeViews();
        initializeButtons();
        year = Objects.requireNonNull(getIntent().getExtras()).getString(Common.YEAR);
        month = Objects.requireNonNull(getIntent().getExtras()).getString(Common.MONTH);
        setupSwipeRefreshLayout();
        setupViewModel();
        setupView();
    }

    private void initializeViews() {
        handler = new Handler(Looper.getMainLooper());

        numberOfReceipts = findViewById(R.id.number_of_receipt);
        priceOfReceipts = findViewById(R.id.price_of_receipt);
        numberOfLiters = findViewById(R.id.number_of_liters);
        priceOfLiters = findViewById(R.id.price_of_liters);

        alert = findViewById(R.id.alert);
        imageView = findViewById(R.id.alert_image);
        textView = findViewById(R.id.alert_text);
        progressBar = findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.electricity_recycler);
        recyclerView.addItemDecoration(new FirstItemMarginDecoration(getResources().getDimensionPixelSize(R.dimen.margin)));
    }

    private void initializeButtons() {
        ImageButton add = findViewById(R.id.add);
        add.setOnClickListener(view -> handler.post(() -> {
            if (!Common.isFinished) {
                ElectricityDialog dialog = new ElectricityDialog((type, number, date, price) -> Firebase.setElectricity(Electricity.this, year, month,
                        new com.ali.myfarm.Models.Electricity(DateAndTime.getCurrentDateTime(), price, type, number)));
                dialog.show(getSupportFragmentManager(), "");
            } else {
                showAlert(getString(R.string.finished));
            }
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
            progressBar.setVisibility(View.GONE);

            if (electricityList == null || electricityList.isEmpty()) {
                handleEmptyElectricity();
            } else {
                setupRecyclerViewData(electricityList);
                handler.post(() -> setupCards(electricityList));
                alert.setVisibility(View.GONE);
            }
        });
    }

    private void handleEmptyElectricity() {
        recyclerView.setAdapter(null);
        alert.setVisibility(View.VISIBLE);
        textView.setText(getString(R.string.data_not_found));
        setTextViewText(0, 0, 0, 0); // Reset values if needed
    }

    private void setupCards(List<com.ali.myfarm.Models.Electricity> electricityList) {
        double receiptsCount = 0, litersCount = 0, receiptsPrice = 0, litersPrice = 0;

        for (com.ali.myfarm.Models.Electricity electricity : electricityList) {
            if (electricity.getType() == com.ali.myfarm.Models.Electricity.Type.RECEIPT) {
                receiptsCount += electricity.getNumber();
                receiptsPrice += Calculation.getElectricityOrHeatingPrice(electricity.getNumber(), electricity.getPrice());
            } else {
                litersCount += electricity.getNumber();
                litersPrice += Calculation.getElectricityOrHeatingPrice(electricity.getNumber(), electricity.getPrice());
            }
        }

        setTextViewText(receiptsCount, receiptsPrice, litersCount, litersPrice);
    }

    private void setTextViewText(double receiptsCount, double receiptsPrice, double litersCount, double litersPrice) {
        numberOfReceipts.setText(Calculation.formatNumberWithCommas(receiptsCount));
        priceOfReceipts.setText(Calculation.formatNumberWithCommas(receiptsPrice));
        numberOfLiters.setText(Calculation.formatNumberWithCommas(litersCount));
        priceOfLiters.setText(Calculation.formatNumberWithCommas(litersPrice));
    }

    private void setupRecyclerViewData(List<com.ali.myfarm.Models.Electricity> electricityList) {
        ElectricityAdapter adapter = new ElectricityAdapter(electricityList);
        recyclerView.setAdapter(adapter);
    }

    private void setupViewModel() {
        model = new ViewModelProvider(this).get(ElectricityViewModel.class);
        model.initialize(this, year, month);
    }

    private void showAlert(String message) {
        handler.post(() -> new Alert(R.drawable.error, message).show(getSupportFragmentManager(), ""));
    }
}