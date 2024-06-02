package com.ali.myfarm.Activities;

import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.ali.myfarm.Classes.Common;
import com.ali.myfarm.Fragments.BuyerTransaction;
import com.ali.myfarm.Fragments.TraderTransaction;
import com.ali.myfarm.R;

public class NewTransactionOperation extends AppCompatActivity {

    private String mainID;
    private String periodID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_transaction_operation);

        mainID = requireNonNullExtra(Common.MAIN_ID);
        periodID = requireNonNullExtra(Common.PERIOD_ID);

        if (mainID == null) {
            throw new IllegalArgumentException("Required data missing in intent extras");
        }

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, new TraderTransaction(mainID, periodID))
                    .commit();
        }

        setupLayout();
        initializeBackButton();
    }

    private void setupLayout() {
        TextView traderTextView = findViewById(R.id.trader);
        TextView buyerTextView = findViewById(R.id.buyer);

        traderTextView.setOnClickListener(view -> switchView(traderTextView, buyerTextView, new TraderTransaction(mainID, periodID)));
        buyerTextView.setOnClickListener(view -> switchView(buyerTextView, traderTextView, new BuyerTransaction(mainID, periodID)));
    }

    private void initializeBackButton() {
        ImageButton backButton = findViewById(R.id.back);
        backButton.setOnClickListener(view -> onBackPressed());
    }

    private void switchView(TextView selectedView, TextView otherView, Fragment fragmentToShow) {
        setFocus(selectedView);
        clearBackground(otherView);
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragmentToShow).commit();
    }

    private void setFocus(TextView textView) {
        TypedValue typedValue = new TypedValue();
        getTheme().resolveAttribute(android.R.attr.textColor, typedValue, true);
        Drawable background = ContextCompat.getDrawable(this, R.drawable.switch_track);
        applyTextStyle(textView, typedValue.data, Typeface.BOLD, background);
    }

    private void clearBackground(TextView textView) {
        textView.setBackground(null);
        applyTextStyle(textView, ContextCompat.getColor(this, R.color.black), Typeface.NORMAL, null);
    }

    private void applyTextStyle(TextView textView, int textColor, int textStyle, Drawable background) {
        textView.setTextColor(textColor);
        textView.setTypeface(null, textStyle);
        textView.setBackground(background);
    }

    private String requireNonNullExtra(String key) {
        if (getIntent().getExtras() == null) {
            throw new IllegalArgumentException("No extras provided in intent");
        }
        String value = getIntent().getStringExtra(key);
        if (value == null) {
            throw new IllegalArgumentException("Missing required extra: " + key);
        }
        return value;
    }
}