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
import com.ali.myfarm.Adapters.TradersTransactionAdapter;
import com.ali.myfarm.Classes.Common;
import com.ali.myfarm.Classes.FirstItemMarginDecoration;
import com.ali.myfarm.Intenet.Internet;
import com.ali.myfarm.Interfaces.ViewOnClickListener;
import com.ali.myfarm.MVVM.TradersViewModel;
import com.ali.myfarm.Models.Trader;
import com.ali.myfarm.R;

import java.util.List;

public class Traders extends Fragment implements ViewOnClickListener {
    private TradersViewModel model;
    private String mainID, periodID;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private ConstraintLayout alert;
    private ImageView imageView;
    private TextView textView;

    public Traders() {
    }

    public Traders(String mainID, String periodID) {
        this.mainID = mainID;
        this.periodID = periodID;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trader, container, false);

        initializeViews(view);
        setupViewModel();
        setupSwipeRefreshLayout(view);
        setupView();

        return view;
    }

    private void initializeViews(View view) {
        alert = view.findViewById(R.id.alert);
        imageView = view.findViewById(R.id.alert_image);
        textView = view.findViewById(R.id.alert_text);
        progressBar = view.findViewById(R.id.progressBar);
        recyclerView = view.findViewById(R.id.trader_recycler);
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
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout_for_traders);

        swipeRefreshLayout.setOnRefreshListener(() -> {
            setupView();
            swipeRefreshLayout.setRefreshing(false);
        });
    }

    private void setRecyclerView() {

        model.getTraders().observe(requireActivity(), traders -> {
            if (traders != null) {
                if (!traders.isEmpty()) {
                    setupRecyclerViewData(traders);
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

    private void setupRecyclerViewData(List<Trader> traders) {
        TradersTransactionAdapter adapter = new TradersTransactionAdapter(traders, this);
        recyclerView.setAdapter(adapter);
    }

    private void setupViewModel() {
        model = new ViewModelProvider(this).get(TradersViewModel.class);
        model.initialize(requireContext(), mainID, periodID);
    }

    @Override
    public void onClickListener(String trader) {
        Intent intent = new Intent(requireContext(), Bill.class);
        intent.putExtra(Common.IS_TRADER, true);
        intent.putExtra(Common.MOVED_DATA, trader);
        startActivity(intent);
    }
}