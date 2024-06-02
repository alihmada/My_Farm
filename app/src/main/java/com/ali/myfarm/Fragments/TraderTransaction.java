package com.ali.myfarm.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.ali.myfarm.Activities.Bill;
import com.ali.myfarm.Classes.Calculation;
import com.ali.myfarm.Classes.Common;
import com.ali.myfarm.Classes.DateAndTime;
import com.ali.myfarm.Classes.Matcher;
import com.ali.myfarm.Classes.Vibrate;
import com.ali.myfarm.Dialogs.SelectPerson;
import com.ali.myfarm.Models.Person;
import com.ali.myfarm.Models.Trader;
import com.ali.myfarm.R;
import com.google.android.material.card.MaterialCardView;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class TraderTransaction extends Fragment {

    private Bundle bundle;
    private Trader transaction;
    private int inFocus, outFocus;
    private String mainID, periodID;

    public TraderTransaction() {
    }

    public TraderTransaction(String mainID, String periodID) {
        this.mainID = mainID;
        this.periodID = periodID;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trader_transaction, container, false);

        bundle = new Bundle();
        setupShowBill(view);
        addTrader(view);
        handelInputs(view);

        return view;
    }

    private void setupShowBill(View view) {
        Gson gson = new Gson();
        Button showBill = view.findViewById(R.id.show_bill);
        showBill.setOnClickListener(v -> {
            if (isValidInputs(view)) {
                Intent intent = new Intent(requireContext(), Bill.class);
                bundle.putString(Common.MOVED_DATA, gson.toJson(transaction));
                bundle.putString(Common.PERIOD_ID, periodID);
                bundle.putBoolean(Common.IS_TRADER, true);
                bundle.putString(Common.MAIN_ID, mainID);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    private void addTrader(View view) {
        MaterialCardView cardView = view.findViewById(R.id.transaction_person);
        TextView hint = view.findViewById(R.id.transaction_hint);
        TextView userName = view.findViewById(R.id.name);
        ConstraintLayout add = view.findViewById(R.id.press_to_add_trader);
        add.setOnClickListener(v -> new Handler(Looper.getMainLooper()).post(() -> {
            SelectPerson person = new SelectPerson(Person.Type.TRADER, (name) -> {
                cardView.setVisibility(View.VISIBLE);
                hint.setVisibility(View.GONE);
                userName.setText(name);
            });
            person.show(getParentFragmentManager(), "");
        }));
    }

    private void handelInputs(View view) {
        Map<EditText, ConstraintLayout> editTextBackgroundMap = new HashMap<>();
        editTextBackgroundMap.put(view.findViewById(R.id.no_of_cages), view.findViewById(R.id.total_num_of_cages));
        editTextBackgroundMap.put(view.findViewById(R.id.no_of_empty_cages), view.findViewById(R.id.total_num_of_empty_cages));
        editTextBackgroundMap.put(view.findViewById(R.id.no_of_full_cages), view.findViewById(R.id.total_num_of_full_cages));
        editTextBackgroundMap.put(view.findViewById(R.id.total_num_of_chicken), view.findViewById(R.id.total_num_of_chickens));

        inFocus = R.drawable.stroke_with_2dp_width;
        outFocus = R.drawable.input_filed;

        for (Map.Entry<EditText, ConstraintLayout> entry : editTextBackgroundMap.entrySet()) {
            EditText editText = entry.getKey();
            ConstraintLayout backgroundLayout = entry.getValue();

            editText.setOnFocusChangeListener((v, hasFocus) -> changeBackground(backgroundLayout, hasFocus ? inFocus : outFocus));
        }

        Map<ImageButton, EditText> imageButtonEditTextMap = new HashMap<>();
        imageButtonEditTextMap.put(view.findViewById(R.id.no_of_cages_equal), view.findViewById(R.id.no_of_cages));
        imageButtonEditTextMap.put(view.findViewById(R.id.no_of_empty_cages_equal), view.findViewById(R.id.no_of_empty_cages));
        imageButtonEditTextMap.put(view.findViewById(R.id.no_of_full_cages_equal), view.findViewById(R.id.no_of_full_cages));
        imageButtonEditTextMap.put(view.findViewById(R.id.total_num_of_chickens_equal), view.findViewById(R.id.total_num_of_chicken));

        for (Map.Entry<ImageButton, EditText> entry : imageButtonEditTextMap.entrySet()) {
            ImageButton imageButton = entry.getKey();
            EditText editText = entry.getValue();

            imageButton.setOnClickListener(v -> {
                String expression = String.valueOf(editText.getText());
                editText.setText(Calculation.evaluateExpression(expression));
            });

            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    imageButton.setVisibility(Matcher.isOperation(s.toString()) ? View.VISIBLE : View.GONE);
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
        }
    }

    private boolean isValidInputs(View view) {
        ConstraintLayout pressToAdd = view.findViewById(R.id.press_to_add_trader);
        ConstraintLayout numberOfCagesParent = view.findViewById(R.id.total_num_of_cages);
        ConstraintLayout weightOfEmptyCagesParent = view.findViewById(R.id.total_num_of_empty_cages);
        ConstraintLayout weightOfFullCagesParent = view.findViewById(R.id.total_num_of_full_cages);
        ConstraintLayout numberOfChickensParent = view.findViewById(R.id.total_num_of_chickens);

        TextView name = view.findViewById(R.id.name);
        EditText numberOfCages = view.findViewById(R.id.no_of_cages);
        EditText weightOfEmptyCages = view.findViewById(R.id.no_of_empty_cages);
        EditText weightOfFullCages = view.findViewById(R.id.no_of_full_cages);
        EditText numberOfChicken = view.findViewById(R.id.total_num_of_chicken);
        EditText kgPrice = view.findViewById(R.id.kg_price);

        boolean isValidName = setInputError(!Matcher.isUserName(String.valueOf(name.getText())), pressToAdd);
        boolean isValidNumberOfCages = setInputError(numberOfCagesParent, !Matcher.isNumber(String.valueOf(numberOfCages.getText())));
        boolean isValidWeightOfEmptyCages = setInputError(weightOfEmptyCagesParent, !Matcher.isFloatingNumber(String.valueOf(weightOfEmptyCages.getText())));
        boolean isValidWeightOfFullCages = setInputError(weightOfFullCagesParent, !Matcher.isFloatingNumber(String.valueOf(weightOfFullCages.getText())));
        boolean isValidNumberOfChicken = setInputError(numberOfChickensParent, !Matcher.isNumber(String.valueOf(numberOfChicken.getText())));
        boolean isValidKgPrice = setInputError(kgPrice, !Matcher.isFloatingNumber(String.valueOf(kgPrice.getText())));

        boolean isValid = isValidName && isValidNumberOfCages && isValidWeightOfEmptyCages && isValidWeightOfFullCages && isValidNumberOfChicken && isValidKgPrice;

        if (isValid) {
            int numOfCages = Integer.parseInt(numberOfCages.getText().toString());
            double weightEmpty = Double.parseDouble(weightOfEmptyCages.getText().toString());
            double weightFull = Double.parseDouble(weightOfFullCages.getText().toString());
            int numOfChicken = Integer.parseInt(numberOfChicken.getText().toString());
            double priceKg = Double.parseDouble(kgPrice.getText().toString());

            double chickenWeight = Calculation.getChickensWeight(weightFull, weightEmpty);
            String dateTime = DateAndTime.getCurrentDateTime();

            transaction = new Trader(name.getText().toString(), numOfCages, weightEmpty, weightFull, numOfChicken, priceKg, chickenWeight, dateTime);
        }

        return isValid;
    }

    private boolean setInputError(boolean isError, ConstraintLayout constraintLayout) {
        if (isError) {
            constraintLayout.setBackground(AppCompatResources.getDrawable(requireContext(), R.drawable.select_user_error));
            Vibrate.vibrate(requireContext());
            showToast(getString(R.string.check_inputs));
            return false;
        }
        return true;
    }

    private boolean setInputError(View view, boolean isError) {
        if (isError) {
            view.setBackground(AppCompatResources.getDrawable(requireContext(), R.drawable.error_p));
            Vibrate.vibrate(requireContext());
            inFocus = R.drawable.red_stroke_with_2dp_width;
            outFocus = R.drawable.red_stroke_with_1dp_width;
            showToast(getString(R.string.check_inputs));
            return false;
        } else {
            view.setBackground(AppCompatResources.getDrawable(requireContext(), R.drawable.input_filed));
            inFocus = R.drawable.stroke_with_2dp_width;
            outFocus = R.drawable.input_filed;
            return true;
        }
    }

    private void showToast(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void changeBackground(ConstraintLayout layout, int stroke) {
        layout.setBackground(AppCompatResources.getDrawable(requireContext(), stroke));
    }
}