package com.ali.myfarm.Dialogs;

import android.app.ProgressDialog;
import android.content.Context;

import com.ali.myfarm.R;

public class Loading {
    private static ProgressDialog progressDialog;

    public static void progressDialogConstructor(Context context) {
        progressDialog = new ProgressDialog(context);
    }

    public static void showProgressDialog() {
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow()
                .setBackgroundDrawableResource(android.R.color.transparent);
    }

    public static void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}
