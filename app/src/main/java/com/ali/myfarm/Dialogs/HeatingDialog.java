package com.ali.myfarm.Dialogs;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;

import com.ali.myfarm.Classes.Common;
import com.ali.myfarm.Classes.DateAndTime;
import com.ali.myfarm.Classes.Vibrate;
import com.ali.myfarm.Models.Heating;
import com.ali.myfarm.R;

public class HeatingDialog extends DialogFragment {
    HeatingDialogListener listener;
    ConstraintLayout gas, diesel;
    CheckBox gasCheck, dieselCheck;
    EditText number, price;

    public HeatingDialog() {
    }

    public HeatingDialog(HeatingDialogListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = new Dialog(requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.heating_dialog);

        initializeView(dialog);

        Window window = dialog.getWindow();
        if (window != null) {
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window.setGravity(Gravity.BOTTOM);
            window.setWindowAnimations(R.style.dialog_animation);
        }
        return dialog;
    }

    private void initializeView(Dialog dialog) {
        initializeEditText(dialog);
        setupGasOption(dialog);
        setupDieselOption(dialog);
        setupConfirmButton(dialog);
    }

    private void initializeEditText(Dialog dialog) {
        number = dialog.findViewById(R.id.number);
        price = dialog.findViewById(R.id.price);
    }

    private void setupEditText(String hintOfNumber, String hintOfPrice, int type) {
        if (number.getVisibility() == View.GONE && price.getVisibility() == View.GONE) {
            number.setVisibility(View.VISIBLE);
            price.setVisibility(View.VISIBLE);
        }

        number.setHint(hintOfNumber);
        number.setInputType(type);
        price.setHint(hintOfPrice);
    }

    private void setupGasOption(Dialog dialog) {
        gas = dialog.findViewById(R.id.gas);
        gasCheck = dialog.findViewById(R.id.gas_checkBox);

        gas.setOnClickListener(v -> {
            setOptionSelected(gas, gasCheck, diesel, dieselCheck);
            setupEditText(getString(R.string.enter_a_number_of_cylinders), getString(R.string.enter_a_price_of_cylinders), InputType.TYPE_CLASS_NUMBER);
        });
    }

    private void setupDieselOption(Dialog dialog) {
        diesel = dialog.findViewById(R.id.diesel);
        dieselCheck = dialog.findViewById(R.id.diesie_checkBox);

        diesel.setOnClickListener(v -> {
            setOptionSelected(diesel, dieselCheck, gas, gasCheck);
            setupEditText(getString(R.string.enter_a_number_of_liters), getString(R.string.enter_a_price_of_liters), InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        });
    }

    private void setOptionSelected(ConstraintLayout selectedLayout, CheckBox selectedCheckBox, ConstraintLayout deselectedLayout, CheckBox deselectedCheckBox) {
        selectedCheckBox.setChecked(true);
        selectedLayout.setBackground(AppCompatResources.getDrawable(requireContext(), R.drawable.green_stroke));

        deselectedCheckBox.setChecked(false);
        deselectedLayout.setBackground(AppCompatResources.getDrawable(requireContext(), R.drawable.gray_stroke));
    }

    private void setupConfirmButton(Dialog view) {
        Button confirm = view.findViewById(R.id.confirm);
        confirm.setOnClickListener(v -> {
            String number = String.valueOf(this.number.getText());
            String price = String.valueOf(this.price.getText());

            if (isValidInput(number) && isValidInput(price)) {
                listener.onDataEntered(gasCheck.isChecked() ? Heating.Type.GAS : Heating.Type.DIESEL, Double.parseDouble(number), DateAndTime.getCurrentDateTime(), Double.parseDouble(price));
                dismiss();
            } else {
                handleInputErrors();
                Vibrate.vibrate(requireContext());
            }
        });
    }

    private boolean isValidInput(String value) {
        return value.matches(Common.DECIMAL_REGEX);
    }

    private void handleInputErrors() {
        if (!gasCheck.isChecked() && !dieselCheck.isChecked()) {
            gas.setBackground(AppCompatResources.getDrawable(requireContext(), R.drawable.error_p));
            diesel.setBackground(AppCompatResources.getDrawable(requireContext(), R.drawable.error_p));
        }

        if (number.getVisibility() != View.GONE) {
            if (isValidInput(number.getText().toString())) {
                number.setBackground(AppCompatResources.getDrawable(requireContext(), R.drawable.error_p));
            } else {
                number.setBackground(AppCompatResources.getDrawable(requireContext(), R.drawable.input_filed));
            }
        }

        if (price.getVisibility() != View.GONE) {
            if (isValidInput(price.getText().toString())) {
                price.setBackground(AppCompatResources.getDrawable(requireContext(), R.drawable.error_p));
            } else {
                price.setBackground(AppCompatResources.getDrawable(requireContext(), R.drawable.input_filed));
            }
        }
    }

    public interface HeatingDialogListener {
        void onDataEntered(Heating.Type type, double number, String date, double price);
    }
}