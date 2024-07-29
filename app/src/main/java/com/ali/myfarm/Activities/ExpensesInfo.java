package com.ali.myfarm.Activities;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.ali.myfarm.Classes.Calculation;
import com.ali.myfarm.Classes.Common;
import com.ali.myfarm.Classes.DateAndTime;
import com.ali.myfarm.Data.Firebase;
import com.ali.myfarm.Dialogs.Alert;
import com.ali.myfarm.Dialogs.Confirmation;
import com.ali.myfarm.Dialogs.ValueDialog;
import com.ali.myfarm.MVVM.SelectedExpensesViewModel;
import com.ali.myfarm.Models.Expenses;
import com.ali.myfarm.R;

import java.util.Objects;

public class ExpensesInfo extends AppCompatActivity {

    private Handler handler;
    private Expenses expenses;
    private SelectedExpensesViewModel model;
    private String year, month, id;
    private TextView balance, reason, date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expenses_info);

        startPoint();
    }

    private void startPoint() {
        handler = new Handler(Looper.getMainLooper());

        getExtras();
        setupTextView();
        initializeButtons();
        setupViewModel();
        setupViewsData();
    }

    private void getExtras() {
        year = Objects.requireNonNull(getIntent().getExtras()).getString(Common.YEAR);
        month = Objects.requireNonNull(getIntent().getExtras()).getString(Common.MONTH);
        id = Objects.requireNonNull(getIntent().getExtras()).getString(Common.MOVED_DATA);
    }

    private void setupTextView() {
        date = findViewById(R.id.date_time);
        balance = findViewById(R.id.balance);
        reason = findViewById(R.id.reason);
    }

    private void initializeButtons() {
        findViewById(R.id.back).setOnClickListener(view -> onBackPressed());

        findViewById(R.id.delete).setOnClickListener(view -> handleDeleteButton());

        findViewById(R.id.edit_balance).setOnClickListener(view -> handleEditButton(
                R.string.enter_balance,
                ValueDialog.inputType.DECIMAL,
                Common.DECIMAL_REGEX,
                text -> {
                    expenses.setDate(DateAndTime.getCurrentDateTime());
                    expenses.setBalance(Double.parseDouble(text));
                    Firebase.editExpenses(this, year, month, expenses);
                }
        ));

        findViewById(R.id.edit_reason).setOnClickListener(view -> handleEditButton(
                R.string.enter_reason,
                ValueDialog.inputType.TEXT,
                Common.NOT_EMPTY_REGEX,
                text -> {
                    expenses.setDate(DateAndTime.getCurrentDateTime());
                    expenses.setReason(text);
                    Firebase.editExpenses(this, year, month, expenses);
                }
        ));
    }

    private void handleDeleteButton() {
        if (!Common.isFinished) {
            Confirmation confirmation = new Confirmation(R.drawable.wrong, getString(R.string.delete_expenses), () -> {
                Firebase.deleteExpenses(this, year, month, expenses);
                finish();
            });
            confirmation.show(getSupportFragmentManager(), "");
        } else {
            showAlert(getString(R.string.finished));
        }
    }

    private void handleEditButton(int titleResId, ValueDialog.inputType inputType, String regex, ValueDialog.OnValueSetListener onValueSetListener) {
        if (!Common.isFinished) {
            ValueDialog dialog = new ValueDialog(titleResId, inputType, regex, onValueSetListener::onValueSet);
            dialog.show(getSupportFragmentManager(), "");
        } else {
            showAlert(getString(R.string.finished));
        }
    }

    private void setupViewModel() {
        model = new ViewModelProvider(this).get(SelectedExpensesViewModel.class);
        model.initialize(this, year, month, id);
    }

    private void setupViewsData() {
        model.getExpensesItem().observe(this, expenses -> {
            if (expenses != null) {
                this.expenses = expenses;

                date.setText(DateAndTime.getDate(this, expenses.getDate()));

                balance.setText(Calculation.formatNumberWithCommas(expenses.getBalance()));
                reason.setText(expenses.getReason());
            }
        });
    }

    private void showAlert(String message) {
        handler.post(() -> new Alert(R.drawable.error, message).show(getSupportFragmentManager(), ""));
    }
}