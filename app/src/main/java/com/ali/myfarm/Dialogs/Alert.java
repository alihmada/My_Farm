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

public class Alert extends DialogFragment {

    String message;
    int image;

    public Alert() {
    }

    public Alert(int image, String message) {
        this.image = image;
        this.message = message;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final Dialog dialog = new Dialog(requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.alert);

        ImageView image = dialog.findViewById(R.id.alert_shape);
        image.setImageResource(this.image);
        TextView message = dialog.findViewById(R.id.alert_message);
        message.setText(this.message);

        Button enable = dialog.findViewById(R.id.ok);
        enable.setOnClickListener(view -> dismiss());

        Window window = dialog.getWindow();
        if (window != null) {
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window.setGravity(Gravity.BOTTOM);
            window.setWindowAnimations(R.style.dialog_animation);
        }
        return dialog;
    }
}
