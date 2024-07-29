package com.ali.myfarm.Dialogs;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.DialogFragment;

import com.ali.myfarm.Classes.Capture;
import com.ali.myfarm.Classes.Ciphering;
import com.ali.myfarm.Classes.Vibrate;
import com.ali.myfarm.Data.Firebase;
import com.ali.myfarm.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.util.Objects;

public class AddNewFarm extends DialogFragment {

    private AddNewFarmListener listener;
    private EditText root, name;
    private final ActivityResultLauncher<ScanOptions> scanOptionsActivityResultLauncher = registerForActivityResult(new ScanContract(), result -> {
        if (result.getContents() != null) {
            root.setText(result.getContents());
        }
    });

    public AddNewFarm() {
    }

    public AddNewFarm(AddNewFarmListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = new Dialog(requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.farm_dialog);

        initializeViews(dialog);
        setupButtons(dialog);
        Loading.progressDialogConstructor(requireContext());

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
        root = dialog.findViewById(R.id.root);
        name = dialog.findViewById(R.id.name);
    }

    private void setupButtons(Dialog dialog) {
        dialog.findViewById(R.id.qr_scanner).setOnClickListener(v -> {
            ScanOptions scanOptions = new ScanOptions().setBeepEnabled(true).setOrientationLocked(true).setCaptureActivity(Capture.class);

            scanOptionsActivityResultLauncher.launch(scanOptions);
        });
        dialog.findViewById(R.id.done).setOnClickListener(v -> {
            if (isValidInputs()) {
                Loading.showProgressDialog();
                try {
                    validateFarmID(Ciphering.decrypt(root.getText().toString().trim()), name.getText().toString().trim());
                } catch (Exception ignored) {
                }
            }
        });
    }

    private void validateFarmID(String root, String name) {
        Query query = Firebase.getInstance().getReference(root);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    validateName(root, name);
                } else {
                    showToast(getString(R.string.check_inputs));
                    Loading.dismissProgressDialog();
                }
            }

            private void validateName(String root, String name) {
                Firebase.getInstance().getReference(root).child("name").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            if (Objects.equals(snapshot.getValue(String.class), name)) {
                                listener.onDataEntered(root, name);
                                dismiss();
                            } else showToast(getString(R.string.check_inputs));
                        } else showToast(getString(R.string.check_inputs));
                        Loading.dismissProgressDialog();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Loading.dismissProgressDialog();
            }
        });
    }

    private boolean isValidInputs() {
        boolean isValid = true;

        isValid &= setInputError(name, name.getText().toString().isEmpty());
        isValid &= setInputError(root, root.getText().toString().isEmpty());

        return isValid;
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

    public interface AddNewFarmListener {
        void onDataEntered(String root, String name);
    }
}
