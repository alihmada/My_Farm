package com.ali.myfarm.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.ali.myfarm.Classes.Common;
import com.ali.myfarm.Fragments.BuyerBill;
import com.ali.myfarm.Fragments.TraderBill;
import com.ali.myfarm.R;

import java.util.Objects;

public class Bill extends AppCompatActivity implements TraderBill.OnFragmentInteractionListener, BuyerBill.OnFragmentInteractionListener {
    Bundle bundle;
    boolean isTrader;
    String mainID, periodID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill);

        bundle = new Bundle();
        mainID = Objects.requireNonNull(getIntent().getExtras()).getString(Common.MAIN_ID);
        periodID = Objects.requireNonNull(getIntent().getExtras()).getString(Common.PERIOD_ID);
        isTrader = Objects.requireNonNull(getIntent().getExtras()).getBoolean(Common.IS_TRADER);

        if (isTrader)
            getSupportFragmentManager().beginTransaction().replace(R.id.container, new TraderBill(mainID, periodID, getIntent().getStringExtra(Common.MOVED_DATA))).commit();
        else
            getSupportFragmentManager().beginTransaction().replace(R.id.container, new BuyerBill(mainID, periodID, getIntent().getStringExtra(Common.MOVED_DATA))).commit();

        ImageButton back = findViewById(R.id.back);
        back.setOnClickListener(view -> onBackPressed());
    }

    @Override
    public void onFinishActivity() {
        Intent intent = new Intent(Bill.this, Transaction.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        bundle.putString(Common.MAIN_ID, mainID);
        bundle.putString(Common.PERIOD_ID, periodID);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }
}