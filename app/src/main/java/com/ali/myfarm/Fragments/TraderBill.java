package com.ali.myfarm.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.ali.myfarm.Classes.Calculation;
import com.ali.myfarm.Classes.Common;
import com.ali.myfarm.Classes.Vibrate;
import com.ali.myfarm.Data.Firebase;
import com.ali.myfarm.Dialogs.Alert;
import com.ali.myfarm.MVVM.PeriodViewModel;
import com.ali.myfarm.MVVM.PersonViewModel;
import com.ali.myfarm.MVVM.TransactionViewModel;
import com.ali.myfarm.Models.Period;
import com.ali.myfarm.Models.Sale;
import com.ali.myfarm.Models.Trader;
import com.ali.myfarm.Models.Transaction;
import com.ali.myfarm.R;
import com.google.gson.Gson;

public class TraderBill extends Fragment {

    private Transaction transaction;
    private TransactionViewModel transactionViewModel;
    private double chickensPrice;
    private Trader trader;
    private Period periodData;
    private PersonViewModel personViewModel;
    private PeriodViewModel periodViewModel;
    private String year, period, traderJson;
    private OnFragmentInteractionListener listener;

    public TraderBill() {
        // Required empty public constructor
    }

    public TraderBill(String year, String period, String traderJson) {
        this.year = year;
        this.period = period;
        this.traderJson = traderJson;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            listener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bill, container, false);

        if (year == null && period == null && savedInstanceState != null) {
            year = savedInstanceState.getString(Common.YEAR);
            period = savedInstanceState.getString(Common.PERIOD);
            traderJson = savedInstanceState.getString(Common.MOVED_DATA);
        }

        parseTraderData();
        setupViews(view);

        if (year != null && period != null) {
            setupViewModels();
            observePeriodData();
            observeTransactionData();
        }

        setupButtons(view);

        return view;
    }

    private void parseTraderData() {
        trader = new Gson().fromJson(traderJson, Trader.class);
    }

    private void setupViews(View view) {
        TextView name = view.findViewById(R.id.name);
        TextView numberOfCages = view.findViewById(R.id.num_of_cages);
        TextView weightOfEmptyCages = view.findViewById(R.id.weight_of_empty_cages);
        TextView weightOfFullCages = view.findViewById(R.id.weight_of_full_cages);
        TextView numberOfChickens = view.findViewById(R.id.num_of_chickens);
        TextView priceOfKg = view.findViewById(R.id.price_of_kilo);
        TextView average = view.findViewById(R.id.average);
        TextView weight = view.findViewById(R.id.weight);
        TextView totalPrice = view.findViewById(R.id.total);

        name.setText(trader.getName());
        numberOfCages.setText(String.valueOf(trader.getTotalNumberOfCages()));
        weightOfEmptyCages.setText(Calculation.formatNumberWithCommas(trader.getTotalWeightOfCages() - trader.getWeightOfChickens()));
        weightOfFullCages.setText(Calculation.formatNumberWithCommas(trader.getTotalWeightOfCages()));
        numberOfChickens.setText(String.valueOf(trader.getNumberOfChickens()));
        priceOfKg.setText(Calculation.formatNumberWithCommas(trader.getPrice()));

        average.setText(Calculation.formatNumberWithCommas(Calculation.getAverage(trader.getWeightOfChickens(), trader.getNumberOfChickens())));
        weight.setText(Calculation.formatNumberWithCommas(trader.getWeightOfChickens()));

        chickensPrice = Calculation.getTotalPrice(trader.getWeightOfChickens(), trader.getPrice());

        totalPrice.setText(Calculation.formatNumberWithCommas(chickensPrice));
    }

    private void setupButtons(View view) {
        Button doneButton = view.findViewById(R.id.done);

        if (year == null && period == null) {
            doneButton.setVisibility(View.GONE);
        } else {
            doneButton.setOnClickListener(v -> handleDoneButtonClick());
        }
    }

    private void handleDoneButtonClick() {
        if ((periodData.getNumberOfAliveChickens() - periodData.getNumberOfSold()) - trader.getNumberOfChickens() >= 0) {
            Firebase.setTraderInTraderBranchFromTransactionBranchThatPeriodHave(requireContext(), year, period, trader);
            Firebase.setSale(requireContext(), year, period, new Sale(trader.getDate(), trader.getNumberOfChickens(), periodData.getNumberOfAliveChickens() - trader.getNumberOfChickens()));
            personViewModel.getPerson().observe(requireActivity(), person -> {
                if (person != null) {
                    person.values().stream().findFirst().ifPresent(value -> {
                        Firebase.setTransactionValueInTraderThatPersonHave(value, year, period);
                        Firebase.updateSoldValue(requireContext(), year, period, periodData.getNumberOfSold(), trader.getNumberOfChickens());
                        Firebase.updateWeightAndPriceForTrader(requireContext(), year, period, transaction.getWeightForTraders(), trader.getWeightOfChickens(), transaction.getPriceForTraders(), chickensPrice);
                        Vibrate.vibrate(requireContext());
                        Toast.makeText(requireContext(), getString(R.string.saved), Toast.LENGTH_SHORT).show();
                        if (listener != null) {
                            listener.onFinishActivity();
                        }
                    });
                }
            });
        } else {
            new Handler(Looper.getMainLooper()).post(() -> new Alert(R.drawable.wrong, getString(R.string.no_enough_chickens)).show(getParentFragmentManager(), ""));
        }
    }

    private void setupViewModels() {
        personViewModel = new ViewModelProvider(this).get(PersonViewModel.class);
        transactionViewModel = new ViewModelProvider(this).get(TransactionViewModel.class);
        periodViewModel = new ViewModelProvider(this).get(PeriodViewModel.class);

        personViewModel.initialize(requireContext(), trader.getName());
        transactionViewModel.initialize(requireContext(), year, period);
        periodViewModel.initialize(requireContext(), year, period);
    }

    private void observePeriodData() {
        periodViewModel.getPeriod().observe(requireActivity(), period -> periodData = period);
    }

    private void observeTransactionData() {
        transactionViewModel.getTransaction().observe(requireActivity(), transaction -> this.transaction = transaction);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(Common.YEAR, year);
        outState.putString(Common.PERIOD, period);
        outState.putString(Common.MOVED_DATA, traderJson);
    }

    public interface OnFragmentInteractionListener {
        void onFinishActivity();
    }
}