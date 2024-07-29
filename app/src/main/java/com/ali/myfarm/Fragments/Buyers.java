package com.ali.myfarm.Fragments;

import android.content.Intent;
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

import com.ali.myfarm.Activities.Bill;
import com.ali.myfarm.Adapters.BuyersTransactionAdapter;
import com.ali.myfarm.Classes.Common;
import com.ali.myfarm.Classes.FirstItemMarginDecoration;
import com.ali.myfarm.Intenet.Internet;
import com.ali.myfarm.Interfaces.ViewOnClickListener;
import com.ali.myfarm.MVVM.BuyersViewModel;
import com.ali.myfarm.Models.Buyer;
import com.ali.myfarm.R;

import java.util.List;

public class Buyers extends Fragment implements ViewOnClickListener {

    private Bundle bundle;
    private BuyersViewModel model;
    private String mainID, periodID;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private ConstraintLayout alert;
    private ImageView imageView;
    private TextView textView;

    public Buyers() {
    }

    public Buyers(String mainID, String periodID) {
        this.mainID = mainID;
        this.periodID = periodID;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_buyer, container, false);

        bundle = new Bundle();

        initializeViews(view);
        setupViewModel();
        setupView();
        setupSwipeRefreshLayout(view);

        return view;
    }

    private void initializeViews(View view) {
        alert = view.findViewById(R.id.alert);
        imageView = view.findViewById(R.id.alert_image);
        textView = view.findViewById(R.id.alert_text);
        progressBar = view.findViewById(R.id.progressBar);
        recyclerView = view.findViewById(R.id.buyer_recycler);
        recyclerView.addItemDecoration(new FirstItemMarginDecoration(getResources().getDimensionPixelSize(R.dimen.margin)));
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
        imageView.setImageDrawable(AppCompatResources.getDrawable(requireContext(), R.drawable.wifi_off));
        recyclerView.setAdapter(null);
        textView.setText(message);
    }

    private void setupSwipeRefreshLayout(View view) {
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout_for_buyer);

        swipeRefreshLayout.setOnRefreshListener(() -> {
            setupView();
            swipeRefreshLayout.setRefreshing(false);
        });
    }

    private void setRecyclerView() {
        model.getBuyers().observe(requireActivity(), buyers -> {
            progressBar.setVisibility(View.GONE);

            if (buyers == null || buyers.isEmpty()) {
                handleEmptyBuyers();
            } else {
                setupRecyclerViewData(buyers);
                alert.setVisibility(View.GONE);
            }
        });
    }

    private void handleEmptyBuyers() {
        recyclerView.setAdapter(null);
        alert.setVisibility(View.VISIBLE);
        textView.setText(getString(R.string.data_not_found));
    }

    private void setupRecyclerViewData(List<Buyer> buyers) {
        BuyersTransactionAdapter adapter = new BuyersTransactionAdapter(buyers, this);
        recyclerView.setAdapter(adapter);
    }

    private void setupViewModel() {
        model = new ViewModelProvider(this).get(BuyersViewModel.class);
        model.initialize(requireContext(), mainID, periodID);
    }

    @Override
    public void onClickListener(String buyer) {
        Intent intent = new Intent(requireContext(), Bill.class);
        bundle.putBoolean(Common.IS_TRADER, false);
        bundle.putString(Common.MOVED_DATA, buyer);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}