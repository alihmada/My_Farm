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
import com.ali.myfarm.Classes.Vibrate;
import com.ali.myfarm.Data.Firebase;
import com.ali.myfarm.Dialogs.Alert;
import com.ali.myfarm.MVVM.PeriodViewMadel;
import com.ali.myfarm.MVVM.PersonViewModel;
import com.ali.myfarm.Models.Period;
import com.ali.myfarm.Models.Sale;
import com.ali.myfarm.Models.Trader;
import com.ali.myfarm.R;
import com.google.firebase.database.DatabaseReference;
import com.google.gson.Gson;

import java.util.Optional;

public class TraderBill extends Fragment {

    private Trader trader;
    private Period periodData;
    private PersonViewModel model;
    private PeriodViewMadel periodViewMadel;
    private String year, period, traderData;
    private OnFragmentInteractionListener mListener;

    public TraderBill() {
        // Required empty public constructor
    }

    public TraderBill(String year, String period, String trader) {
        this.year = year;
        this.period = period;
        this.traderData = trader;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bill, container, false);

        getIntentData();
        setupViews(view);
        if (year != null && period != null) {
            setupViewModel();
            setupPeriodViewModel();
            getPeriodData();
        }
        initializeButtons(view);

        return view;
    }

    private void getIntentData() {
        Gson gson = new Gson();
        trader = gson.fromJson(traderData, Trader.class);
    }

    private void setupViews(View view) {
        TextView name = view.findViewById(R.id.name);
        name.setText(trader.getName());
        TextView numberOfCages = view.findViewById(R.id.num_of_cages);
        numberOfCages.setText(String.valueOf(trader.getTotalNumberOfCages()));
        TextView weightOfEmptyCages = view.findViewById(R.id.weight_of_empty_cages);
        weightOfEmptyCages.setText(Calculation.getNumber(trader.getTotalWeightOfEmptyCages()));
        TextView weightOfFullCages = view.findViewById(R.id.weight_of_full_cages);
        weightOfFullCages.setText(Calculation.getNumber(trader.getTotalWeightOfCages()));
        TextView numberOfChickens = view.findViewById(R.id.num_of_chickens);
        numberOfChickens.setText(String.valueOf(trader.getNumberOfChickens()));
        TextView priceOfKg = view.findViewById(R.id.price_of_kilo);
        priceOfKg.setText(Calculation.getNumber(trader.getPrice()));

        double chickensWeight = Calculation.getChickensWeight(trader.getTotalWeightOfCages(), trader.getTotalWeightOfEmptyCages());

        TextView average = view.findViewById(R.id.average);
        average.setText(Calculation.getNumber(Calculation.getAverage(chickensWeight, trader.getNumberOfChickens())));
        TextView weight = view.findViewById(R.id.weight);
        weight.setText(Calculation.getNumber(chickensWeight));
        TextView totalPrice = view.findViewById(R.id.total);
        totalPrice.setText(Calculation.formatNumberWithCommas(Calculation.getTotalPrice(chickensWeight, trader.getPrice())));
    }

    private void initializeButtons(View view) {
        Button done = view.findViewById(R.id.done);

        if (year == null && period == null) done.setVisibility(View.GONE);

        done.setOnClickListener(view1 -> {
            if ((periodData.getNumberOfAliveChickens() - periodData.getNumberOfSold()) - trader.getNumberOfChickens() >= 0) {
                Firebase.setTraderInTraderBranchFromTransactionBranchThatPeriodHave(requireContext(), year, period, trader);
                Firebase.setSale(requireContext(), year, period, new Sale(trader.getDate(), trader.getNumberOfChickens(), periodData.getNumberOfAliveChickens() - trader.getNumberOfChickens()));
                model.getPerson().observe(requireActivity(), person -> {
                    if (person != null) {
                        Optional<DatabaseReference> firstValue = person.values().stream().findFirst();
                        firstValue.ifPresent(value -> Firebase.setTransactionValueInTraderThatPersonHave(value, year, period));
                        Firebase.updateSoldValue(requireContext(), year, period, periodData.getNumberOfSold(), trader.getNumberOfChickens());
                        Vibrate.vibrate(requireContext());
                        Toast.makeText(requireContext(), getString(R.string.saved), Toast.LENGTH_SHORT).show();
                        if (mListener != null) {
                            mListener.onFinishActivity();
                        }
                    }
                });
            } else {
                new Handler(Looper.getMainLooper()).post(() -> new Alert(R.drawable.wrong, getString(R.string.no_enough_chickens)).show(getParentFragmentManager(), ""));
            }
        });
    }

    private void setupViewModel() {
        model = new ViewModelProvider(this).get(PersonViewModel.class);
        model.initialize(requireContext(), trader.getName());
    }

    private void setupPeriodViewModel() {
        periodViewMadel = new ViewModelProvider(this).get(PeriodViewMadel.class);
        periodViewMadel.initialize(requireContext(), year, period);
    }

    private void getPeriodData() {
        periodViewMadel.getPeriod().observe(requireActivity(), period -> periodData = period);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFinishActivity();
    }
}