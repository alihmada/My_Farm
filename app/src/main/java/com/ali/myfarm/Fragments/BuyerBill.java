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
import com.ali.myfarm.MVVM.PeriodViewModel;
import com.ali.myfarm.MVVM.PersonViewModel;
import com.ali.myfarm.MVVM.TransactionViewModel;
import com.ali.myfarm.Models.Buyer;
import com.ali.myfarm.Models.Period;
import com.ali.myfarm.Models.Sale;
import com.ali.myfarm.Models.Transaction;
import com.ali.myfarm.R;
import com.google.gson.Gson;

public class BuyerBill extends Fragment {

    private Transaction transaction;
    private double chickensPrice;
    private Buyer buyer;
    private Period periodData;
    private TransactionViewModel transactionViewModel;
    private PersonViewModel personViewModel;
    private PeriodViewModel periodViewModel;
    private String year;
    private String period;
    private String buyerData;
    private OnFragmentInteractionListener listener;

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
        if (context instanceof OnFragmentInteractionListener) {
            listener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_buyer_bill, container, false);

        parseBuyerData();
        setupViews(view);

        if (year != null && period != null) {
            setupViewModels();
            observePeriodData();
            observeTransactionData();
        }

        setupButtons(view);

        return view;
    }

    private void parseBuyerData() {
        buyer = new Gson().fromJson(buyerData, Buyer.class);
    }

    private void setupViews(View view) {
        TextView name = view.findViewById(R.id.name);
        TextView weight = view.findViewById(R.id.weight);
        TextView numberOfChickens = view.findViewById(R.id.num_of_chickens);
        TextView priceOfKg = view.findViewById(R.id.price_of_kilo);
        TextView average = view.findViewById(R.id.average);
        TextView totalPrice = view.findViewById(R.id.total);

        name.setText(buyer.getName());
        weight.setText(Calculation.getNumber(buyer.getWeightOfChickens()));
        numberOfChickens.setText(String.valueOf(buyer.getNumberOfChickens()));
        priceOfKg.setText(Calculation.getNumber(buyer.getPrice()));
        average.setText(Calculation.getNumber(Calculation.getAverage(buyer.getWeightOfChickens(), buyer.getNumberOfChickens())));

        chickensPrice = Calculation.getTotalPrice(buyer.getWeightOfChickens(), buyer.getPrice());

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
        if ((periodData.getNumberOfAliveChickens() - periodData.getNumberOfSold()) - buyer.getNumberOfChickens() >= 0) {
            Firebase.setBuyerInTransactionBranchThatPeriodHave(requireContext(), year, period, buyer);
            Firebase.setSale(requireContext(), year, period, new Sale(buyer.getDate(), buyer.getNumberOfChickens(), periodData.getNumberOfAliveChickens() - buyer.getNumberOfChickens()));
            personViewModel.getPerson().observe(requireActivity(), person -> {
                if (person != null) {
                    person.values().stream().findFirst().ifPresent(value -> {
                        Firebase.setTransactionValueInBuyerThatPersonHave(value, year, period);
                        Firebase.updateSoldValue(requireContext(), year, period, periodData.getNumberOfSold(), buyer.getNumberOfChickens());
                        Firebase.updateWeightAndPriceForBuyer(requireContext(), year, period, transaction.getWeightForBuyers(), buyer.getWeightOfChickens(), transaction.getPriceForBuyers(), chickensPrice);
                        Vibrate.vibrate(requireContext());
                        Toast.makeText(requireContext(), getString(R.string.saved), Toast.LENGTH_SHORT).show();
                        if (listener != null) {
                            listener.onFinishActivity();
                        }
                    });
                }
            });
        } else {
            showAlert();
        }
    }

    private void showAlert() {
        new Handler(Looper.getMainLooper()).post(() -> new Alert(R.drawable.wrong, getString(R.string.no_enough_chickens)).show(getParentFragmentManager(), ""));
    }

    private void setupViewModels() {
        personViewModel = new ViewModelProvider(this).get(PersonViewModel.class);
        transactionViewModel = new ViewModelProvider(this).get(TransactionViewModel.class);
        periodViewModel = new ViewModelProvider(this).get(PeriodViewModel.class);

        personViewModel.initialize(requireContext(), buyer.getName());
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

    public interface OnFragmentInteractionListener {
        void onFinishActivity();
    }
}