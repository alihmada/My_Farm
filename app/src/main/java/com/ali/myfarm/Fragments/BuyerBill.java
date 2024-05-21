package com.ali.myfarm.Fragments;

import android.content.Context;
import android.os.Bundle;
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
import com.ali.myfarm.MVVM.PeriodViewMadel;
import com.ali.myfarm.MVVM.PersonViewModel;
import com.ali.myfarm.Models.Buyer;
import com.ali.myfarm.Models.Period;
import com.ali.myfarm.Models.Sale;
import com.ali.myfarm.R;
import com.google.firebase.database.DatabaseReference;
import com.google.gson.Gson;

import java.util.Optional;

public class BuyerBill extends Fragment {
    private Buyer buyer;
    private Period periodData;
    private PersonViewModel model;
    private PeriodViewMadel periodViewMadel;
    private String year, period, buyerData;
    private OnFragmentInteractionListener mListener;


    public BuyerBill() {
        // Required empty public constructor
    }

    public BuyerBill(String year, String period, String buyerData) {
        this.year = year;
        this.period = period;
        this.buyerData = buyerData;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof TraderBill.OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_buyer_bill, container, false);

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
        buyer = gson.fromJson(buyerData, Buyer.class);
    }

    private void setupViews(View view) {
        TextView name = view.findViewById(R.id.name);
        name.setText(buyer.getName());
        TextView weight = view.findViewById(R.id.weight);
        weight.setText(Calculation.getNumber(buyer.getWeightOfChickens()));
        TextView numberOfChickens = view.findViewById(R.id.num_of_chickens);
        numberOfChickens.setText(String.valueOf(buyer.getNumberOfChickens()));
        TextView priceOfKg = view.findViewById(R.id.price_of_kilo);
        priceOfKg.setText(Calculation.getNumber(buyer.getPrice()));

        TextView average = view.findViewById(R.id.average);
        average.setText(Calculation.getNumber(Calculation.getAverage(buyer.getWeightOfChickens(), buyer.getNumberOfChickens())));
        TextView totalPrice = view.findViewById(R.id.total);
        totalPrice.setText(Calculation.formatNumberWithCommas(Calculation.getTotalPrice(buyer.getWeightOfChickens(), buyer.getPrice())));
    }

    private void initializeButtons(View view) {
        Button done = view.findViewById(R.id.done);

        if (year == null && period == null) done.setVisibility(View.GONE);

        done.setOnClickListener(view1 -> {
            if ((periodData.getNumberOfAliveChickens() - periodData.getNumberOfSold()) - buyer.getNumberOfChickens() >= 0) {
                Firebase.setBuyerInTransactionBranchThatPeriodHave(requireContext(), year, period, buyer);
                Firebase.setSale(requireContext(), year, period, new Sale(buyer.getDate(), buyer.getNumberOfChickens(), periodData.getNumberOfAliveChickens() - buyer.getNumberOfChickens()));
                model.getPerson().observe(requireActivity(), person -> {
                    if (person != null) {
                        Optional<DatabaseReference> firstValue = person.values().stream().findFirst();
                        firstValue.ifPresent(value -> Firebase.setTransactionValueInBuyerThatPersonHave(value, year, period));
                        Firebase.updateSoldValue(requireContext(), year, period, periodData.getNumberOfSold(), buyer.getNumberOfChickens());
                        Vibrate.vibrate(requireContext());
                        Toast.makeText(requireContext(), getString(R.string.saved), Toast.LENGTH_SHORT).show();
                        if (mListener != null) {
                            mListener.onFinishActivity();
                        }
                    }

                });
            }
        });
    }

    private void setupViewModel() {
        model = new ViewModelProvider(this).get(PersonViewModel.class);
        model.initialize(requireContext(), buyer.getName());
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