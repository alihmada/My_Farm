package com.ali.myfarm.Dialogs;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;

import com.ali.myfarm.Classes.Matcher;
import com.ali.myfarm.Classes.Vibrate;
import com.ali.myfarm.Models.Bag;
import com.ali.myfarm.Models.Feed;
import com.ali.myfarm.R;

public class FeedStatus extends DialogFragment {
    ConstraintLayout[] selectType, selectOperation;
    CheckBox[] checkBoxesOfType, checkBoxesOfOperation;
    EditText numberOfBags, priceOfTon;
    FeedDialogListener listener;
    Bag.Operation operation = null;
    Feed.Type type = null;

    public FeedStatus() {
    }

    public FeedStatus(FeedDialogListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = new Dialog(requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.feed_status_dialog);

        initializeView(dialog);

        Window window = dialog.getWindow();
        if (window != null) {
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window.setGravity(Gravity.BOTTOM);
            window.setWindowAnimations(R.style.dialog_animation);
        }
        return dialog;
    }

    private void initializeView(Dialog dialog) {
        numberOfBags = dialog.findViewById(R.id.num_of_bags);
        priceOfTon = dialog.findViewById(R.id.price_of_ton);

        initializeChooseType(dialog);
        initializeChooseOperation(dialog);
        initializeConfirmButton(dialog);
    }

    private void initializeChooseType(Dialog dialog) {
        selectType = new ConstraintLayout[]{dialog.findViewById(R.id.growing), dialog.findViewById(R.id.initialize), dialog.findViewById(R.id.end)};
        checkBoxesOfType = new CheckBox[]{dialog.findViewById(R.id.growing_checkBox), dialog.findViewById(R.id.initialize_checkBox), dialog.findViewById(R.id.end_checkBox)};

        View.OnClickListener onClickListener = view -> {
            if (view.getId() == R.id.growing) {
                type = Feed.Type.GROWING;
                setTypeOptionSelected(selectType, checkBoxesOfType, 0);
            } else if (view.getId() == R.id.initialize) {
                type = Feed.Type.BEGGING;
                setTypeOptionSelected(selectType, checkBoxesOfType, 1);
            } else {
                type = Feed.Type.END;
                setTypeOptionSelected(selectType, checkBoxesOfType, 2);
            }
        };

        for (ConstraintLayout layout : selectType)
            layout.setOnClickListener(onClickListener);
    }

    private void setTypeOptionSelected(ConstraintLayout[] parents, CheckBox[] checkBoxes, int indexOfSelectedLayout) {
        for (int index = 0; index < parents.length; index++) {
            if (index == indexOfSelectedLayout) {
                checkBoxes[index].setChecked(true);
                parents[index].setBackground(AppCompatResources.getDrawable(requireContext(), R.drawable.green_stroke));
            } else {
                checkBoxes[index].setChecked(false);
                parents[index].setBackground(AppCompatResources.getDrawable(requireContext(), R.drawable.gray_stroke));
            }
        }
    }

    private void initializeChooseOperation(Dialog dialog) {
        selectOperation = new ConstraintLayout[]{dialog.findViewById(R.id.add), dialog.findViewById(R.id.remove)};

        checkBoxesOfOperation = new CheckBox[]{dialog.findViewById(R.id.add_checkbox), dialog.findViewById(R.id.remove_checkbox)};

        View.OnClickListener onClickListener = view -> {
            if (view.getId() == R.id.add) {
                setOperationOptionSelected(selectOperation, checkBoxesOfOperation, 0, 1);
                operation = Bag.Operation.ADD;
                numberOfBags.setVisibility(View.VISIBLE);
                priceOfTon.setVisibility(View.VISIBLE);
            } else {
                setOperationOptionSelected(selectOperation, checkBoxesOfOperation, 1, 0);
                operation = Bag.Operation.REMOVE;
                numberOfBags.setVisibility(View.VISIBLE);
                priceOfTon.setVisibility(View.GONE);
            }
        };

        for (ConstraintLayout layout : selectOperation)
            layout.setOnClickListener(onClickListener);
    }

    private void setOperationOptionSelected(ConstraintLayout[] parents, CheckBox[] checkBoxes, int selectedItem, int deselectedItem) {
        checkBoxes[selectedItem].setChecked(true);
        parents[selectedItem].setBackground(AppCompatResources.getDrawable(requireContext(), R.drawable.green_stroke));

        checkBoxes[deselectedItem].setChecked(false);
        parents[deselectedItem].setBackground(AppCompatResources.getDrawable(requireContext(), R.drawable.gray_stroke));
    }

    private void initializeConfirmButton(Dialog dialog) {
        Button button = dialog.findViewById(R.id.confirm);
        button.setOnClickListener(view -> {
            if (isValidInputs()) {
                if (operation == Bag.Operation.ADD)
                    listener.onDataEntered(type, operation, Integer.parseInt(numberOfBags.getText().toString()), Integer.parseInt(priceOfTon.getText().toString()));
                else
                    listener.onDataEntered(type, operation, Integer.parseInt(numberOfBags.getText().toString()), 0);
                dismiss();
            }
        });
    }

    private boolean isValidInputs() {
        if (type == null) setSelectionError(selectType);
        if (operation == null) setSelectionError(selectOperation);

        setInputsError(numberOfBags, !Matcher.isNumber(numberOfBags.getText().toString()));
        setInputsError(priceOfTon, !Matcher.isPrice(priceOfTon.getText().toString()));

        if (operation != null && operation == Bag.Operation.ADD)
            return type != null && Matcher.isNumber(numberOfBags.getText().toString()) && Matcher.isPrice(priceOfTon.getText().toString());
        else
            return type != null && operation != null && Matcher.isNumber(numberOfBags.getText().toString());
    }

    private void setSelectionError(ConstraintLayout[] parents) {
        for (ConstraintLayout layout : parents) {
            layout.setBackground(AppCompatResources.getDrawable(requireContext(), R.drawable.red_stroke_with_1dp_width));
        }
        Vibrate.vibrate(requireContext());
    }

    private void setInputsError(EditText editText, boolean error) {
        if (editText.getVisibility() == View.VISIBLE) {
            if (error) {
                editText.setBackground(AppCompatResources.getDrawable(requireContext(), R.drawable.error_p));
                Vibrate.vibrate(requireContext());
            } else
                editText.setBackground(AppCompatResources.getDrawable(requireContext(), R.drawable.input_filed));
        }
    }

    public interface FeedDialogListener {
        void onDataEntered(Feed.Type type, Bag.Operation operation, int numberOfBags, double priceOfTon);
    }
}
