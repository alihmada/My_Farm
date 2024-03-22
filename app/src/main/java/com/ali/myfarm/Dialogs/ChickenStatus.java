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

public class ChickenStatus extends DialogFragment {
    ChickenStatusDialogListener listener;
    EditText numberOfDead;

    public ChickenStatus() {
    }

    public ChickenStatus(ChickenStatusDialogListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = new Dialog(requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.checken_status_dialog);

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
        numberOfDead = dialog.findViewById(R.id.number_of_dead);
    }

    private void setupConfirmButton(Dialog dialog) {
        Button confirm = dialog.findViewById(R.id.done);
        confirm.setOnClickListener(v -> {
            String numberOfDead = this.numberOfDead.getText().toString();
            if (Matcher.isNumber(numberOfDead)) {
                listener.onDataEntered(Integer.parseInt(numberOfDead));
                dismiss();
            } else {
                handleInputErrors();
                Vibrate.vibrate(requireContext());
            }
        });
    }

    private void handleInputErrors() {
        validateAndSetBackground(numberOfDead, Matcher.isNumber(numberOfDead.getText().toString()));
        Toast.makeText(requireContext(), requireContext().getString(R.string.check_inputs), Toast.LENGTH_SHORT).show();
    }

    private void validateAndSetBackground(EditText editText, boolean isValid) {
        if (isValid) {
            editText.setBackground(AppCompatResources.getDrawable(requireContext(), R.drawable.error_p));
        } else {
            editText.setBackground(AppCompatResources.getDrawable(requireContext(), R.drawable.input_filed));
        }
    }

    public interface ChickenStatusDialogListener {
        void onDataEntered(int dead);
    }
}
