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
import com.ali.myfarm.Models.Electricity;
import com.ali.myfarm.R;

public class ElectricityDialog extends DialogFragment {
    ElectricityDialogListener listener;
    ConstraintLayout electricity, petrol;
    CheckBox electricityCheck, petrolCheck;
    EditText number, price;

    public ElectricityDialog() {
    }

    public ElectricityDialog(ElectricityDialogListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = new Dialog(requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.electricity_dialog);

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
        setupElectricityOption(dialog);
        setupPetrolOption(dialog);
        setupConfirmButton(dialog);
    }

    private void initializeEditText(Dialog dialog) {
        number = dialog.findViewById(R.id.number);
        price = dialog.findViewById(R.id.price);
    }

    private void setupEditText(String hintOfNumber, String hintOfPrice, int type, boolean isPetrol) {
        if (isPetrol) {
            number.setVisibility(View.VISIBLE);
            number.setHint(hintOfNumber);
            number.setInputType(type);
        } else {
            number.setVisibility(View.GONE);
        }

        price.setVisibility(View.VISIBLE);
        price.setHint(hintOfPrice);
    }


    private void setupElectricityOption(Dialog dialog) {
        electricity = dialog.findViewById(R.id.electricity);
        electricityCheck = dialog.findViewById(R.id.electricity_checkBox);

        electricity.setOnClickListener(v -> {
            setOptionSelected(electricity, electricityCheck, petrol, petrolCheck);
            setupEditText("", getString(R.string.enter_balance), InputType.TYPE_CLASS_NUMBER, false);
        });
    }

    private void setupPetrolOption(Dialog dialog) {
        petrol = dialog.findViewById(R.id.petrol);
        petrolCheck = dialog.findViewById(R.id.petrol_checkBox);

        petrol.setOnClickListener(v -> {
            setOptionSelected(petrol, petrolCheck, electricity, electricityCheck);
            setupEditText(getString(R.string.enter_a_number_of_liters), getString(R.string.enter_a_price_of_liters), InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL, true);
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
            String number = "1";
            if (petrolCheck.isChecked()) {
                number = String.valueOf(this.number.getText());
            }
            String price = String.valueOf(this.price.getText());

            if (isValidInput(number) && isValidInput(price)) {
                listener.onDataEntered(electricityCheck.isChecked() ? Electricity.Type.RECEIPT : Electricity.Type.PETROL, Double.parseDouble(number), DateAndTime.getCurrentDateTime(), Double.parseDouble(price));
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
        if (!electricityCheck.isChecked() && !petrolCheck.isChecked()) {
            electricity.setBackground(AppCompatResources.getDrawable(requireContext(), R.drawable.error_p));
            petrol.setBackground(AppCompatResources.getDrawable(requireContext(), R.drawable.error_p));
        }

        if (number.getVisibility() != View.GONE) {
            if (!isValidInput(number.getText().toString().trim())) {
                number.setBackground(AppCompatResources.getDrawable(requireContext(), R.drawable.error_p));
            } else {
                number.setBackground(AppCompatResources.getDrawable(requireContext(), R.drawable.input_filed));
            }
        }

        if (price.getVisibility() != View.GONE) {
            if (!isValidInput(price.getText().toString().trim())) {
                price.setBackground(AppCompatResources.getDrawable(requireContext(), R.drawable.error_p));
            } else {
                price.setBackground(AppCompatResources.getDrawable(requireContext(), R.drawable.input_filed));
            }
        }
    }

    public interface ElectricityDialogListener {
        void onDataEntered(Electricity.Type type, double number, String date, double price);
    }
}