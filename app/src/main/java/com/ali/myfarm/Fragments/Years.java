package com.ali.myfarm.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.ali.myfarm.Adapters.YearsAdapter;
import com.ali.myfarm.Classes.FirstItemMarginDecoration;
import com.ali.myfarm.Intenet.Internet;
import com.ali.myfarm.Interfaces.OnResume;
import com.ali.myfarm.Interfaces.ViewOnClickListener;
import com.ali.myfarm.MVVM.YearsViewModel;
import com.ali.myfarm.R;

import java.util.List;

public class Years extends Fragment implements ViewOnClickListener {

    private OnUserSelectYearInterface onUserSelectYearInterface;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private YearsAdapter yearsAdapter;
    private ProgressBar progressBar;
    private ConstraintLayout alert;
    private OnResume onResume;
    private YearsViewModel model;
    private ImageView imageView;
    private TextView alertText;

    public Years() {

    }

    public Years(OnResume onResume, OnUserSelectYearInterface onUserSelectYearInterface) {
        this.onResume = onResume;
        this.onUserSelectYearInterface = onUserSelectYearInterface;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_years, container, false);

        initializeViews(view);
        setupViewModel();
        setupView();
        setupSwipeRefreshLayout(view);

        return view;
    }

    private void initializeViews(View view) {
        alert = view.findViewById(R.id.alert);
        imageView = view.findViewById(R.id.alert_image);
        alertText = view.findViewById(R.id.alert_text);
        progressBar = view.findViewById(R.id.progressBar);
        recyclerView = view.findViewById(R.id.years_recycler);
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

        model.getYears().observe(requireActivity(), years -> {
            progressBar.setVisibility(View.GONE);

            if (years == null || years.isEmpty()) {
                handleEmptyYears();
            } else {
                setupRecyclerViewData(years);
                alert.setVisibility(View.GONE);
            }
        });
    }

    private void handleEmptyYears() {
        alert.setVisibility(View.VISIBLE);
        alertText.setText(getString(R.string.data_not_found));
    }

    private void setupViewModel() {
        model = new ViewModelProvider(this).get(YearsViewModel.class);
        model.initialize(requireContext());
    }

    private void setupRecyclerViewData(List<String> yearList) {
        yearsAdapter = new YearsAdapter(yearList, this);
        recyclerView.setAdapter(yearsAdapter);
    }

    public void search(String query) {
        if (yearsAdapter != null) {
            yearsAdapter.getFilter().filter(query);
        }
    }

    @Override
    public void onClickListener(String year) {
        onUserSelectYearInterface.onUserSelectYear(year);
    }

    @Override
    public void onResume() {
        super.onResume();
        onResume.onResumeFragment();
    }

    public interface OnUserSelectYearInterface {
        void onUserSelectYear(String year);
    }
}