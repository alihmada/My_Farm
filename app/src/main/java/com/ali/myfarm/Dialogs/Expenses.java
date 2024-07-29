package com.ali.myfarm.Dialogs;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.DialogFragment;

import com.ali.myfarm.Classes.Matcher;
import com.ali.myfarm.Classes.Vibrate;
import com.ali.myfarm.R;

public class Expenses extends DialogFragment {
    EditText reason, price;
    private ExpensesDialogListener listener;

    public Expenses() {
    }

    public Expenses(ExpensesDialogListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = new Dialog(requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.expenses_dialog);

        initializeViews(dialog);
        setupConfirmButton(dialog);

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
        reason = dialog.findViewById(R.id.reason);
        price = dialog.findViewById(R.id.balance);
    }

    private void setupConfirmButton(Dialog dialog) {
        Button confirm = dialog.findViewById(R.id.done);
        confirm.setOnClickListener(v -> {
            String reason = this.reason.getText().toString().trim();
            String price = this.price.getText().toString().trim();
            if (isValidData(reason, price)) {
                listener.onDataEntered(reason, Double.parseDouble(price));
                dismiss();
            } else {
                handleInputErrors();
                Vibrate.vibrate(requireContext());
            }
        });
    }

    private boolean isValidData(String reason, String price) {
        return !reason.isEmpty() && Matcher.isFloatingNumber(price);
    }

    private void handleInputErrors() {
        validateAndSetBackground(reason, reason.getText().toString().trim().isEmpty());
        validateAndSetBackground(price, !Matcher.isFloatingNumber(price.getText().toString().trim()));
        Toast.makeText(requireContext(), requireContext().getString(R.string.check_inputs), Toast.LENGTH_SHORT).show();
    }

    private void validateAndSetBackground(EditText editText, boolean isValid) {
        if (isValid) {
            editText.setBackground(AppCompatResources.getDrawable(requireContext(), R.drawable.error_p));
        } else {
            editText.setBackground(AppCompatResources.getDrawable(requireContext(), R.drawable.input_filed));
        }
    }

    public interface ExpensesDialogListener {
        void onDataEntered(String reason, double balance);
    }
}
