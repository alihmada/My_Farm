package com.ali.myfarm.Activities;

import android.content.Intent;
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

import com.ali.myfarm.Adapters.ExpensesAdapter;
import com.ali.myfarm.Classes.Calculation;
import com.ali.myfarm.Classes.Common;
import com.ali.myfarm.Classes.DateAndTime;
import com.ali.myfarm.Classes.FirstItemMarginDecoration;
import com.ali.myfarm.Classes.UniqueIdGenerator;
import com.ali.myfarm.Data.Firebase;
import com.ali.myfarm.Dialogs.Alert;
import com.ali.myfarm.Intenet.Internet;
import com.ali.myfarm.Interfaces.ViewOnClickListener;
import com.ali.myfarm.MVVM.ExpensesViewModel;
import com.ali.myfarm.R;

import java.util.List;
import java.util.Objects;

public class Expenses extends AppCompatActivity implements ViewOnClickListener {

    private Bundle bundle;
    private Handler handler;
    private String year, month;
    private ExpensesViewModel model;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private ConstraintLayout alert;
    private ImageView imageView;
    private TextView textView, balance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expenses);

        getExtras();
        initializeViews();
        initializeButtons();
        setupSwipeRefreshLayout();
        setupViewModel();
        setupView();
    }

    private void getExtras() {
        year = Objects.requireNonNull(getIntent().getExtras()).getString(Common.YEAR);
        month = Objects.requireNonNull(getIntent().getExtras()).getString(Common.MONTH);
    }

    private void initializeViews() {
        bundle = new Bundle();
        handler = new Handler(Looper.getMainLooper());
        balance = findViewById(R.id.balance);
        alert = findViewById(R.id.alert);
        imageView = findViewById(R.id.alert_image);
        textView = findViewById(R.id.alert_text);
        progressBar = findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.other_expenses_recycler);
        recyclerView.addItemDecoration(new FirstItemMarginDecoration(getResources().getDimensionPixelSize(R.dimen.margin)));
    }

    private void initializeButtons() {
        ImageButton add = findViewById(R.id.add);
        add.setOnClickListener(view -> handler.post(() -> {
            if (!Common.isFinished) {
                com.ali.myfarm.Dialogs.Expenses expenses = new com.ali.myfarm.Dialogs.Expenses((reason, balance) -> Firebase.setExpenses(Expenses.this, year, month, new com.ali.myfarm.Models.Expenses(UniqueIdGenerator.generateUniqueId(), DateAndTime.getCurrentDateTime(), reason, balance)));
                expenses.show(getSupportFragmentManager(), "");
            } else {
                showAlert(getString(R.string.finished));
            }
        }));

        ImageButton pdf = findViewById(R.id.pdf);
        pdf.setOnClickListener(view -> {

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
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout_for_other_expenses);

        swipeRefreshLayout.setOnRefreshListener(() -> {
            setupView();
            swipeRefreshLayout.setRefreshing(false);
        });
    }

    private void setRecyclerView() {
        model.getExpenses().observe(this, expensesList -> {
            progressBar.setVisibility(View.GONE);

            if (expensesList == null || expensesList.isEmpty()) {
                handleEmptyExpenses();
            } else {
                setupRecyclerViewData(expensesList);
                handler.post(() -> getExpenses(expensesList));
                alert.setVisibility(View.GONE);
            }
        });
    }

    private void handleEmptyExpenses() {
        balance.setText("0");
        recyclerView.setAdapter(null);
        alert.setVisibility(View.VISIBLE);
        textView.setText(getString(R.string.data_not_found));
    }

    private void getExpenses(List<com.ali.myfarm.Models.Expenses> expensesList) {
        double sum = 0;
        for (com.ali.myfarm.Models.Expenses expenses : expensesList) {
            sum += expenses.getBalance();
        }

        balance.setText(Calculation.formatNumberWithCommas(sum));
    }

    private void setupRecyclerViewData(List<com.ali.myfarm.Models.Expenses> expensesList) {
        ExpensesAdapter adapter = new ExpensesAdapter(expensesList, this);
        recyclerView.setAdapter(adapter);
    }

    private void setupViewModel() {
        model = new ViewModelProvider(this).get(ExpensesViewModel.class);
        model.initialize(this, year, month);
    }

    private void showAlert(String message) {
        handler.post(() -> new Alert(R.drawable.error, message).show(getSupportFragmentManager(), ""));
    }

    @Override
    public void onClickListener(String id) {
        Intent intent = new Intent(this, ExpensesInfo.class);
        bundle.putString(Common.YEAR, year);
        bundle.putString(Common.MONTH, month);
        bundle.putString(Common.MOVED_DATA, id);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}