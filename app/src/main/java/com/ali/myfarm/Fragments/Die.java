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

import com.ali.myfarm.Adapters.DieAdapter;
import com.ali.myfarm.Classes.FirstItemMarginDecoration;
import com.ali.myfarm.Intenet.Internet;
import com.ali.myfarm.MVVM.ChickenViewModel;
import com.ali.myfarm.Models.Chicken;
import com.ali.myfarm.R;

import java.util.List;

public class Die extends Fragment {
    String mainID, periodID;
    ChickenViewModel model;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private ConstraintLayout alert;
    private ImageView imageView;
    private TextView textView;

    public Die() {
    }

    public Die(String mainID, String periodID) {
        this.mainID = mainID;
        this.periodID = periodID;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_die, container, false);

        initializeViews(view);
        setupSwipeRefreshLayout(view);
        setupViewModel();
        setupView();

        return view;
    }

    private void initializeViews(View view) {
        alert = view.findViewById(R.id.alert);
        imageView = view.findViewById(R.id.alert_image);
        textView = view.findViewById(R.id.alert_text);
        progressBar = view.findViewById(R.id.progressBar);
        recyclerView = view.findViewById(R.id.die_recycler);
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
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout_for_die);

        swipeRefreshLayout.setOnRefreshListener(() -> {
            setupView();
            swipeRefreshLayout.setRefreshing(false);
        });
    }

    private void setRecyclerView() {
        model.getChicken().observe(requireActivity(), chickens -> {
            if (chickens != null) {
                if (!chickens.isEmpty()) {
                    setupRecyclerViewData(chickens);
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

    private void setupRecyclerViewData(List<Chicken> chickens) {
        DieAdapter adapter = new DieAdapter(chickens);
        recyclerView.setAdapter(adapter);
    }

    private void setupViewModel() {
        model = new ViewModelProvider(this).get(ChickenViewModel.class);
        model.initialize(requireContext(), mainID, periodID);
    }
}