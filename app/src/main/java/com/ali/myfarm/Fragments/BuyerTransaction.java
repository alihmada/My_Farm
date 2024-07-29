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
import com.ali.myfarm.Models.Buyer;
import com.ali.myfarm.Models.Person;
import com.ali.myfarm.R;
import com.google.android.material.card.MaterialCardView;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class BuyerTransaction extends Fragment {

    private Bundle bundle;
    private Buyer transaction;
    private int inFocus, outFocus;
    private String mainID, periodID;

    public BuyerTransaction() {
    }

    public BuyerTransaction(String mainID, String periodID) {
        this.mainID = mainID;
        this.periodID = periodID;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_buyer_transaction, container, false);

        bundle = new Bundle();
        setupShowBill(view);
        addBuyer(view);
        handelInputs(view);

        return view;
    }

    private void setupShowBill(View view) {
        Gson gson = new Gson();
        Button showBill = view.findViewById(R.id.show_bill);
        showBill.setOnClickListener(v -> {
            if (isValidInputs(view)) {
                Intent intent = new Intent(requireContext(), Bill.class);
                intent.putExtra(Common.MOVED_DATA, gson.toJson(transaction));
                bundle.putString(Common.YEAR, mainID);
                bundle.putString(Common.MONTH, periodID);
                bundle.putBoolean(Common.IS_TRADER, false);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    private void addBuyer(View view) {
        MaterialCardView cardView = view.findViewById(R.id.transaction_person);
        TextView hint = view.findViewById(R.id.transaction_hint);
        TextView userName = view.findViewById(R.id.name);
        ConstraintLayout add = view.findViewById(R.id.press_to_add_buyer);
        add.setOnClickListener(v -> new Handler(Looper.getMainLooper()).post(() -> {
            SelectPerson person = new SelectPerson(Person.Type.BUYER, (name) -> {
                cardView.setVisibility(View.VISIBLE);
                hint.setVisibility(View.GONE);
                userName.setText(name);
            });
            person.show(getParentFragmentManager(), "");
        }));
    }

    private void handelInputs(View view) {
        Map<EditText, ConstraintLayout> editTextBackgroundMap = new HashMap<>();
        editTextBackgroundMap.put(view.findViewById(R.id.num_of_chicken), view.findViewById(R.id.number_of_chicken));
        editTextBackgroundMap.put(view.findViewById(R.id.weight_of_chicken), view.findViewById(R.id.weight_of_chickens));

        inFocus = R.drawable.stroke_with_2dp_width;
        outFocus = R.drawable.input_filed;

        for (Map.Entry<EditText, ConstraintLayout> entry : editTextBackgroundMap.entrySet()) {
            EditText editText = entry.getKey();
            ConstraintLayout backgroundLayout = entry.getValue();

            editText.setOnFocusChangeListener((v, hasFocus) -> changeBackground(backgroundLayout, hasFocus ? inFocus : outFocus));
        }

        Map<ImageButton, EditText> imageButtonEditTextMap = new HashMap<>();
        imageButtonEditTextMap.put(view.findViewById(R.id.no_of_chickens_equal), view.findViewById(R.id.num_of_chicken));
        imageButtonEditTextMap.put(view.findViewById(R.id.weight_of_chickens_equal), view.findViewById(R.id.weight_of_chicken));

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
        ConstraintLayout pressToAdd = view.findViewById(R.id.press_to_add_buyer);
        ConstraintLayout numberOfChickensParent = view.findViewById(R.id.number_of_chicken);
        ConstraintLayout weightOfChickensParent = view.findViewById(R.id.weight_of_chickens);

        TextView name = view.findViewById(R.id.name);
        EditText numberOfChickens = view.findViewById(R.id.num_of_chicken);
        EditText weightOfChickens = view.findViewById(R.id.weight_of_chicken);
        EditText kgPrice = view.findViewById(R.id.price_of_kg);

        boolean isValidName = setInputError(Matcher.isUserName(String.valueOf(name.getText())), pressToAdd);
        boolean isValidNumberOfCages = setInputError(numberOfChickensParent, !Matcher.isNumber(String.valueOf(numberOfChickens.getText())));
        boolean isValidWeightOfEmptyCages = setInputError(weightOfChickensParent, !Matcher.isFloatingNumber(String.valueOf(weightOfChickens.getText())));
        boolean isValidKgPrice = setInputError(kgPrice, !Matcher.isFloatingNumber(String.valueOf(kgPrice.getText())));

        boolean isValid = isValidName && isValidNumberOfCages && isValidWeightOfEmptyCages && isValidKgPrice;

        if (isValid) {
            int numOfChickens = Integer.parseInt(numberOfChickens.getText().toString());
            double chickensWeight = Double.parseDouble(weightOfChickens.getText().toString());
            double priceKg = Double.parseDouble(kgPrice.getText().toString());

            String dateTime = DateAndTime.getCurrentDateTime();

            transaction = new Buyer(dateTime, priceKg, name.getText().toString(), numOfChickens, chickensWeight);
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