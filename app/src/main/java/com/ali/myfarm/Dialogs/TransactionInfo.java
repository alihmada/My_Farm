package com.ali.myfarm.Dialogs;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.ali.myfarm.Classes.Calculation;
import com.ali.myfarm.Models.Transaction;
import com.ali.myfarm.R;

public class TransactionInfo extends DialogFragment {

    private Transaction transaction;

    public TransactionInfo() {
    }

    public TransactionInfo(Transaction transaction) {
        this.transaction = transaction;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = new Dialog(requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.transaction_info_dialog);

        if (transaction != null) initialize(dialog);

        Window window = dialog.getWindow();
        if (window != null) {
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window.setGravity(Gravity.BOTTOM);
            window.setWindowAnimations(R.style.dialog_animation);
        }
        return dialog;
    }

    private void initialize(Dialog dialog) {
        ((TextView)dialog.findViewById(R.id.weight_of_traders)).setText(Calculation.getNumber(transaction.getWeightForTraders()));
        ((TextView)dialog.findViewById(R.id.price_of_traders)).setText(Calculation.formatNumberWithCommas(transaction.getPriceForTraders()));
        ((TextView)dialog.findViewById(R.id.weight_of_buyers)).setText(Calculation.getNumber(transaction.getWeightForBuyers()));
        ((TextView)dialog.findViewById(R.id.price_of_buyers)).setText(Calculation.formatNumberWithCommas(transaction.getPriceForBuyers()));
        ((TextView)dialog.findViewById(R.id.weight)).setText(Calculation.getNumber(transaction.getTotalWeight()));
        ((TextView)dialog.findViewById(R.id.price)).setText(Calculation.formatNumberWithCommas(transaction.getTotalPrice()));
    }
}
