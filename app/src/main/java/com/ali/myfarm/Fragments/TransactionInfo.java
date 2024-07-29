package com.ali.myfarm.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.ali.myfarm.Classes.Calculation;
import com.ali.myfarm.Data.Firebase;
import com.ali.myfarm.MVVM.TransactionViewModel;
import com.ali.myfarm.Models.Transaction;
import com.ali.myfarm.R;

public class TransactionInfo extends Fragment {

    private TransactionViewModel model;
    private String mainID, periodID;
    private int sold;

    public TransactionInfo() {
        // Required empty public constructor
    }

    public TransactionInfo(String mainID, String periodID, int sold) {
        this.periodID = periodID;
        this.mainID = mainID;
        this.sold = sold;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transaction_info, container, false);

        setupViewModel();
        getTransaction(view);

        return view;
    }

    private void initialize(View view, Transaction transaction) {
        ((TextView) view.findViewById(R.id.sold)).setText(Calculation.formatNumberWithCommas(sold));

        ((TextView) view.findViewById(R.id.average)).setText(Calculation.formatNumberWithCommas(Calculation.getAverage(transaction.getTotalWeight(), sold)));

        ((TextView) view.findViewById(R.id.total_weight_of_traders)).setText(Calculation.formatNumberWithCommas(transaction.getWeightForTraders()));
        ((TextView) view.findViewById(R.id.total_price_of_traders)).setText(Calculation.formatNumberWithCommas(transaction.getPriceForTraders()));

        ((TextView) view.findViewById(R.id.total_weight_of_buyers)).setText(Calculation.formatNumberWithCommas(transaction.getWeightForBuyers()));
        ((TextView) view.findViewById(R.id.total_price_of_buyers)).setText(Calculation.formatNumberWithCommas(transaction.getPriceForBuyers()));

        ((TextView) view.findViewById(R.id.weight)).setText(Calculation.formatNumberWithCommas(transaction.getTotalWeight()));
        ((TextView) view.findViewById(R.id.total)).setText(Calculation.formatNumberWithCommas(transaction.getTotalPrice()));
    }

    private void setupViewModel() {
        model = new ViewModelProvider(this).get(TransactionViewModel.class);
        model.initialize(requireContext(), mainID, periodID);
    }

    private void getTransaction(View view) {
        model.getTransaction().observe(requireActivity(), transaction -> {
            if (transaction != null) {
                initialize(view, transaction);
            } else {
                Firebase.setTransactionBranchThatPeriodHave(requireContext(), mainID, periodID, new com.ali.myfarm.Models.Transaction(0.0, 0.0, 0.0, 0.0));
            }
        });
    }
}