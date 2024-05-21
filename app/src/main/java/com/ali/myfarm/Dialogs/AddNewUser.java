package com.ali.myfarm.Dialogs;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;

import com.ali.myfarm.Classes.Matcher;
import com.ali.myfarm.Classes.Vibrate;
import com.ali.myfarm.Models.Person;
import com.ali.myfarm.R;

public class AddNewUser extends DialogFragment {

    private AddNewUserListener listener;
    private EditText firstNameEditText, lastNameEditText, phoneEditText;
    private ConstraintLayout traderLayout, buyerLayout;
    private CheckBox traderCheckBox, buyerCheckBox;
    private Person.Type selectedType;

    public AddNewUser() {
    }

    public AddNewUser(AddNewUserListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = new Dialog(requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.add_new_user);

        initializeViews(dialog);
        setupConfirmButton(dialog);
        initializeChooseType(dialog);

        Window window = dialog.getWindow();
        if (window != null) {
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window.setGravity(Gravity.BOTTOM);
            window.setWindowAnimations(R.style.dialog_animation);
        }
        return dialog;
    }

    private void initializeViews(Dialog dialog) {
        firstNameEditText = dialog.findViewById(R.id.first_name);
        lastNameEditText = dialog.findViewById(R.id.last_name);
        phoneEditText = dialog.findViewById(R.id.phone);
    }

    private void setupConfirmButton(Dialog dialog) {
        Button confirmButton = dialog.findViewById(R.id.done);
        confirmButton.setOnClickListener(v -> {
            if (isValidInputs()) {
                listener.onDataEntered(firstNameEditText.getText().toString(),
                        lastNameEditText.getText().toString(),
                        phoneEditText.getText().toString(),
                        selectedType);
                dismiss();
            }
        });
    }

    private void initializeChooseType(Dialog dialog) {
        traderLayout = dialog.findViewById(R.id.trader);
        buyerLayout = dialog.findViewById(R.id.buyer);
        traderCheckBox = dialog.findViewById(R.id.trader_checkbox);
        buyerCheckBox = dialog.findViewById(R.id.buyer_checkbox);

        View.OnClickListener onClickListener = view -> {
            if (view.getId() == R.id.trader) {
                selectType(traderLayout, traderCheckBox, buyerLayout, buyerCheckBox, Person.Type.TRADER);
            } else {
                selectType(buyerLayout, buyerCheckBox, traderLayout, traderCheckBox, Person.Type.BUYER);
            }
        };

        traderLayout.setOnClickListener(onClickListener);
        buyerLayout.setOnClickListener(onClickListener);
    }

    private void selectType(ConstraintLayout selectedLayout, CheckBox selectedCheckBox,
                            ConstraintLayout deselectedLayout, CheckBox deselectedCheckBox,
                            Person.Type type) {
        selectedCheckBox.setChecked(true);
        selectedLayout.setBackground(AppCompatResources.getDrawable(requireContext(), R.drawable.green_stroke));

        deselectedCheckBox.setChecked(false);
        deselectedLayout.setBackground(AppCompatResources.getDrawable(requireContext(), R.drawable.gray_stroke));

        selectedType = type;
    }

    private boolean isValidInputs() {
        boolean isValid = true;

        if (selectedType == null) {
            setSelectionError(traderLayout, buyerLayout);
            isValid = false;
        }

        isValid &= setInputError(firstNameEditText, !Matcher.isUserName(firstNameEditText.getText().toString()));
        isValid &= setInputError(lastNameEditText, !Matcher.isUserName(lastNameEditText.getText().toString()));
        isValid &= setInputError(phoneEditText, !Matcher.isPhoneNumber(phoneEditText.getText().toString()));

        return isValid;
    }

    private void setSelectionError(ConstraintLayout traderLayout, ConstraintLayout buyerLayout) {
        traderLayout.setBackground(AppCompatResources.getDrawable(requireContext(), R.drawable.red_stroke_with_1dp_width));
        buyerLayout.setBackground(AppCompatResources.getDrawable(requireContext(), R.drawable.red_stroke_with_1dp_width));
        Vibrate.vibrate(requireContext());
    }

    private boolean setInputError(EditText editText, boolean isError) {
        if (isError) {
            editText.setBackground(AppCompatResources.getDrawable(requireContext(), R.drawable.error_p));
            Vibrate.vibrate(requireContext());
            showToast(getString(R.string.check_inputs));
            return false;
        } else {
            editText.setBackground(AppCompatResources.getDrawable(requireContext(), R.drawable.input_filed));
            return true;
        }
    }

    private void showToast(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }

    public interface AddNewUserListener {
        void onDataEntered(String firstName, String lastName, String phone, Person.Type type);
    }
}
