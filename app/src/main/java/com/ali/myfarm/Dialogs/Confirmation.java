package com.ali.myfarm.Dialogs;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.ali.myfarm.R;

public class Confirmation extends DialogFragment {

    private String message;
    private int image;
    private OnOkCLickedListener listener;

    public Confirmation() {
    }

    public Confirmation(OnOkCLickedListener listener) {
        this.listener = listener;
    }

    public Confirmation(int image, String message, OnOkCLickedListener listener) {
        this.listener = listener;
        this.image = image;
        this.message = message;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final Dialog dialog = new Dialog(requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.confirmation);

        if (image != 0 && message != null) {
            ImageView image = dialog.findViewById(R.id.alert_shape);
            image.setImageResource(this.image);
            TextView message = dialog.findViewById(R.id.alert_message);
            message.setText(this.message);
        }

        Button ok = dialog.findViewById(R.id.ok);
        ok.setOnClickListener(view -> {
            if (listener != null)
                listener.onOkClicked();
            dismiss();
        });

        Button cancel = dialog.findViewById(R.id.cancel);
        cancel.setOnClickListener(view -> dismiss());

        Window window = dialog.getWindow();
        if (window != null) {
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window.setGravity(Gravity.BOTTOM);
            window.setWindowAnimations(R.style.dialog_animation);
        }
        return dialog;
    }

    public interface OnOkCLickedListener {
        void onOkClicked();
    }
}
