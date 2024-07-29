package com.ali.myfarm.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.ali.myfarm.Activities.Chicken;
import com.ali.myfarm.Activities.Feed;
import com.ali.myfarm.Classes.Common;
import com.ali.myfarm.Interfaces.OnMoreClickedListener;
import com.ali.myfarm.MVVM.PeriodViewModel;
import com.ali.myfarm.R;
import com.google.android.material.card.MaterialCardView;

public class First extends Fragment {

    private MaterialCardView chickens, feed, medicine, more;
    private OnMoreClickedListener onMoreClickedListener;
    private String year, month;
    private PeriodViewModel model;
    private TextView numberOfChickens, feedCount;
    private Bundle bundle;

    public First() {
        // Required empty public constructor
    }

    public First(String year, String month, OnMoreClickedListener onMoreClickedListener) {
        this.year = year;
        this.month = month;
        this.onMoreClickedListener = onMoreClickedListener;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnMoreClickedListener) {
            onMoreClickedListener = (OnMoreClickedListener) context;
        } else {
            throw new RuntimeException(context + " must implement OnMoreClickedListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_first, container, false);

        if (year == null && month == null && savedInstanceState != null) {
            year = savedInstanceState.getString(Common.YEAR);
            month = savedInstanceState.getString(Common.MONTH);
        }

        initializeViews(view);

        return view;
    }

    private void initializeViews(View view) {
        bundle = new Bundle();
        setupViewModel();
        initializeCards(view);
        initializeTextViews(view);
        initializeOnClickListeners();
        getPeriod();
    }

    private void initializeCards(View view) {
        chickens = view.findViewById(R.id.chicken);
        feed = view.findViewById(R.id.feed);
        medicine = view.findViewById(R.id.medicine);
        more = view.findViewById(R.id.more);
    }

    private void initializeTextViews(View view) {
        numberOfChickens = view.findViewById(R.id.chicken_number);
        feedCount = view.findViewById(R.id.feed_count);
    }

    private void initializeOnClickListeners() {
        setupChickens();
        setupFeed();
        setupMedicine();
        setupMore();
    }

    private void setupChickens() {
        chickens.setOnClickListener(view -> {
            Intent intent = new Intent(requireContext(), Chicken.class);
            bundle.putString(Common.YEAR, year);
            bundle.putString(Common.MONTH, month);
            intent.putExtras(bundle);
            startActivity(intent);
        });
    }

    private void setupFeed() {
        feed.setOnClickListener(view -> {
            Intent intent = new Intent(requireContext(), Feed.class);
            bundle.putString(Common.YEAR, year);
            bundle.putString(Common.MONTH, month);
            intent.putExtras(bundle);
            startActivity(intent);
        });
    }

    private void setupMedicine() {
        medicine.setOnClickListener(view -> {
//            Intent intent = new Intent(requireContext(), Medicine.class);
//            startActivity(intent);
        });
    }

    private void setupViewModel() {
        model = new ViewModelProvider(this).get(PeriodViewModel.class);
        model.initialize(getContext(), year, month);
    }

    private void getPeriod() {
        model.getPeriod().observe(requireActivity(), period -> {
            if (period != null) {
                numberOfChickens.setText(String.valueOf(period.getNumberOfAliveChickens() - period.getNumberOfSold()));
                feedCount.setText(String.valueOf(period.getNumberOfFeedBags()));
            }
        });
    }

    private void setupMore() {
        more.setOnClickListener(view -> onMoreClickedListener.onMoreClicked());
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(Common.YEAR, year);
        outState.putString(Common.MONTH, month);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onMoreClickedListener = null; // Avoid memory leaks
    }
}