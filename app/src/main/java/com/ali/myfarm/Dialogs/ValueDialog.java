package com.ali.myfarm.Dialogs;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.DialogFragment;

import com.ali.myfarm.Classes.Common;
import com.ali.myfarm.Classes.Vibrate;
import com.ali.myfarm.R;

public class ValueDialog extends DialogFragment {

    private BottomSheetListener listener;
    private int headerId, hintId;
    private inputType type;
    private String regex;
    private EditText text;

    public ValueDialog() {
    }

    public ValueDialog(int hintId, inputType type, String regex, BottomSheetListener listener) {
        this.hintId = hintId;
        this.type = type;
        this.regex = regex;
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = new Dialog(requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.enter_value);

        if (headerId == 0 && hintId == 0 && regex == null) {
            if (savedInstanceState != null) {
                int[] ints = savedInstanceState.getIntArray(Common.VALUE);
                if (ints != null) {
                    headerId = ints[0];
                    hintId = ints[1];
                }
                String regex = savedInstanceState.getString(Common.REGEX);
                assert regex != null;
                this.regex = regex;
            }
        }

        initialize(dialog);

        Window window = dialog.getWindow();
        if (window != null) {
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window.setGravity(Gravity.BOTTOM);
            window.setWindowAnimations(R.style.dialog_animation);
        }
        return dialog;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putIntArray(Common.VALUE, new int[]{headerId, hintId});
        outState.putString(Common.REGEX, regex);
        super.onSaveInstanceState(outState);
    }

    private void initialize(Dialog dialog) {
        setupEditText(dialog);
        setupButton(dialog);
    }

    private void setupEditText(Dialog dialog) {
        text = dialog.findViewById(R.id.input_field);
        if (type == inputType.PHONE) text.setInputType(InputType.TYPE_CLASS_PHONE);
        else if (type == inputType.TEXT) text.setInputType(InputType.TYPE_CLASS_TEXT);
        else if (type == inputType.NUMBER) text.setInputType(InputType.TYPE_CLASS_NUMBER);
        else text.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        text.setHint(getString(hintId));
    }

    private void setupButton(Dialog dialog) {
        Button confirm = dialog.findViewById(R.id.done);

        confirm.setOnClickListener(view -> {
            String edited = String.valueOf(text.getText());
            if (!edited.isEmpty() && validateAndSetValue(edited)) {
                listener.onDataEntered(edited);
                dialog.dismiss();
            } else {
                text.setBackground(AppCompatResources.getDrawable(requireContext(), R.drawable.error_p));
                Vibrate.vibrate(requireContext());
            }
        });
    }

    private boolean validateAndSetValue(String edited) {
        return edited.matches(regex);
    }

    public enum inputType {
        PHONE, DECIMAL, TEXT, NUMBER
    }

    public interface BottomSheetListener {
        void onDataEntered(String text);
    }

    public interface OnValueSetListener {
        void onValueSet(String text);
    }
}
