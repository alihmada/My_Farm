package com.ali.myfarm.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.ali.myfarm.Activities.Electricity;
import com.ali.myfarm.Activities.Expenses;
import com.ali.myfarm.Activities.Heating;
import com.ali.myfarm.Activities.Transaction;
import com.ali.myfarm.Classes.Common;
import com.ali.myfarm.R;
import com.google.android.material.card.MaterialCardView;

public class Second extends Fragment {

    private Bundle bundle;
    private String year, month;
    private MaterialCardView transactions, heating, electricity, expenses;

    public Second() {
        // Required empty public constructor
    }

    public Second(String year, String month) {
        this.year = year;
        this.month = month;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_second, container, false);

        if (year == null && month == null && savedInstanceState != null) {
            year = savedInstanceState.getString(Common.YEAR);
            month = savedInstanceState.getString(Common.MONTH);
        }

        initializeViews(view);

        return view;
    }

    private void initializeViews(View view) {
        bundle = new Bundle();
        initializeCards(view);
        initializeOnClickListeners();
    }

    private void initializeCards(View view) {
        heating = view.findViewById(R.id.fire);
        electricity = view.findViewById(R.id.energy);
        transactions = view.findViewById(R.id.transaction);
        expenses = view.findViewById(R.id.expenses);
    }

    private void initializeOnClickListeners() {
        setupHeating();
        setupElectricity();
        setupTransaction();
        setupExpenses();
    }

    private void setupHeating() {
        heating.setOnClickListener(view -> {
            Intent intent = new Intent(requireContext(), Heating.class);
            bundle.putString(Common.YEAR, year);
            bundle.putString(Common.MONTH, month);
            intent.putExtras(bundle);
            startActivity(intent);
        });
    }

    private void setupElectricity() {
        electricity.setOnClickListener(view -> {
            Intent intent = new Intent(requireContext(), Electricity.class);
            bundle.putString(Common.YEAR, year);
            bundle.putString(Common.MONTH, month);
            intent.putExtras(bundle);
            startActivity(intent);
        });
    }

    private void setupTransaction() {
        transactions.setOnClickListener(view -> {
            Intent intent = new Intent(requireContext(), Transaction.class);
            bundle.putString(Common.YEAR, year);
            bundle.putString(Common.MONTH, month);
            intent.putExtras(bundle);
            startActivity(intent);
        });
    }

    private void setupExpenses() {
        expenses.setOnClickListener(view -> {
            Intent intent = new Intent(requireContext(), Expenses.class);
            bundle.putString(Common.YEAR, year);
            bundle.putString(Common.MONTH, month);
            intent.putExtras(bundle);
            startActivity(intent);
        });
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(Common.YEAR, year);
        outState.putString(Common.MONTH, month);
    }
}