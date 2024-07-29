package com.ali.myfarm.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.ali.myfarm.Activities.Period;
import com.ali.myfarm.Adapters.MonthsAdapter;
import com.ali.myfarm.Classes.Common;
import com.ali.myfarm.Classes.FirstItemMarginDecoration;
import com.ali.myfarm.Intenet.Internet;
import com.ali.myfarm.Interfaces.ViewOnClickListener;
import com.ali.myfarm.MVVM.MonthsViewModel;
import com.ali.myfarm.R;

import java.util.AbstractMap;
import java.util.List;

public class Months extends Fragment implements ViewOnClickListener {

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private MonthsAdapter monthsAdapter;
    private ProgressBar progressBar;
    private ConstraintLayout alert;
    private MonthsViewModel model;
    private ImageView imageView;
    private TextView alertText;
    private Bundle bundle;
    private String year;

    public Months() {

    }

    public Months(String year) {
        this.year = year;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_months, container, false);

        if (year == null && savedInstanceState != null) {
            year = savedInstanceState.getString(Common.YEAR);
        }

        initializeViews(view);
        setupViewModel();
        setupView();
        setupSwipeRefreshLayout(view);

        return view;
    }

    private void initializeViews(View view) {
        bundle = new Bundle();
        alert = view.findViewById(R.id.alert);
        imageView = view.findViewById(R.id.alert_image);
        alertText = view.findViewById(R.id.alert_text);
        progressBar = view.findViewById(R.id.progressBar);
        recyclerView = view.findViewById(R.id.months_recycler);
        recyclerView.addItemDecoration(new FirstItemMarginDecoration(getResources().getDimensionPixelSize(R.dimen.margin)));
    }

    private void setupSwipeRefreshLayout(View view) {
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);

        swipeRefreshLayout.setOnRefreshListener(() -> {
            setupView();
            swipeRefreshLayout.setRefreshing(false);
        });
    }

    private void setupView() {
        if (Internet.isConnectedWithoutMessage(requireContext())) {
            alert.setVisibility(View.GONE);
            if (Internet.isNetworkLimited(requireContext())) {
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
        alertText.setText(message);
        recyclerView.setAdapter(null);
        imageView.setImageDrawable(AppCompatResources.getDrawable(requireContext(), R.drawable.wifi_off));
    }

    private void setRecyclerView() {
        model.getPeriods().observe(requireActivity(), periods -> {
            progressBar.setVisibility(View.GONE);

            if (periods == null || periods.isEmpty()) {
                handleEmptyPeriods();
            } else {
                setupRecyclerViewData(periods);
                alert.setVisibility(View.GONE);
            }
        });
    }

    private void handleEmptyPeriods() {
        recyclerView.setAdapter(null);
        alert.setVisibility(View.VISIBLE);
        alertText.setText(getString(R.string.data_not_found));
    }

    private void setupViewModel() {
        model = new ViewModelProvider(this).get(MonthsViewModel.class);
        model.initialize(requireContext(), year);
    }

    private void setupRecyclerViewData(List<AbstractMap.SimpleEntry<String, String>> periodsList) {
        monthsAdapter = new MonthsAdapter(this, periodsList);
        recyclerView.setAdapter(monthsAdapter);
    }

    public void search(String query) {
        if (monthsAdapter != null) {
            monthsAdapter.getFilter().filter(query);
        }
    }

    @Override
    public void onClickListener(String month) {
        bundle.putString(Common.YEAR, year);
        bundle.putString(Common.MONTH, month);

        Intent intent = new Intent(requireContext(), Period.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(Common.YEAR, year);
    }
}