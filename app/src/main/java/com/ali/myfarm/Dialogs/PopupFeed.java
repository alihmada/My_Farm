package com.ali.myfarm.Dialogs;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.ali.myfarm.R;

public class PopupFeed extends DialogFragment {

    ImageView image;
    TextView total, used, still;

    int photo;
    String totalTxt, usedTxt, stillTxt;

    public PopupFeed() {
    }

    public PopupFeed(int image, String total, String used, String still) {
        this.photo = image;
        this.totalTxt = total;
        this.usedTxt = used;
        this.stillTxt = still;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = new Dialog(requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.feed_details);

        initializeViews(dialog);

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
        image = dialog.findViewById(R.id.image);
        image.setImageResource(photo);
        total = dialog.findViewById(R.id.total);
        total.setText(totalTxt);
        used = dialog.findViewById(R.id.used);
        used.setText(usedTxt);
        still = dialog.findViewById(R.id.still);
        still.setText(stillTxt);
    }
}
