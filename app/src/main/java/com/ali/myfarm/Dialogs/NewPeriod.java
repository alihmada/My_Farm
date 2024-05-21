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

public class NewPeriod extends DialogFragment {
    EditText numberOfChickens, priceOfAChick;
    private NewPeriodDialogListener listener;

    public NewPeriod() {
    }

    public NewPeriod(NewPeriodDialogListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = new Dialog(requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.add_period_dialog);

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
        numberOfChickens = dialog.findViewById(R.id.num_of_chicks);
        priceOfAChick = dialog.findViewById(R.id.price_of_chicks);
    }

    private void setupConfirmButton(Dialog dialog) {
        Button confirm = dialog.findViewById(R.id.done);
        confirm.setOnClickListener(v -> {
            String numberOfChickens = this.numberOfChickens.getText().toString();
            String priceOfAChick = this.priceOfAChick.getText().toString();
            if (isValidData(numberOfChickens, priceOfAChick)) {
                listener.onDataEntered(numberOfChickens, priceOfAChick);
                dismiss();
            } else {
                handleInputErrors();
                Vibrate.vibrate(requireContext());
            }
        });
    }

    private boolean isValidData(String count, String price) {
        return Matcher.isNumber(count) && Matcher.isFloatingNumber(price);
    }

    private void handleInputErrors() {
        validateAndSetBackground(numberOfChickens, Matcher.isNumber(numberOfChickens.getText().toString()));
        validateAndSetBackground(priceOfAChick, Matcher.isFloatingNumber(priceOfAChick.getText().toString()));
        Toast.makeText(requireContext(), requireContext().getString(R.string.check_inputs), Toast.LENGTH_SHORT).show();
    }

    private void validateAndSetBackground(EditText editText, boolean isValid) {
        if (isValid) {
            editText.setBackground(AppCompatResources.getDrawable(requireContext(), R.drawable.error_p));
        } else {
            editText.setBackground(AppCompatResources.getDrawable(requireContext(), R.drawable.input_filed));
        }
    }

    public interface NewPeriodDialogListener {
        void onDataEntered(String count, String price);
    }
}
